package com.multiteam.modules.document.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.modules.document.Document;
import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentDTO(
    UUID id,
    String nameFile,
    String nameKey,
    String type,
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDateTime createdDate
) {

  public DocumentDTO(Document document) {
    this(
        document.getId(),
        document.getFileName(),
        document.getDocumentKey(),
        document.getDocumentType().getType(),
        document.getCreatedDate()
    );
  }
}
