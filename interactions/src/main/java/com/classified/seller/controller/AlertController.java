package com.classified.seller.controller;

import com.classified.seller.commons.dto.request.AlertRequest;
import com.classified.seller.commons.dto.response.AlertResponse;
import com.classified.seller.commons.entity.Alert;
import com.classified.seller.commons.enumeration.AlertLevelEnum;
import com.classified.seller.commons.enumeration.AlertStatusEnum;
import com.classified.seller.commons.mapper.AlertMapper;
import com.classified.seller.commons.service.AlertService;
import com.classified.seller.commons.service.ClusterService;
import com.classified.seller.commons.service.RegexService;
import com.classified.seller.commons.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController()
@RequestMapping("/alerts")
public class AlertController {

    @Autowired
    private AlertService alertService;
    @Autowired
    private ClusterService clusterService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private RegexService regexService;

    private AlertMapper alertMapper = AlertMapper.MAPPER;

    @PostMapping("/")
    public AlertResponse addAlert(@RequestBody AlertRequest alertRequest) {
        Alert alert = new Alert();
        alert.setId(0L);
        alert.setLag(alertRequest.getLag());
        alert.setTopic(alertRequest.getTopic());
        alert.setConsumerGroup(alertRequest.getConsumerGroup());
        alert.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        alert.setCluster(clusterService.getCluster(alertRequest.getClusterId()));
        if (alertRequest.getRuleId() != null) {
            alert.setRule(ruleService.getRuleById(alertRequest.getRuleId()));
            alert.setLevel(AlertLevelEnum.ERROR);
            if (alert.getLag() < alert.getRule().getLowerBoundary()) {
                alert.setMessage("[Cluster: " + alert.getCluster().getName()+ "] Lag found: " + alert.getLag() + " is lower than boundary: " +
                        "" + alert.getRule().getLowerBoundary() + " for  topic: " + alert.getTopic() + " and consumer group: " + alert.getConsumerGroup());
                alert.setStatus(AlertStatusEnum.LOWER);
            } else {
                alert.setMessage("[Cluster: " + alert.getCluster().getName()+ "] Lag found: " + alert.getLag() + " is higher than boundary: " +
                        "" + alert.getRule().getUpperBoundary() + " for  topic: " + alert.getTopic() + " and consumer group: " + alert.getConsumerGroup());
                alert.setStatus(AlertStatusEnum.HIGHER);
            }
        }
        else if (alertRequest.getRegexId() != null) {
            alert.setRegex(regexService.getRegexById(alertRequest.getRegexId()));
            alert.setMessage("[Cluster: " + alert.getCluster().getName()+ "] Rule definition needed because regex relation found for " +
                    "topic: " + alert.getTopic() + " and consumer group: " + alert.getConsumerGroup() + " with " +
                    "topic regex: " + alert.getRegex().getTopic() + " and consumer group regex: " + alert.getRegex().getConsumerGroup());
            alert.setLevel(AlertLevelEnum.WARNING);
            alert.setStatus(AlertStatusEnum.NOT_FOUND);
        }

        alert.setProcessed(Boolean.FALSE);
        alert = alertService.saveAlert(alert);
        return alertMapper.toReponse(alert);
    }

}
