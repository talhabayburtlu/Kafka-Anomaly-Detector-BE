package com.classified.seller.commons.config.kafka;

import com.classified.seller.commons.entity.Cluster;
import com.classified.seller.commons.service.ClusterService;
import com.classified.seller.commons.util.ObjectSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

// This factory maintains KafkaConsumers for a cluster.
// It prevents creating multiple KafkaConsumers for a cluster.
@Component
@Transactional
public class KafkaConsumerFactory {
    private ConcurrentHashMap<Long, KafkaConsumer> consumers;
    @Autowired
    private ClusterService clusterService;

    public KafkaConsumerFactory(ClusterService clusterService) {
        this.clusterService = clusterService;
        this.consumers = new ConcurrentHashMap<>();
    }

    private KafkaConsumer getConsumer(Cluster cluster) {
        Properties properties = new Properties();
        Map<String, String> configMap = (Map<String, String>) ObjectSerializer.deserialize(cluster.getConfig());
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, cluster.getAddress());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        if (configMap != null)
            properties.putAll(configMap);

        return new KafkaConsumer<>(properties);
    }

    public KafkaConsumer getKafkaConsumer(Long clusterId) {
        Cluster cluster = clusterService.getCluster(clusterId);
        return getKafkaConsumer(cluster);
    }

    // Gets available KafkaConsumer for a cluster.
    public KafkaConsumer getKafkaConsumer(Cluster cluster) {
        KafkaConsumer kafkaConsumer = consumers.get(cluster.getId());
        if (kafkaConsumer != null) {
            return kafkaConsumer;
        }

        kafkaConsumer = getConsumer(cluster);
        consumers.put(cluster.getId(), kafkaConsumer);
        return kafkaConsumer;
    }

}
