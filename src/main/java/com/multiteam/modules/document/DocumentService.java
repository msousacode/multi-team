package com.multiteam.modules.document;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
      final MultipartFile partFile) {

    String filenameExtension = StringUtils.getFilenameExtension(partFile.getOriginalFilename());
    String key = UUID.randomUUID().toString() + "." + filenameExtension;

    saveDocument(treatmentId, patientId, partFile, key, filenameExtension);

    ObjectMetadata data = new ObjectMetadata();
    data.setContentType(partFile.getContentType());
    data.setContentLength(partFile.getSize());

    try {
      PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, key,
          partFile.getInputStream(), data);
      amazonS3.putObject(putObjectRequest);
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          ApplicationError.S3_EXCEPTION.getMessage());
    }
    return Boolean.TRUE;
  }

  @Transactional
  private void saveDocument(UUID treatmentId, UUID patientId, MultipartFile partFile, String key, String filenameExtension) {
    Document document = new Document();

    var patient = patientService.findOneById(patientId);
    var treatment = treatmentService.findTreatment(treatmentId);

    if (patient.isEmpty() & treatment.isEmpty()) {
      throw new BadRequestException(
          ApplicationError.PATIENT_OR_TREATMENT_NOT_CAN_BE_EMPTY.getMessage());
    }

    document.setPatient(patient.get());
    document.setTreatment(treatment.get());

    document.setFileName(partFile.getOriginalFilename());
    document.setDocumentKey(key);
    document.setSize(Double.valueOf(partFile.getSize()));
    document.setDocumentType(DocumentType.getType(filenameExtension));

    documentRepository.save(document);
  }
}
