package com.classified.seller.commons.service;

import com.classified.seller.commons.dto.inner.LagDTO;
import com.classified.seller.commons.entity.Alert;
import com.classified.seller.commons.entity.Cluster;
import com.classified.seller.commons.entity.Destination;
import com.classified.seller.commons.entity.Regex;
import com.classified.seller.commons.enumeration.AlertLevelEnum;
import com.classified.seller.commons.enumeration.AlertStatusEnum;
import com.classified.seller.commons.repository.RegexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@Service
public class RegexService {
    @Autowired
    private AlertService alertService;
    @Autowired
    private RegexRepository regexRepository;

    public Regex addRegex(Regex regex, Cluster cluster, Destination destination) {
        checkValidRegex(regex.getTopic());
        checkValidRegex(regex.getConsumerGroup());

        regex.setId(0L);
        regex.setCluster(cluster);
        regex.setDestination(destination);
        return regexRepository.saveAndFlush(regex);
    }

    public Regex getRegexById(Long id){
        return regexRepository.getReferenceById(id);
    }

    public List<Regex> getAllRegexByCluster(Cluster cluster) {
        return regexRepository.getAllRegexByCluster(cluster);
    }

    // Checks all regex relations between consumerGroup-topic pairs for creating alerts if a rule is not applied to them.
    public void checkAllRegex(Cluster cluster, Map<String, Map<String, LagDTO>> groupLags) {
        List<Regex> regexList = getAllRegexByCluster(cluster);
        groupLags.forEach((consumerGroup, lags) -> {
            regexList.forEach(regex -> {
                Pattern topicPattern = Pattern.compile(regex.getTopic()), consumerGroupPattern = Pattern.compile(regex.getConsumerGroup());
                if (!consumerGroupPattern.matcher(consumerGroup).find()) // Check whether this consumer group is matching with regex.
                    return;

                lags.forEach((topic, lagDTO) -> {
                    if (!topicPattern.matcher(topic).find()) // Check whether this topic is matching with regex.
                        return;

                    // If both are matching create alert for specifying a rule needed for them.
                    System.out.println("[ConsumerService#checkRule]: [ALERT] Rule definition needed because regex relation found for topic: " + topic + " and consumer group: " + consumerGroup + " with topic regex: " + regex.getTopic() + " and consumer group regex: " + regex.getConsumerGroup());
                    alertService.saveAlert(Alert.builder()
                            .cluster(cluster)
                            .regex(regex)
                            .consumerGroup(consumerGroup)
                            .topic(topic)
                            .message("[Cluster: " + cluster.getName()+ "] Rule definition needed because regex relation found for topic: " + topic + " and consumer group: " + consumerGroup + " with topic regex: " + regex.getTopic() + " and consumer group regex: " + regex.getConsumerGroup())
                            .status(AlertStatusEnum.NOT_FOUND)
                            .level(AlertLevelEnum.WARNING)
                            .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                            .processed(Boolean.FALSE)
                            .build());
                });
            });
        });
    }

    private void checkValidRegex(String regex) throws PatternSyntaxException {
        Pattern pattern = Pattern.compile(regex);
    }
}
