package com.classified.seller.commons.service;

import com.classified.seller.commons.dto.inner.LagDTO;
import com.classified.seller.commons.entity.Alert;
import com.classified.seller.commons.entity.Cluster;
import com.classified.seller.commons.entity.Destination;
import com.classified.seller.commons.entity.Rule;
import com.classified.seller.commons.enumeration.AlertLevelEnum;
import com.classified.seller.commons.enumeration.AlertStatusEnum;
import com.classified.seller.commons.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RuleService {

    @Autowired
    private AlertService alertService;
    @Autowired
    private RuleRepository ruleRepository;

    @Transactional
    public Rule addRule(Rule rule, Cluster cluster, Destination destination) {
        rule.setId(0L);
        rule.setCluster(cluster);
        rule.setDestination(destination);
        return ruleRepository.saveAndFlush(rule);
    }

    public Rule getRuleById(Long id) {
        return ruleRepository.getReferenceById(id);
    }

    public Rule getRule(String topic, String consumerGroup){
        return ruleRepository.getRule(topic, consumerGroup);
    }

    public List<Rule> getAllRulesByCluster(Cluster cluster){
        return ruleRepository.getAllRulesByCluster(cluster);
    }

    public void checkAllRules(Cluster cluster, Map<String, Map<String, LagDTO>> groupLags) {
        Map<Rule,Boolean> rules = getAllRulesByCluster(cluster).stream().collect(Collectors.toMap(r -> r , r -> false));
        rules.entrySet().forEach(entry -> {
            Rule rule = entry.getKey();
            Map<String, LagDTO> groupLag = groupLags.get(rule.getConsumerGroup());
            if (groupLag == null)
                return;

            LagDTO lagDTO = groupLag.get(rule.getTopic());
            if (lagDTO == null)
                return;

            checkRule(rule, lagDTO.getLag(), cluster);
            lagDTO.setVisited(Boolean.TRUE);
            entry.setValue(Boolean.TRUE);
        });

        Map<Rule,Boolean> untraversedRules = rules.entrySet().stream()
                .filter(ruleBooleanEntry -> !ruleBooleanEntry.getValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // For untraversed rules throw error alert.
        // A rule only can be traversed if the topic and consumer group exist and consumer group is connected to topic.
        untraversedRules.forEach((rule, travelled) -> {
            System.out.println("[ConsumerService#checkRule]: [ALERT] Rule found between topic: " + rule.getTopic() + " and consumer group: " + rule.getConsumerGroup() + " but lag couldn't found.");
            alertService.saveAlert(Alert.builder()
                    .message("[Cluster: " + cluster.getName() + "] Rule found between topic: " + rule.getTopic() + " and consumer group: " + rule.getConsumerGroup() + " but lag couldn't found.")
                    .status(AlertStatusEnum.NOT_FOUND)
                    .level(AlertLevelEnum.ERROR)
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                    .processed(Boolean.FALSE)
                    .cluster(cluster)
                    .rule(rule)
                    .build());
        });

    }
    public void checkRule(Rule rule, Long lag, Cluster cluster) {
        String topic = rule.getTopic(), consumerGroup = rule.getConsumerGroup();
        Long lowerBoundary = rule.getLowerBoundary(), upperBoundary = rule.getUpperBoundary();
        if (lowerBoundary > lag) {
            System.out.println("[ConsumerService#checkRule]: [ALERT] Lag found: " + lag + " is lower than boundary: " + lowerBoundary +  " for  topic: " + topic + " and consumer group: " + consumerGroup);
            alertService.saveAlert(Alert.builder()
                    .consumerGroup(consumerGroup)
                    .topic(topic)
                    .message("[Cluster: " + cluster.getName()+ "] Lag found: " + lag + " is lower than boundary: " + lowerBoundary +  " for  topic: " + topic + " and consumer group: " + consumerGroup)
                    .lag(lag)
                    .status(AlertStatusEnum.LOWER)
                    .level(AlertLevelEnum.ERROR)
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                    .processed(Boolean.FALSE)
                    .cluster(cluster)
                    .rule(rule)
                    .build());
        }
        else if (upperBoundary < lag) {
            System.out.println("[ConsumerService#checkRule]: [ALERT] Lag found: " + lag + " is higher than boundary: " + upperBoundary +  " for  topic: " + topic + " and consumer group: " + consumerGroup);
            alertService.saveAlert(Alert.builder()
                    .cluster(cluster)
                    .rule(rule)
                    .consumerGroup(consumerGroup)
                    .topic(topic)
                    .message("[Cluster: " + cluster.getName()+ "] Lag found: " + lag + " is higher than boundary: " + upperBoundary +  " for  topic: " + topic + " and consumer group: " + consumerGroup)
                    .lag(lag)
                    .status(AlertStatusEnum.LOWER)
                    .level(AlertLevelEnum.ERROR)
                    .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                    .processed(Boolean.FALSE)
                    .build());
        }
    }

}
