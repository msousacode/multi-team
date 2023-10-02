package com.multiteam.modules.program.controller;

import com.multiteam.modules.program.dto.FolderDTO;
import com.multiteam.modules.program.dto.FolderListDTO;
import com.multiteam.modules.program.dto.ProgramDTO;
import com.multiteam.modules.program.dto.ProgramListDTO;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.mapper.ProgramMapper;
import com.multiteam.modules.program.repository.FolderRepository;
import com.multiteam.modules.program.service.FolderService;
import com.multiteam.modules.program.service.ProgramService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<FolderDTO> getFolderById(@PathVariable("folderId") UUID folderId) {
        return folderService.getFolderById(folderId)
                .map(folderDTO -> ResponseEntity.ok(folderDTO)).orElse(ResponseEntity.notFound().build());
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
    public ResponseEntity<Boolean> createFolder(@PathVariable("patientId") final UUID patientId, @RequestBody FolderDTO folderDTO) {
        if (folderService.createFolder(patientId, folderDTO)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{folderId}")
    public ResponseEntity<Void> updateFolder(@PathVariable("folderId") final UUID folderId, @RequestBody FolderDTO folderDTO) {
        if (folderService.updateFolder(folderId, folderDTO)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
