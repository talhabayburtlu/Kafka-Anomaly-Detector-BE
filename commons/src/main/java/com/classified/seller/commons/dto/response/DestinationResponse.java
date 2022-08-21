package com.classified.seller.commons.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DestinationResponse {
    private Long id;
    private String name;
    private List<TargetResponse> targets;
}
