package com.multiteam.core.enums;

import lombok.Getter;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum ProtocolEnum {

    PROTOCOLO_ABC("Protocolo ABC", 1),
    PROTOCOLO_OCORRENCIA_RESPOSTA("Protocolo de Ocorrência de Respostas", 2);

    private final String description;
    private final Integer value;

    private static final Map<Integer, ProtocolEnum> lookup = new HashMap<>();

    ProtocolEnum(String description, Integer value) {
        this.description = description;
        this.value = value;
    }

    static {
        for (ProtocolEnum s : ProtocolEnum.values()) {
            lookup.put(s.getValue(), s);
        }
    }

    public static ProtocolEnum get(Integer value) {
        Assert.notNull(value, "ability not can be null");
        return lookup.get(value);
    }
}
