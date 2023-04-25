package com.multiteam.enums;

public enum RelationshipEnum {
    MAE("Mãe"),
    PAI("Pai"),
    TIA("Tia(o)"),
    AVO("Avó(ô)"),
    BABA("Babá"),
    PROFESSOR("Professor");

    public final String name;

    RelationshipEnum(String name) {
        this.name = name;
    }
}
