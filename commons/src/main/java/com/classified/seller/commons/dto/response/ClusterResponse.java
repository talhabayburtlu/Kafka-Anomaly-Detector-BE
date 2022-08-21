package com.classified.seller.commons.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ClusterResponse {
    private Long id;
    private String name;
    private String description;
    private String env;
    private String address;
    private Map<String, String> config;
    private Boolean active;
}
