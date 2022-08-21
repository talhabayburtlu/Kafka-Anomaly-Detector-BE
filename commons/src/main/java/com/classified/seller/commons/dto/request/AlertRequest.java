package com.classified.seller.commons.dto.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AlertRequest {
    private Long lag;
    private String status;
    private String level;
    private String topic;
    private String consumerGroup;
    private Long clusterId;
    private Long ruleId;
    private Long regexId;
}
