package com.classified.seller.commons.service;

import com.classified.seller.commons.entity.Alert;
import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SlackService {

    public void sendMessageToSlack(Alert alert, String webhookURL) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("*[" + alert.getTimestamp().toLocalDateTime().toString() + "]")
                .append("[" + alert.getLevel().getName() + "]:* ")
                .append("\n")
                .append(alert.getMessage())
                .append("\n");
        processMessage(stringBuilder.toString(), webhookURL);
    }

    private void processMessage(String message, String webhookURL) {
        Payload payload = Payload.builder().text(message).build();
        try {
            Slack.getInstance().send(webhookURL, payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
