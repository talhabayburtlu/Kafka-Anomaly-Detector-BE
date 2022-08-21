package com.classified.seller.commons.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegexResponse {
    private Long id;
    private String topic;
    private String consumerGroup;
    private DestinationResponse destination;
}
