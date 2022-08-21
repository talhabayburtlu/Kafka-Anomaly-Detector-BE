package com.classified.seller.commons.dto.inner;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class LagDTO {
    private Long lag;
    private Boolean visited;
}
