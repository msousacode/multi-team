package com.multiteam.enums;

public enum RelationshipType {
    MAE("Mãe"),
    PAI("Pai"),
    TIA("Tia(o)"),
    AVO("Avó(ô)"),
    PROFESSOR("Professora(o)");

    public final String name;

    RelationshipType(String name) {
        this.name = name;
    }
}
