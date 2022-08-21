package com.classified.seller.commons.repository;

import com.classified.seller.commons.entity.Cluster;
import com.classified.seller.commons.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Long> {

    @Query(value = "SELECT r FROM Rule r WHERE r.topic LIKE ?1 AND r.consumerGroup LIKE ?2")
    Rule getRule(String topic, String consumerGroup);

    @Query(value = "SELECT r FROM Rule r WHERE r.cluster = ?1")
    List<Rule> getAllRulesByCluster(Cluster cluster);

}
