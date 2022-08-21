package com.classified.seller.commons.repository;

import com.classified.seller.commons.entity.Cluster;
import com.classified.seller.commons.entity.Lag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LagRepository extends JpaRepository<Lag, Long> {

    @Query(value = "SELECT l FROM Lag l WHERE l.topic = ?1 AND l.consumerGroup = ?2 AND l.cluster = ?3 ORDER BY l.timestamp")
    List<Lag> getLagsBetweenTopicAndConsumerGroup(String topic, String consumerGroup, Cluster cluster);

}
