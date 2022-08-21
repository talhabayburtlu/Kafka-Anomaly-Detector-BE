package com.classified.seller.commons.enumeration;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum AlertStatusEnum {
    LOWER("lower"),
    HIGHER("higher"),
    NOT_FOUND("not_found");

    AlertStatusEnum(final String name) {
        this.name = name;
    }

    private final String name;

    static final Map<String, AlertStatusEnum> names = Arrays.stream(AlertStatusEnum.values())
            .collect(Collectors.toMap(AlertStatusEnum::getName, Function.identity()));

    public static AlertStatusEnum fromName(final String name) {
        return names.get(name);
    }
}
