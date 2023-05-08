package com.multiteam.core.enums;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum TreatmentEnum {

    TERAPIA_OCUPACIONAL("Terapia Ocupacional"),
    FONOAUDIOLOGIA("Fonoaudiologia"),
    PSICOPEDAGOGIA("Psicopedagogia");

    private final String description;
    private static final Map<String, TreatmentEnum> lookup = new HashMap<>();

    TreatmentEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    static {
        for (TreatmentEnum s : TreatmentEnum.values()) {
            lookup.put(s.getDescription(), s);
        }
    }

    public static TreatmentEnum get(String name) {
        var value = lookup.get(name);
        Assert.notNull(value, "treatment type not can be null");
        return value;
    }
}
