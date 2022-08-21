package com.classified.seller.commons.service;

import com.classified.seller.commons.entity.Target;
import com.classified.seller.commons.repository.TargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TargetService {
    @Autowired
    private TargetRepository targetRepository;

    public Target saveTarget(Target target) {
        target.setId(0L);
        return targetRepository.saveAndFlush(target);
    }

    public List<Target> getTargetsById(List<Long> ids) {
        return targetRepository.getTargetsById(ids);
    }

    public List<Target> getAllTargets() {
        return targetRepository.findAll();
    }

}
