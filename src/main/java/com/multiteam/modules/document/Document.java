package com.multiteam.modules.document;

import com.multiteam.core.enums.DocumentType;
import com.multiteam.core.models.Auditable;
import com.multiteam.modules.patient.model.Patient;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "documents")
public class Document extends Auditable {

  @Id
  @GeneratedValue
  @Column(name = "document_id")
  private UUID id;

  @Column(name = "document_key")//Name saved in bucket S3
  private String documentKey;

  @Column(name = "file_name")
  private String fileName;

  @Column(name = "size")
  private Double size;

  @Column(name = "document_type")
  @Enumerated(EnumType.STRING)
  private DocumentType documentType;

  @JoinColumn(name = "patient_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Patient patient;

  public Document() {
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getDocumentKey() {
    return documentKey;
  }

  public void setDocumentKey(String documentKey) {
    this.documentKey = documentKey;
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

  public DocumentType getDocumentType() {
    return documentType;
  }

  public void setDocumentType(DocumentType documentType) {
    this.documentType = documentType;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }
}
