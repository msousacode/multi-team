package com.multiteam.core.enums;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;

public enum SpecialtyEnum {

  FONOAUDIOLOGIA("Fonoaudiologia"),
  MUSICOTERAPIA("Musicoterapia"),
  PSICOLOGIA("Psicologia"),
  PSICOMOTRICIDADE("Psicomotricidade"),
  TERAPIA_OCUPACIONAL("Terapia Ocupacional"),
  TERAPIA_FAMILIAR("Terapia Familiar"),
  PSICOPEDAGOGIA("Psicopedagogia"),
  NEUROPSICOLOGIA("Neuropsicológia"),
  PET_TERAPIA("Pet Terapia"),
  FISIOTERAPIA("Fisioterapia"),
  EDUCACAO_FISICA("Educação Física Especializada"),
  REFORCO_ESCOLAR("Reforço escolar"),
  ACOMPANHANTE_TERAPEUTICO("Acompanhante Terapêutico");

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
