package com.classified.seller.commons.repository;

import com.classified.seller.commons.entity.Cluster;
import com.classified.seller.commons.entity.Regex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegexRepository extends JpaRepository<Regex, Long> {

    @Query(value = "SELECT r FROM Regex r WHERE r.cluster = ?1")
    List<Regex> getAllRegexByCluster(Cluster cluster);

}
