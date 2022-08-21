package com.classified.seller.commons.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AllLagsResponse {
    private Long lowerBoundary;
    private Long upperBoundary;
    private List<LagResponse> lags;
}
