package com.classified.seller.commons.enumeration;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum TargetTypeEnum {
    email("email"),
    slack("slack"),
    sms("sms");

    TargetTypeEnum(final String name) {
        this.name = name;
    }

    private final String name;

    static final Map<String, TargetTypeEnum> names = Arrays.stream(TargetTypeEnum.values())
            .collect(Collectors.toMap(TargetTypeEnum::getName, Function.identity()));

    public static TargetTypeEnum fromName(final String name) {
        return names.get(name);
    }
}
