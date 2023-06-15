package com.multiteam.core.enums;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;

public enum ScheduleEnum {
  AGENDADO("#00BFFF"),
  CONFIRMADO("#0000CD"),
  AUSENCIA("#DC143C"),
  CANCELADO("#708090"),
  REAGENDADO("#FFD700"),
  REALIZADO("#008000");

  private String color;
  private static final Map<String, ScheduleEnum> lookup = new HashMap<>();

  public String getColor() {
    return color;
  }

  ScheduleEnum(String color) {
    this.color = color;
  }

  static {
    for (ScheduleEnum s : ScheduleEnum.values()) {
      lookup.put(s.name(), s);
    }
  }

  public static String getColorByStatus(String name) {
    var value = lookup.get(name);
    Assert.notNull(value, "schedule type not can be null");
    return value.getColor();
  }
}
