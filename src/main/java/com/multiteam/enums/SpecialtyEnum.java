package com.multiteam.enums;

public enum SpecialtyEnum {

    TERAPIA_OCUPACIONAL("fonoaudióloga"),
    FONOAUDIOLOGIA("Fonoaudiologia"),
    PSICOPEDAGOGIA("psicopedagogia");

    private final String name;

    SpecialtyEnum(String name) {
        this.name = name;
    }
}
