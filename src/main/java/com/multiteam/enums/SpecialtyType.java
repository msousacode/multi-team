package com.multiteam.enums;

public enum SpecialtyType {

    TERAPIA_OCUPACIONAL("fonoaudióloga"),
    FONOAUDIOLOGIA("Fonoaudiologia"),
    PSICOPEDAGOGIA("psicopedagogia");

    private final String name;

    SpecialtyType(String name) {
        this.name = name;
    }
}
