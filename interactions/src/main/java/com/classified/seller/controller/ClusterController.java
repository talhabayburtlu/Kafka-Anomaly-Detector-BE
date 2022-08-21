package com.classified.seller.controller;

import com.classified.seller.commons.dto.request.ClusterRequest;
import com.classified.seller.commons.dto.request.RegexRequest;
import com.classified.seller.commons.dto.request.RuleRequest;
import com.classified.seller.commons.dto.response.*;
import com.classified.seller.commons.entity.Cluster;
import com.classified.seller.commons.entity.Destination;
import com.classified.seller.commons.entity.Regex;
import com.classified.seller.commons.entity.Rule;
import com.classified.seller.commons.mapper.ClusterMapper;
import com.classified.seller.commons.mapper.LagMapper;
import com.classified.seller.commons.mapper.RegexMapper;
import com.classified.seller.commons.mapper.RuleMapper;
import com.classified.seller.commons.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController()
@RequestMapping("/clusters")
public class ClusterController {
    @Autowired
    private ClusterService clusterService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private RegexService regexService;
    @Autowired
    private LagService lagService;
    @Autowired
    private DestinationService destinationService;
    private ClusterMapper clusterMapper = ClusterMapper.MAPPER;
    private RuleMapper ruleMapper = RuleMapper.MAPPER;
    private RegexMapper regexMapper = RegexMapper.MAPPER;
    private LagMapper lagMapper = LagMapper.MAPPER;

    @PostMapping("/")
    public ClusterResponse addCluster(@RequestBody ClusterRequest clusterRequest) {
        Cluster cluster = clusterService.saveCluster(clusterMapper.toEntity(clusterRequest));
        return clusterMapper.toResponse(cluster);
    }

    @PostMapping("/{id}/rules/destinations/{destinationId}")
    public RuleResponse addRule(@RequestBody RuleRequest ruleRequest, @PathVariable(value = "id") Long id, @PathVariable(value = "destinationId") Long destinationId) {
        Cluster cluster = clusterService.getCluster(id);
        Destination destination = destinationService.getDestinationById(destinationId);
        Rule rule = ruleService.addRule(ruleMapper.toEntity(ruleRequest), cluster, destination);
        return ruleMapper.toResponse(rule);
    }

    @PostMapping("/{id}/regex/destinations/{destinationId}")
    public RegexResponse addRegex(@RequestBody RegexRequest regexRequest, @PathVariable(value = "id") Long id, @PathVariable(value = "destinationId") Long destinationId) {
        Cluster cluster = clusterService.getCluster(id);
        Destination destination = destinationService.getDestinationById(destinationId);
        Regex regex = regexService.addRegex(regexMapper.toEntity(regexRequest), cluster, destination);
        return regexMapper.toResponse(regex);
    }

    @GetMapping("/")
    public List<ClusterResponse> getAllClusters() {
        return clusterService.getAllClusters().stream().map(cluster -> clusterMapper.toResponse(cluster)).collect(Collectors.toList());
    }
    @GetMapping("/{id}/topics")
    public List<String> getAllTopics(@PathVariable(value = "id") Long id) {
        return clusterService.getTopicsFromCluster(id);
    }

    @GetMapping("/{id}/consumers")
    public List<String> getAllConsumerGroups(@PathVariable(value = "id") Long id) {
        return clusterService.getConsumerGroupsFromCluster(id);
    }

    @GetMapping("/{id}/consumers/{consumerGroup}")
    public List<String> getAllConsumerGroups(@PathVariable(value = "id") Long id, @PathVariable(value = "consumerGroup") String consumerGroup) {
        return clusterService.getTopicsOfConsumerGroup(consumerGroup,id);
    }

    @GetMapping("/{id}/consumers/{consumerGroup}/topics/{topic}/lags")
    public AllLagsResponse getAllConsumerGroups(@PathVariable(value = "id") Long id,
                                                      @PathVariable(value = "consumerGroup") String consumerGroup,
                                                      @PathVariable(value = "topic") String topic) {
        List<LagResponse> lags = lagService.getLagsBetweenTopicAndConsumerGroup(topic, consumerGroup, clusterService.getCluster(id))
                .stream().map(lagMapper::toResponse).collect(Collectors.toList());
        Rule rule = ruleService.getRule(topic, consumerGroup);
        return AllLagsResponse.builder()
                .lags(lags)
                .lowerBoundary(rule != null ? rule.getLowerBoundary() : null)
                .upperBoundary(rule != null ? rule.getUpperBoundary() : null)
                .build();
    }

}
