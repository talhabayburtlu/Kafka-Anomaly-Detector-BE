package com.classified.seller.commons.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class ClusterRequest {
    private String address;
    private String name;
    private String description;
    private String env;
    private Map<String, String> config;
    private Boolean active;
}
