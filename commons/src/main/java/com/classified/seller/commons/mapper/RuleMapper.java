package com.classified.seller.commons.mapper;

import com.classified.seller.commons.dto.request.RuleRequest;
import com.classified.seller.commons.dto.response.RuleResponse;
import com.classified.seller.commons.entity.Rule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = DestinationMapper.class)
public interface RuleMapper {

    RuleMapper MAPPER = Mappers.getMapper(RuleMapper.class);
    @Mapping(target = "id", constant = "0L")
    Rule toEntity(RuleRequest ruleRequest);

    RuleResponse toResponse(Rule rule);

}
