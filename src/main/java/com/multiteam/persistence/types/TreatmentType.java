package com.multiteam.persistence.types;

public enum TreatmentType {

    TERAPIA_OCUPACIONAL("Terapia Ocupacional", "Terapeuta(o) Ocupacional"),
    FONOAUDIOLOGIA("Fonoaudiologia", "Fonoaudióloga(o)"),
    PSICOPEDAGOGIA("psicopedagogia", "Psicopedagoga(o)");

    private final String name;
    private final String specialty;

    TreatmentType(String name, String specialty) {
        this.name = name;
        this.specialty = specialty;
    }
}
