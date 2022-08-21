package com.classified.seller.commons.mapper;

import com.classified.seller.commons.dto.response.LagResponse;
import com.classified.seller.commons.entity.Lag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface LagMapper {

    LagMapper MAPPER = Mappers.getMapper(LagMapper.class);

   LagResponse toResponse(Lag lag);

}
