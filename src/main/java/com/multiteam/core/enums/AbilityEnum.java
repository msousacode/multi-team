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

    private static final Map<Integer, AbilityEnum> lookup = new HashMap<>();

    AbilityEnum(String description, Integer value) {
        this.description = description;
        this.value = value;
    }

    static {
        for (AbilityEnum s : AbilityEnum.values()) {
            lookup.put(s.getValue(), s);
        }
    }

    public static AbilityEnum get(Integer value) {
        Assert.notNull(value, "ability not can be null");
        return lookup.get(value);
    }
}
