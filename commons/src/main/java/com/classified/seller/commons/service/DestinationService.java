package com.classified.seller.commons.service;

import com.classified.seller.commons.entity.Destination;
import com.classified.seller.commons.entity.Target;
import com.classified.seller.commons.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DestinationService {
    @Autowired
    private DestinationRepository destinationRepository;

    public Destination saveDestination(Destination destination) {
        destination.setId(0L);
        return destinationRepository.saveAndFlush(destination);
    }

    public Destination saveDestination(Destination destination, List<Target> targets) {
        destination.setTargets(targets);
        return saveDestination(destination);
    }

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Destination getDestinationById(Long id) {
        return destinationRepository.getReferenceById(id);
    }

}
