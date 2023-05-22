package com.multiteam.core.enums;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum ScheduleEnum {
    AGENDADO("blue"),
    CONFIRMADO("green"),
    AUSENCIA("red"),
    CANCELADO("orange"),
    REAGENDADO("yellow"),
    REALIZADO("black");

    private String color;
    private static final Map<String, ScheduleEnum> lookup = new HashMap<>();

    public String getColor() {
        return color;
    }

    ScheduleEnum(String color) {
        this.color = color;
    }

    static {
        for (ScheduleEnum s : ScheduleEnum.values()) {
            lookup.put(s.getColor(), s);
        }
    }

    public static ScheduleEnum get(String name) {
        var value = lookup.get(name);
        Assert.notNull(value, "schedule type not can be null");
        return value;
    }
}
