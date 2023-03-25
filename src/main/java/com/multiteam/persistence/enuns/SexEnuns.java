package com.multiteam.persistence.enuns;

public enum SexEnuns {

    FAMALE("F", "Feminino"),
    MALE("M", "Masculino");

    private final String description;
    private final String acronym;

    SexEnuns(String description, String acronym) {
        this.description = description;
        this.acronym = acronym;
    }

    public String getDescription() {
        return description;
    }

    public String getAcronym() {
        return acronym;
    }
}
