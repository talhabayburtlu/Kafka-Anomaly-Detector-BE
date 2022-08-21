package com.classified.seller.commons.repository;

import com.classified.seller.commons.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query(value = "SELECT a FROM Alert a WHERE a.processed = false")
    List<Alert> getUnprocessedAlerts();
}
