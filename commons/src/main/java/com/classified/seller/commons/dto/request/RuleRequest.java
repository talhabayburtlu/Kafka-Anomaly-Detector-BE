package com.classified.seller.commons.dto.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class RuleRequest {
    private Long lowerBoundary;
    private Long upperBoundary;
    private String topic;
    private String consumerGroup;
}
