package com.classified.seller.commons.service;

import com.classified.seller.commons.config.kafka.KafkaAdminClientFactory;
import com.classified.seller.commons.config.kafka.KafkaConsumerFactory;
import com.classified.seller.commons.dto.inner.LagDTO;
import com.classified.seller.commons.entity.Cluster;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ConsumerService {

    @Autowired
    @Lazy
    private KafkaAdminClientFactory kafkaAdminClientFactory;
    @Autowired
    @Lazy
    private KafkaConsumerFactory kafkaConsumerFactory;

    // Analyzes lag of a consumer group.
    public Map<String, LagDTO> analyzeLag(Cluster cluster, String groupId) {
        Map<TopicPartition, Long> consumerGrpOffsets = getConsumerGrpOffsets(groupId, cluster);
        Map<TopicPartition, Long> producerOffsets = getProducerOffsets(consumerGrpOffsets, cluster);
        return computeLags(consumerGrpOffsets, producerOffsets);
    }

    // Gets consumer groups offsets on topic partitions.
    private Map<TopicPartition, Long> getConsumerGrpOffsets(String groupId, Cluster cluster) {
        KafkaAdminClient kafkaAdminClient = null;
        Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap = null;
        try {
            kafkaAdminClient = kafkaAdminClientFactory.getAdminClient(cluster);
            ListConsumerGroupOffsetsResult info = kafkaAdminClient.listConsumerGroupOffsets(groupId);
            topicPartitionOffsetAndMetadataMap = info.partitionsToOffsetAndMetadata().get();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("[ConsumerService#getConsumerGrpOffsets] Could not connected to cluster with id: " +  cluster.getId());
            return new HashMap<>();
        }

        Map<TopicPartition, Long> groupOffset = new HashMap<>();
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : topicPartitionOffsetAndMetadataMap.entrySet()) {
            TopicPartition key = entry.getKey();
            OffsetAndMetadata metadata = entry.getValue();
            groupOffset.putIfAbsent(new TopicPartition(key.topic(), key.partition()), metadata.offset());
        }
        return groupOffset;
    }

    // Gets topic end offsets related with consumer groups.
    private Map<TopicPartition, Long> getProducerOffsets(Map<TopicPartition, Long> consumerGrpOffset, Cluster cluster) {
        List<TopicPartition> topicPartitions = new LinkedList<>();
        for (Map.Entry<TopicPartition, Long> entry : consumerGrpOffset.entrySet()) {
            TopicPartition key = entry.getKey();
            topicPartitions.add(new TopicPartition(key.topic(), key.partition()));
        }

        KafkaConsumer kafkaConsumer = null;
        try {
            kafkaConsumer = kafkaConsumerFactory.getKafkaConsumer(cluster);
            return kafkaConsumer.endOffsets(topicPartitions);
        } catch (Exception e) {
            System.out.println("[ConsumerService#getProducerOffsets] Could not connected to cluster with id: " +  cluster.getId());
            return new HashMap<>();
        }
    }

    // Computes lags between consumer groups and topics by extracting end offsets and current offsets.
    private Map<String, LagDTO> computeLags(Map<TopicPartition, Long> consumerGrpOffsets, Map<TopicPartition, Long> producerOffsets) {
        Map<TopicPartition, Long> lags = new HashMap<>();
        for (Map.Entry<TopicPartition, Long> entry : consumerGrpOffsets.entrySet()) {
            Long producerOffset = producerOffsets.get(entry.getKey());
            Long consumerOffset = consumerGrpOffsets.get(entry.getKey());
            long lag = Math.abs(producerOffset - consumerOffset);
            lags.putIfAbsent(entry.getKey(), lag);
        }

        // Merging partition lags of a topic.
        Map<String, Long> convertedLags = new HashMap<>();
        for (Map.Entry<TopicPartition, Long> entry : lags.entrySet()) {
            convertedLags.merge(entry.getKey().topic(), entry.getValue() , Long::sum);
        }

        return convertedLags.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> LagDTO.builder().lag(entry.getValue()).visited(Boolean.FALSE).build())
        );
    }


}
