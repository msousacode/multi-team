package com.multiteam.modules.document;

import java.io.IOException;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/v1/documents")
@RestController
public class DocumentController {

  private final DocumentService documentService;

  public DocumentController(DocumentService documentService) {
    this.documentService = documentService;
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN') or hasAuthority('PERM_DOCUMENT_WRITE')")
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> uploadDocument(
      @RequestParam("tre") UUID treatmentId,
      @RequestParam("pat") UUID patientId,
      @RequestParam("file") MultipartFile file
  ) throws IOException {
    if (documentService.uploadDocumentS3(treatmentId, patientId, file)) {
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
}
