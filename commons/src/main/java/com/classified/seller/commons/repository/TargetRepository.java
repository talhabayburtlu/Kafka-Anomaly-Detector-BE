package com.classified.seller.commons.repository;

import com.classified.seller.commons.entity.Target;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TargetRepository extends JpaRepository<Target, Long> {

    @Query(value = "SELECT t FROM Target t WHERE t.id IN ?1")
    List<Target> getTargetsById(List<Long> ids);

}
