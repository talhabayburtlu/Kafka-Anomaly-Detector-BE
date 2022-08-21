package com.classified.seller.commons.service;

import com.classified.seller.commons.config.kafka.KafkaAdminClientFactory;
import com.classified.seller.commons.dto.inner.LagDTO;
import com.classified.seller.commons.entity.Cluster;
import com.classified.seller.commons.repository.ClusterRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ClusterService {
    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private LagService lagService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private RegexService regexService;
    @Autowired
    private ClusterRepository clusterRepository;
    @Autowired
    @Lazy
    private KafkaAdminClientFactory kafkaAdminClientFactory;

    public Cluster saveCluster(Cluster cluster) {
        cluster.setId(0L);
        return clusterRepository.saveAndFlush(cluster);
    }

    public Cluster getCluster(Long id) {
        return clusterRepository.getReferenceById(id);
    }

    public List<Cluster> getAllClusters() {
        return clusterRepository.getActiveClusters();
    }

    public List<String> getTopicsFromCluster(Long id) {
        KafkaAdminClient kafkaAdminClient = null;
        try {
            kafkaAdminClient = kafkaAdminClientFactory.getAdminClient(id);
            return new ArrayList<>(kafkaAdminClient.listTopics().names().get());
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("[ClusterService#getTopicsFromCluster] Could not connected to cluster with id: " +  id);
        }
        return new ArrayList<>();
    }

    public List<String> getConsumerGroupsFromCluster(Long id) {
        KafkaAdminClient kafkaAdminClient = null;
        try {
            kafkaAdminClient = kafkaAdminClientFactory.getAdminClient(id);
            return kafkaAdminClient.listConsumerGroups().valid().get().stream().map(ConsumerGroupListing::groupId).collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("[ClusterService#getConsumerGroupsFromCluster] Could not connected to cluster with id: " +  id);
        }
        return new ArrayList<>();
    }

    //Analyzes lag of a cluster.
    public void analyzeLagOfCluster(Long id) {
        KafkaAdminClient kafkaAdminClient = null;
        Collection<ConsumerGroupListing> consumerGroupListings = null;
        try {
            kafkaAdminClient = kafkaAdminClientFactory.getAdminClient(id);
            consumerGroupListings = kafkaAdminClient.listConsumerGroups().valid().get();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("[ClusterService#liveLagAnalysis] Could not connected to cluster with id: " +  id);
            return;
        }

        analyzeLagOfCluster(id, consumerGroupListings);
    }

    public List<String> getTopicsOfConsumerGroup(String consumerGroup, Long clusterId) {
        Cluster cluster = getCluster(clusterId);
        return new ArrayList<>(consumerService.analyzeLag(cluster, consumerGroup).keySet());
    }

    //Analyzes lag of a cluster.
    private void analyzeLagOfCluster(Long id, Collection<ConsumerGroupListing> consumerGroupListings ) {
        Cluster cluster = getCluster(id);
        Map<String, Map<String, LagDTO>> groupLags = new HashMap<>();

        // Creating lags of consumerGroup-topic pairs inside of a cluster.
        consumerGroupListings.forEach(c -> {
            Map<String, LagDTO> groupLag = consumerService.analyzeLag(cluster, c.groupId());
            lagService.createLagsForGroup(cluster, c.groupId(), groupLag);
            groupLags.put(c.groupId(), groupLag);
        });

        // Checking all rules between consumerGroup-topic pairs for creating alerts if boundaries are exceeded.
        ruleService.checkAllRules(cluster,groupLags);

        // Checking all regex relations between consumerGroup-topic pairs for creating alerts if a rule is not applied to them.
        Map<String, Map<String, LagDTO>> notTraversedGroupLags = getNotTraversedGroupLags(groupLags); // Getting not travelled lags after checking rules.
        regexService.checkAllRegex(cluster, notTraversedGroupLags);
    }

    // Gets not travelled lags of consumerGroup-topic pairs.
    private Map<String, Map<String, LagDTO>> getNotTraversedGroupLags(Map<String, Map<String, LagDTO>> groupLags) {
        Map<String, Map<String, LagDTO>> untraversedGroupLags = new HashMap<>();
        for (Map.Entry<String, Map<String, LagDTO>> groupLag : groupLags.entrySet()) {
            Map<String, LagDTO> untraversedLags = groupLag.getValue().entrySet().stream()
                    .filter(stringLagDTOEntry -> !stringLagDTOEntry.getValue().getVisited())
                    .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue));
            if (!untraversedLags.isEmpty())
                untraversedGroupLags.put(groupLag.getKey(), untraversedLags);
        }
        return untraversedGroupLags;
    }
}
