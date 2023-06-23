package com.multiteam.modules.document;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.multiteam.core.enums.ApplicationError;
import com.multiteam.core.enums.DocumentType;
import com.multiteam.core.exception.BadRequestException;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.treatment.TreatmentService;
import java.io.IOException;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

  @Value("${aws.s3.bucket-name}")
  private String s3BucketName;

  @Autowired
  private AmazonS3 amazonS3;

  private final DocumentRepository documentRepository;
  private final TreatmentService treatmentService;
  private final PatientService patientService;

  public DocumentService(DocumentRepository documentRepository, TreatmentService treatmentService,
      PatientService patientService) {
    this.documentRepository = documentRepository;
    this.treatmentService = treatmentService;
    this.patientService = patientService;
  }

  public Boolean uploadDocumentS3(final UUID treatmentId, final UUID patientId,
      final MultipartFile partFile)
      throws IOException {

    saveDocument(treatmentId, patientId, partFile);

    ObjectMetadata data = new ObjectMetadata();
    data.setContentType(partFile.getContentType());
    data.setContentLength(partFile.getSize());

    PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName,
        partFile.getOriginalFilename(), partFile.getInputStream(), data);

    PutObjectResult objectResult = amazonS3.putObject(putObjectRequest);

    return Boolean.TRUE;
  }

  @Transactional
  private void saveDocument(UUID treatmentId, UUID patientId, MultipartFile partFile) {
    Document document = new Document();

    var patient = patientService.findOneById(patientId);
    var treatment = treatmentService.findTreatment(treatmentId);

    if (patient.isEmpty() & treatment.isEmpty()) {
      throw new BadRequestException(ApplicationError.PATIENT_OR_TREATMENT_NOT_CAN_BE_EMPTY.getMessage());
    }

    document.setPatient(patient.get());
    document.setTreatment(treatment.get());

    document.setFileName(partFile.getOriginalFilename());
    document.setSize(Double.valueOf(partFile.getSize()));
    document.setDocumentType(DocumentType.DOC);
    document.setDocumentPath("");

    documentRepository.save(document);
  }
}
