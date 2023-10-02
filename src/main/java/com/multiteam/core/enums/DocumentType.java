package com.multiteam.core.enums;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;

public enum DocumentType {

  JPEG("jpeg"),
  JPG("jpg"),
  PNG("png"),
  ZIP("zip"),
  MP4("mp4"),
  MP3("mp3"),
  PDF("pdf"),
  XLS("xls"),
  XLSX("xlsx"),
  CSV("csv"),
  DOC("doc"),
  DOCX("docx"),
  HTML5("html"),
  DOT("dot"),
  DOTX("dotx");

  private final String type;
  private static final Map<String, DocumentType> lookup = new HashMap<>();

  DocumentType(String type) {
    this.type = type;
  }

  static {
    for (DocumentType s : DocumentType.values()) {
      lookup.put(s.name(), s);
    }
  }

  public static DocumentType getType(String type) {
    var value = lookup.get(type.toUpperCase());
    Assert.notNull(value, "schedule type not can be null");
    return value;
  }

  public String getType() {
    return type;
  }
}
