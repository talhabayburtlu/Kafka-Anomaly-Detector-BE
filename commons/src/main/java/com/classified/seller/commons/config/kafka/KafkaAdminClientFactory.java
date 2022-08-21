package com.classified.seller.commons.config.kafka;

import com.classified.seller.commons.entity.Cluster;
import com.classified.seller.commons.service.ClusterService;
import com.classified.seller.commons.util.ObjectSerializer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


// This factory maintains KafkaAdminClients for a cluster.
// It prevents creating multiple KafkaAdminClients for a cluster.
@Component
@Transactional
public class KafkaAdminClientFactory {

    private ConcurrentHashMap<Long, KafkaAdminClient> clients;
    @Autowired
    private ClusterService clusterService;

    public KafkaAdminClientFactory(ClusterService clusterService) {
        this.clusterService = clusterService;
        this.clients = new ConcurrentHashMap<>();
    }

    private KafkaAdminClient createAdminClient(Cluster cluster) {
        Properties config = new Properties();
        Map<String, String> configMap = (Map<String, String>) ObjectSerializer.deserialize(cluster.getConfig());
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, cluster.getAddress());
        if (configMap != null)
            config.putAll(configMap);

        return (KafkaAdminClient) KafkaAdminClient.create(config);
    }

    public KafkaAdminClient getAdminClient(Long clusterId) {
        Cluster cluster = clusterService.getCluster(clusterId);
        return getAdminClient(cluster);
    }
    // Gets available KafkaAdminClient for a cluster.
    public KafkaAdminClient getAdminClient(Cluster cluster) {

        KafkaAdminClient kafkaAdminClient = clients.get(cluster.getId());
        if (kafkaAdminClient != null) {
            return kafkaAdminClient;
        }

        kafkaAdminClient = createAdminClient(cluster);
        clients.put(cluster.getId(), kafkaAdminClient);
        return kafkaAdminClient;
    }

}
