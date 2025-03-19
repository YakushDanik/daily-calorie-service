package com.yakush.dailycalorieservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Sex {
    MALE("male", 1),
    FEMALE("female", 2);

    private final String text;
    private final int value;
}
