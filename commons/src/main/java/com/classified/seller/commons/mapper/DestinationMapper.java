package com.classified.seller.commons.mapper;

import com.classified.seller.commons.dto.request.DestinationRequest;
import com.classified.seller.commons.dto.response.DestinationResponse;
import com.classified.seller.commons.entity.Destination;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = TargetMapper.class)
public interface DestinationMapper {

    DestinationMapper MAPPER = Mappers.getMapper(DestinationMapper.class);
    @Mapping(target = "id", constant = "0L")
    Destination toEntity(DestinationRequest destinationRequest);

    DestinationResponse toResponse(Destination destination);

}
