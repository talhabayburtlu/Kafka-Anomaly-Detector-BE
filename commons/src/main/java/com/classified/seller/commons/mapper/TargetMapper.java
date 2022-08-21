package com.classified.seller.commons.mapper;

import com.classified.seller.commons.dto.request.TargetRequest;
import com.classified.seller.commons.dto.response.TargetResponse;
import com.classified.seller.commons.entity.Target;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface TargetMapper {

    TargetMapper MAPPER = Mappers.getMapper(TargetMapper.class);
    @Mapping(target = "id", constant = "0L")
    Target toEntity(TargetRequest targetRequest);

    TargetResponse toReponse(Target target);

}
