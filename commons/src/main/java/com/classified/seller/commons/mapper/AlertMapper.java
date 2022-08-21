package com.classified.seller.commons.mapper;

import com.classified.seller.commons.dto.request.AlertRequest;
import com.classified.seller.commons.dto.response.AlertResponse;
import com.classified.seller.commons.entity.Alert;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper()
public interface AlertMapper {

    AlertMapper MAPPER = Mappers.getMapper(AlertMapper.class);
    @Mapping(target = "id", constant = "0L")
    Alert toEntity(AlertRequest alertRequest);

    AlertResponse toReponse(Alert alert);

}
