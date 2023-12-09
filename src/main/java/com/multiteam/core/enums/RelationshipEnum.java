package com.multiteam.core.enums;

import java.util.Arrays;

public enum RelationshipEnum {
    PAI(1, "Pai"),
    MAE(2, "Mãe"),
    TIA(3, "Tia(o)"),
    AVO(4, "Avó(ô)"),
    PROFESSOR(5, "Professor(a)");

    public final int code;
    public final String name;

    RelationshipEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static RelationshipEnum getValue(int code) {
        return Arrays.stream(RelationshipEnum.values()).findFirst().filter(i -> i.code == code).get();
    }
}
