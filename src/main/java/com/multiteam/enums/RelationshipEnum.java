package com.multiteam.enums;

public enum RelationshipEnum {
    MAE("Mãe"),
    PAI("Pai"),
    TIA("Tia(o)"),
    AVO("Avó(ô)"),
    PROFESSOR("Professora(o)");

    public final String name;

    RelationshipEnum(String name) {
        this.name = name;
    }
}
