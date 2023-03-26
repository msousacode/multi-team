package com.multiteam.persistence.types;

public enum SexType {

    FEMININO("F", "Feminino"),
    MASCULINO("M", "Masculino"),
    NAO_DECLARADO("M", "Não declarado");

    private final String description;
    private final String acronym;

    SexType(String description, String acronym) {
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
