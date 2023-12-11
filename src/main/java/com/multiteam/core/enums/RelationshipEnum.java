package com.multiteam.core.enums;

import lombok.Getter;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum RelationshipEnum {
    PAI(1, "Pai"),
    MAE(2, "Mãe"),
    TIA(3, "Tia(o)"),
    AVO(4, "Avó(ô)"),
    PROFESSOR(5, "Professor(a)");

    public final int code;
    public final String name;

    private static final Map<Integer, RelationshipEnum> lookup = new HashMap<>();

    RelationshipEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    static {
        for (RelationshipEnum type : RelationshipEnum.values()) {
            lookup.put(type.getCode(), type);
        }
    }

    public static RelationshipEnum getValue(Integer value) {
        Assert.notNull(value, "enum not can be null");
        return lookup.get(value);
    }
}
