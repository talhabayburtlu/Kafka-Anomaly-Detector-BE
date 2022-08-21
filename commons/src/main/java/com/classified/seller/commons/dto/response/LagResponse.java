package com.classified.seller.commons.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LagResponse {
    private Long id;
    private Timestamp timestamp;
    private Long value;
    private String topic;
    private String consumerGroup;
}
