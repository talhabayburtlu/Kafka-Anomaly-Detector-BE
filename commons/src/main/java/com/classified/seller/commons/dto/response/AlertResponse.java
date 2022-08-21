package com.classified.seller.commons.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
public class AlertResponse {
    private Long lag;
    private String status;
    private String level;
    private String message;
    private String topic;
    private String consumerGroup;
    private Timestamp timestamp;
    private Boolean processed;
    private RuleResponse rule;
    private RegexResponse regex;
}
