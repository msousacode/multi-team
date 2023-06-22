package com.multiteam.core.enums;

public enum DocumentType {

  IMAGE_JPG("jpg"),
  IMAGE_PNG("png"),
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

  private String type;

  DocumentType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
