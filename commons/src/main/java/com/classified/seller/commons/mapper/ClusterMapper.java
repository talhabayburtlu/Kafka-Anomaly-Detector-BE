package com.classified.seller.commons.mapper;

import com.classified.seller.commons.dto.request.ClusterRequest;
import com.classified.seller.commons.dto.response.ClusterResponse;
import com.classified.seller.commons.entity.Cluster;
import com.classified.seller.commons.util.ObjectSerializer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;
import java.util.Map;

@Mapper()
public interface ClusterMapper {

    ClusterMapper MAPPER = Mappers.getMapper(ClusterMapper.class);
    @Mapping(target = "id", constant = "0L")
    @Mapping(source = "config" , target = "config", qualifiedByName = "stringifyConfig")
    Cluster toEntity(ClusterRequest clusterRequest);

    @Mapping(source = "config" , target = "config", qualifiedByName = "parseConfig")
    ClusterResponse toResponse(Cluster cluster);

    @Named("stringifyConfig")
    default String stringifyConfig(Map<String, String> config) {
        return ObjectSerializer.serialize((Serializable) config);
    }

    @Named("parseConfig")
    default Map<String,String> parseConfig(String config) {
        if (config == null)
            return null;
        return (Map<String, String>) ObjectSerializer.deserialize(config);
    }

}
