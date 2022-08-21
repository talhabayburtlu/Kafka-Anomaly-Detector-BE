package com.classified.seller.commons.repository;

import com.classified.seller.commons.entity.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long> {

    @Query(value = "SELECT c FROM Cluster c WHERE c.active = true")
    List<Cluster> getActiveClusters();

}
