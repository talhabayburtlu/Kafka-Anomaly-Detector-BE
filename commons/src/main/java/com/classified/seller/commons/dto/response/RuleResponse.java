package com.classified.seller.commons.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RuleResponse {
    private Long id;
    private String topic;
    private String consumerGroup;
    private Long lowerBoundary;
    private Long upperBoundary;
    private DestinationResponse destination;
}
