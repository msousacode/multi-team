package com.multiteam.modules.document;

import com.multiteam.core.enums.DocumentType;
import com.multiteam.core.models.Auditable;
import com.multiteam.modules.patient.Patient;
import com.multiteam.modules.treatment.Treatment;
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

  @Column(name = "document_path")
  private String documentPath;

  @Column(name = "file_name")
  private String fileName;

  @Column(name = "size")
  private Double size;

  @Column(name = "file_metadata")
  private String fileMetadata;

  @Column(name = "document_type")
  @Enumerated(EnumType.STRING)
  private DocumentType documentType;

  @JoinColumn(name = "patient_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Patient patient;

  @JoinColumn(name = "treatment_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Treatment treatment;

  public Document() {
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getDocumentPath() {
    return documentPath;
  }

  public void setDocumentPath(String documentPath) {
    this.documentPath = documentPath;
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

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public Treatment getTreatment() {
    return treatment;
  }

  public void setTreatment(Treatment treatment) {
    this.treatment = treatment;
  }
}
