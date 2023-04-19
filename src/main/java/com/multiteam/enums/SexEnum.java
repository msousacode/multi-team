package com.multiteam.enums;

public enum SexEnum {

    FEMININO("F", "Feminino"),
    MASCULINO("M", "Masculino"),
    NAO_DECLARADO("M", "Não declarado");

    private final String description;
    private final String acronym;

    SexEnum(String description, String acronym) {
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
