package com.multiteam.modules.program.controller;

import com.multiteam.core.utils.Select;
import com.multiteam.modules.program.dto.FolderListDTO;
import com.multiteam.modules.program.dto.FolderPostDTO;
import com.multiteam.modules.program.dto.FolderPutDTO;
import com.multiteam.modules.program.service.FolderService;
import com.multiteam.modules.program.service.ProgramService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
@RequestMapping(
        path = "/v1/folders",
        produces = APPLICATION_JSON_VALUE
)
@RestController
public class FolderController {

    private final FolderService folderService;
    private final ProgramService programService;

    public FolderController(FolderService folderService, ProgramService programService) {
        this.folderService = folderService;
        this.programService = programService;
    }


    @GetMapping("/{folderId}")
    public ResponseEntity<FolderListDTO> getFolderById(@PathVariable("folderId") final UUID folderId) {
        return folderService.getFolderById(folderId)
                .map(folderDTO -> ResponseEntity.ok(folderDTO)).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Select>> getFolderByPatientId(@PathVariable("patientId") final UUID patientId) {
        var folders = folderService.getFolderByPatientId(patientId).stream()
                .map(folder -> Select.toSelect(folder.getFolderName(), folder.getId().toString())).toList();
        return ResponseEntity.ok(folders);
    }

    @GetMapping
    public ResponseEntity<Page<FolderListDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "folderName") String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(folderService.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort)))
                        .map(FolderListDTO::new));
    }

    @PostMapping("/patient/{patientId}")
    public ResponseEntity<Boolean> createFolder(
            @PathVariable("patientId") final UUID patientId,
            @RequestBody FolderPostDTO folderPostDTO) {
        if (folderService.createFolder(patientId, folderPostDTO)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{folderId}/patient/{patientId}")
    public ResponseEntity<Void> updateFolder(
            @PathVariable("folderId") final UUID folderId,
            @PathVariable("patientId") final UUID patientId,
            @RequestBody FolderPutDTO folderPutDTO) {
        if (folderService.updateFolder(folderId, patientId, folderPutDTO)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
