package com.classified.seller.controller;

import com.classified.seller.commons.dto.request.DestinationRequest;
import com.classified.seller.commons.dto.response.DestinationResponse;
import com.classified.seller.commons.entity.Destination;
import com.classified.seller.commons.entity.Target;
import com.classified.seller.commons.mapper.DestinationMapper;
import com.classified.seller.commons.service.DestinationService;
import com.classified.seller.commons.service.TargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/destinations")
public class DestinationController {

    @Autowired
    private DestinationService destinationService;

    @Autowired
    private TargetService targetService;

    private DestinationMapper destinationMapper = DestinationMapper.MAPPER;

    @PostMapping("/")
    public DestinationResponse addDestination(@RequestBody DestinationRequest destinationRequest) {
        List<Target> targets = targetService.getTargetsById(destinationRequest.getTargetIds());
        Destination destination = destinationService.saveDestination(destinationMapper.toEntity(destinationRequest) , targets);
        return destinationMapper.toResponse(destination);
    }

    @GetMapping("/")
    public List<DestinationResponse> getAllDestinations() {
        return destinationService.getAllDestinations().stream().map(destination -> destinationMapper.toResponse(destination)).collect(Collectors.toList());
    }

}
