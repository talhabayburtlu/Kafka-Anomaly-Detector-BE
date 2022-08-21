package com.classified.seller.commons.mapper;

import com.classified.seller.commons.dto.request.RegexRequest;
import com.classified.seller.commons.dto.response.RegexResponse;
import com.classified.seller.commons.entity.Regex;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = DestinationMapper.class)
public interface RegexMapper {

    RegexMapper MAPPER = Mappers.getMapper(RegexMapper.class);
    @Mapping(target = "id", constant = "0L")
    Regex toEntity(RegexRequest regexRequest);

    RegexResponse toResponse(Regex regex);

}
