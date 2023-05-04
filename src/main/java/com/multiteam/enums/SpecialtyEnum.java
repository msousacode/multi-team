package com.multiteam.enums;

import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

public enum SpecialtyEnum {

    ATENDIMENTO_ABA("Atendimento ABA"),
    ATENDIMENTO_EM_GRUPO("Atendimento em grupo"),
    CONSULTORIA("Consultoria"),
    TERAPIA_FAMILIAR("Ter. Familiar Cognit. Comportamental"),
    TERAPIA_OCUPACIONAL("Terapia Ocupacional"),
    FONOAUDIOLOGIA("Fonoaudiologia"),
    PSICOPEDAGOGIA("Psicopedagogia"),
    PSICOMOTRICIDADE("Psicomotricidade"),
    PET_TERAPIA("Pet Terapia"),
    FISIOTERAPIA("Fisioterapia"),
    EDUCACAO_FISICA("Educação Física Especializada"),
    REFORCO_ESCOLAR("Reforço escolar"),
    ORIENTACAO_PROFISSIONAL("Orientação profissional"),
    JIU_JITSU("Jiu-Jitsu"),
    TREINAMENTO_EQUIPES("Treinamento de equipes");

    private final String name;
    private static final Map<String, SpecialtyEnum> lookup = new HashMap<>();

    SpecialtyEnum(String name) {
        this.name = name;
    }

    static {
        for (SpecialtyEnum s : SpecialtyEnum.values()) {
            lookup.put(s.getName(), s);
        }
    }

    public String getName() {
        return name;
    }

    public static SpecialtyEnum get(String name) {
        var value = lookup.get(name);
        Assert.notNull(value, "specialty not can be null");
        return value;
    }
}
