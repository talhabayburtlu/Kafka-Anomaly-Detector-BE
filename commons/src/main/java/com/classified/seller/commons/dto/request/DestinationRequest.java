package com.classified.seller.commons.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class DestinationRequest {
    private String name;
    private List<Long> targetIds;
}
