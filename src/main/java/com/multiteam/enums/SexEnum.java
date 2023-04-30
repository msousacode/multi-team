package com.multiteam.enums;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum SexEnum {

    FEMININO("Feminino"),
    MASCULINO("Masculino"),
    NAO_DECLARADO("Não declarado");

    private final String description;
    private static final Map<String, SexEnum> lookup = new HashMap<>();

    SexEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    static {
        for (SexEnum s : SexEnum.values()) {
            lookup.put(s.getDescription(), s);
        }
    }

    public static SexEnum get(String name) {
        var value = lookup.get(name);
        Assert.notNull(value, "specialty not can be null");
        return value;
    }
}
