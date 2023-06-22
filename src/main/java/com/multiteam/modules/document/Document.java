package com.multiteam.modules.document;

import com.multiteam.core.enums.DocumentType;
import com.multiteam.core.models.Auditable;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "documents")
public class Document extends Auditable {

  @Id
  @GeneratedValue
  @Column(name = "document_id")
  private UUID id;

  @Column(name = "document_path")
  private String docmentpath;

  @Column(name = "file_name")
  private String fileName;

  @Column(name = "size")
  private Double size;

  @Column(name = "file_metadata")
  private String fileMetadata;

  @Column(name = "document_type")
  @Enumerated(EnumType.STRING)
  private DocumentType documentType;

  public Document() {
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getDocmentpath() {
    return docmentpath;
  }

  public void setDocmentpath(String docmentpath) {
    this.docmentpath = docmentpath;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public Double getSize() {
    return size;
  }

  public void setSize(Double size) {
    this.size = size;
  }

  public String getFileMetadata() {
    return fileMetadata;
  }

  public void setFileMetadata(String fileMetadata) {
    this.fileMetadata = fileMetadata;
  }

  public DocumentType getDocumentType() {
    return documentType;
  }

  public void setDocumentType(DocumentType documentType) {
    this.documentType = documentType;
  }
}
