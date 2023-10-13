package com.multiteam.core.enums;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum SituationEnum {
    ANDAMENTO("Andamento"),
    NAO_ALOCADA("Não alocada"),
    EM_COLETA("Em coleta"),
    CANCELADO("Cancelada"),
    PAUSADO("Pausada"),
    CONCLUIDO("Concluída"),
    INATIVO("Inativa");


    private final String description;
    private static final Map<String, SituationEnum> lookup = new HashMap<>();

    SituationEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    static {
        for (SituationEnum s : SituationEnum.values()) {
            lookup.put(s.getDescription(), s);
        }
    }

    public static SituationEnum get(String name) {
        var value = lookup.get(name);
        Assert.notNull(value, "situation type not can be null");
        return value;
    }
}
