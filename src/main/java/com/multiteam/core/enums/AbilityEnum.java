package com.multiteam.core.enums;

import lombok.Getter;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum AbilityEnum {

    HABILIDADE_ATENCAO("Habilidades de Atenção", 1),
    HABILIDADE_IMITACAO("Habilidades de Imitação", 2),
    HABILIDADE_LINGUAGEM_RECPTIVA("Habilidades de Linguagem Receptiva", 3),
    HABILIDADE_LINGUAGEM_EXPRESSIVA("Habilidades de Linguagem Expressiva", 4),
    HABILIDADE_PRE_ACADEMICA("Habilidades Pré-Acadêmicas", 5);

    private final String description;
    private final Integer value;

    private static final Map<String, AbilityEnum> lookup = new HashMap<>();

    AbilityEnum(String description, Integer value) {
        this.description = description;
        this.value = value;
    }

    static {
        for (AbilityEnum s : AbilityEnum.values()) {
            lookup.put(s.getDescription(), s);
        }
    }

    public static AbilityEnum get(String name) {
        var value = lookup.get(name);
        Assert.notNull(value, "specialty not can be null");
        return value;
    }
}
