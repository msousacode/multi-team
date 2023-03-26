package com.multiteam.persistence.types;

public enum SpecialtyType {

    TERAPIA_OCUPACIONAL("fonoaudióloga"),
    FONOAU("Fonoaudiologia"),
    PSICOPEDAGOGIA("psicopedagogia");

    private final String name;

    SpecialtyType(String name) {
        this.name = name;
    }
}
