package com.multiteam.core.enums;

import lombok.Getter;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TargetSituationEnum {

    NAO_INICIADO("Não iniciado", 1),
    EM_TREINO("Em treino", 2),
    MANUTENCAO("Manutenção", 3),
    PAUSADO("Pausado", 4),
    FINALIZADO("Finalizado", 5);

    private final String description;
    private final Integer value;

    private static final Map<Integer, TargetSituationEnum> lookup = new HashMap<>();

    TargetSituationEnum(String description, Integer value) {
        this.description = description;
        this.value = value;
    }

    static {
        for (TargetSituationEnum s : TargetSituationEnum.values()) {
            lookup.put(s.getValue(), s);
        }
    }

    public static TargetSituationEnum get(Integer value) {
        Assert.notNull(value, "target situation not can be null");
        return lookup.get(value);
    }
}
