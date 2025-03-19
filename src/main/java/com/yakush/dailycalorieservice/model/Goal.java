package com.yakush.dailycalorieservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Goal {
    WEIGHT_LOSS("weight loss", 1),
    MAINTENANCE("maintenance", 2),
    MASS_GAIN("mass gain", 3);

    private final String text;
    private final int value;
}
