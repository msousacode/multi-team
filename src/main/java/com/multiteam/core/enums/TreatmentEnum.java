package com.multiteam.core.enums;

public enum TreatmentEnum {

    TERAPIA_OCUPACIONAL("Terapia Ocupacional", "Terapeuta(o) Ocupacional"),
    FONOAUDIOLOGIA("Fonoaudiologia", "Fonoaudióloga(o)"),
    PSICOPEDAGOGIA("psicopedagogia", "Psicopedagoga(o)");

    private final String name;
    private final String specialty;

    TreatmentEnum(String name, String specialty) {
        this.name = name;
        this.specialty = specialty;
    }
}
