package com.classified.seller.commons.service;

import com.classified.seller.commons.entity.*;
import com.classified.seller.commons.enumeration.TargetTypeEnum;
import com.classified.seller.commons.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class AlertService {
    @Autowired
    private SlackService slackService;
    @Autowired
    private AlertRepository alertRepository;

    public Alert saveAlert(Alert alert) {
        if (alert.getId() == null)
            alert.setId(0L);
        return alertRepository.saveAndFlush(alert);
    }

    public List<Alert> getUnprocessedAlerts() {
        return alertRepository.getUnprocessedAlerts();
    }

    @Transactional
    public void processAlerts() {
        List<Alert> alerts = getUnprocessedAlerts();
        alerts.forEach(alert -> {
            Rule rule = alert.getRule();
            Regex regex = alert.getRegex();
            Destination destination = null;
            if (rule != null)
                destination = rule.getDestination();
            else if (regex != null)
                destination = regex.getDestination();

            destination.getTargets().forEach(target -> {
                processAlert(alert, target);
                alert.setProcessed(Boolean.TRUE);
                saveAlert(alert);
            });

        });
    }

    private void processAlert(Alert alert, Target target) {
        if (target.getType() == TargetTypeEnum.slack) {
            slackService.sendMessageToSlack(alert, target.getValue());
        }
    }

}
