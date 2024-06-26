package com.multiteam.modules.document;

import com.multiteam.modules.document.dto.DocumentDTO;
import com.multiteam.modules.document.dto.DocumentSearch;
import com.multiteam.modules.document.dto.UrlPreSignedDTO;
import java.io.IOException;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
  @PostMapping(value = "/upload/patient/{patientId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> uploadDocument(
      @PathVariable("patientId") final UUID patientId,
      @RequestParam("file") MultipartFile file
  ) throws IOException {
    if (documentService.uploadDocumentS3(patientId, file)) {
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
  @PostMapping("/search")
  public ResponseEntity<Page<DocumentDTO>> getDocumentsSearch(
      @RequestBody final DocumentSearch documentSearch,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "100") int size,
      @RequestParam(value = "sort", defaultValue = "createdDate") String sort,
      @RequestParam(value = "direction", defaultValue = "DESC") String direction) {

    var document = documentService.getDocumentsSearch(documentSearch,
        PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort)));
    return ResponseEntity.ok(document.map(DocumentDTO::new));
  }

  @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
  @GetMapping("/download/{documentId}/key/{nameKey}")
  public ResponseEntity<UrlPreSignedDTO> generatePresignedUrl(
      @PathVariable("documentId") final UUID documentId,
      @PathVariable("nameKey") final String nameKey) {
    return ResponseEntity.ok(new UrlPreSignedDTO(documentService.generatePresignedUrl(documentId, nameKey)));
  }
}
