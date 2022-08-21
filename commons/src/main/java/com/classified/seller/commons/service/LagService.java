package com.classified.seller.commons.service;

import com.classified.seller.commons.dto.inner.LagDTO;
import com.classified.seller.commons.entity.Cluster;
import com.classified.seller.commons.entity.Lag;
import com.classified.seller.commons.repository.LagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class LagService {

    @Autowired
    private LagRepository lagRepository;

    @Autowired
    @Lazy
    private ClusterService clusterService;

    public void createLag(Lag lag) {
        lag.setId(0L);
        lagRepository.save(lag);
    }

    public List<Lag> getLagsBetweenTopicAndConsumerGroup(String topic, String consumerGroup, Cluster cluster) {
        return lagRepository.getLagsBetweenTopicAndConsumerGroup(topic, consumerGroup, cluster);
    }

    // Creates lag records between consumer groups and topics.
    public void createLagsForGroup(Cluster cluster, String groupId, Map<String, LagDTO> lags) {
        for (Map.Entry<String, LagDTO> lagEntry : lags.entrySet()) {
            String topic = lagEntry.getKey();
            Long lag = lagEntry.getValue().getLag();
            createLag(Lag.builder().
                    timestamp(Timestamp.valueOf(LocalDateTime.now())).
                    topic(topic).
                    value(lag).
                    consumerGroup(groupId).
                    cluster(cluster).
                    build());
        }
    }

    // Analyzes all clusters.
    public void analyzeLag() {
        List<Cluster> clusters = clusterService.getAllClusters();
        clusters.forEach(cluster -> {
            if (cluster.getActive())
                clusterService.analyzeLagOfCluster(cluster.getId());
        });
    }

    public void createMocklags() {
        Cluster cluster = clusterService.getCluster(1L);
        Long millisecond = 1654030800000L;
        // Timestamp timestamp = Timestamp.valueOf("2022-06-01T00:00:00.000+00:00");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
        for (int i = 1; i <= 30 ; i++) {
            for (int j = 0 ; j < 24 ; j++) {
                // long localDateTime = LocalDateTime.of(2022,6, i, j, 0,0,0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                //LocalDateTime localDateTime = LocalDateTime.of(2022,6, i, j, 0,0,0);
                createLag(Lag.builder()
                        .cluster(cluster)
                        .consumerGroup("my-first-application1")
                        .topic("test_topic_1")
                        .value((long) (Math.random() * 30))
                        .timestamp(new Timestamp(2022,6,i,j,0,0,0))
                        .build());
            }
        }
        System.out.println("CREATED ALL MOCK LAGS");
    }

}
