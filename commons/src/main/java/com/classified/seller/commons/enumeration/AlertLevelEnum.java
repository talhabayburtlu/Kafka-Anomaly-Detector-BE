package com.classified.seller.commons.enumeration;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum AlertLevelEnum {
    ERROR("error"),
    WARNING("warning");

    AlertLevelEnum(final String name) {
        this.name = name;
    }

    private final String name;

    static final Map<String, AlertLevelEnum> names = Arrays.stream(AlertLevelEnum.values())
            .collect(Collectors.toMap(AlertLevelEnum::getName, Function.identity()));

    public static AlertLevelEnum fromName(final String name) {
        return names.get(name);
    }
}
