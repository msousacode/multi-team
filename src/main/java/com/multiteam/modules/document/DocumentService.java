package com.multiteam.modules.document;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.multiteam.core.enums.ApplicationError;
import com.multiteam.core.enums.DocumentType;
import com.multiteam.core.exception.BadRequestException;
import com.multiteam.modules.document.dto.DocumentSearch;
import com.multiteam.modules.patient.PatientService;
import com.multiteam.modules.treatment.TreatmentService;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  public Boolean uploadDocumentS3(final UUID patientId, final MultipartFile partFile) {

    String filenameExtension = StringUtils.getFilenameExtension(partFile.getOriginalFilename());
    String key = UUID.randomUUID().toString() + "." + filenameExtension;

    saveDocument(patientId, partFile, key, filenameExtension);

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
  private void saveDocument(UUID patientId, MultipartFile partFile, String key,
      String filenameExtension) {
    Document document = new Document();

    var patient = patientService.getPatientById(patientId);

    if (patient.isEmpty()) {
      throw new BadRequestException(
          ApplicationError.PATIENT_NOT_FOUND.getMessage());
    }

    document.setPatient(patient.get());

    document.setFileName(partFile.getOriginalFilename());
    document.setDocumentKey(key);
    document.setSize(Double.valueOf(partFile.getSize()));
    document.setDocumentType(DocumentType.getType(filenameExtension));

    documentRepository.save(document);
  }

  public Page<Document> getDocumentsSearch(DocumentSearch documentSearch, Pageable pageable) {
    return documentRepository.findAllByPatient_Id(documentSearch.patientId(), pageable);
  }

  public String generatePresignedUrl(UUID documentId, String nameKey) {

    var expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += 100 * 60 * 60;
    expiration.setTime(expTimeMillis);

    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(s3BucketName, nameKey)
            .withMethod(HttpMethod.GET)
            .withExpiration(expiration);

    URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);

    return url.toString();
  }
}
