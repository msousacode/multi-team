package com.multiteam.modules.program.controller;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.utils.Select;
import com.multiteam.modules.program.dto.FolderGridDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
@RequestMapping(path = "/v1/folders", produces = APPLICATION_JSON_VALUE)
@RestController
public class FolderController {

    private final FolderService folderService;
    private final ProgramService programService;

    public FolderController(FolderService folderService, ProgramService programService) {
        this.folderService = folderService;
        this.programService = programService;
    }


    @GetMapping("/{folderId}")
    public ResponseEntity<FolderListDTO> getFolderById(@PathVariable("folderId") UUID folderId) {
        return folderService.getFolderById(folderId)
                .map(folderDTO -> ResponseEntity.ok(folderDTO)).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<FolderGridDTO> getFoldersByPatient(@PathVariable("patientId") UUID patientId) {

        var folders = folderService.getFoldersByPatient(patientId);

        List<Select> allocated = new ArrayList<>();
        List<Select> unallocated = new ArrayList<>();

        folders.forEach(folder -> folder.getFolderProfessional().forEach(folderProfessional -> {
            if (SituationEnum.NAO_ALOCADA.equals(folderProfessional.getSituation())) {
                unallocated.add(Select.toSelect(folder.getFolderName(), folder.getId().toString()));
            } else if (SituationEnum.EM_COLETA.equals(folderProfessional.getSituation())) {
                allocated.add(Select.toSelect(folder.getFolderName(), folder.getId().toString()));
            }
        }));

        return ResponseEntity.ok(new FolderGridDTO(allocated, unallocated));
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
            @PathVariable("folderId") UUID folderId,
            @PathVariable("patientId") UUID patientId,
            @RequestBody FolderPutDTO folderPutDTO) {
        if (folderService.updateFolder(folderId, patientId, folderPutDTO)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{folderId}/program/{programId}")
    public ResponseEntity<Void> deleteProgramFolder(
            @PathVariable("programId") UUID programId,
            @PathVariable("folderId") UUID folderId) {
        folderService.deleteProgramFolder(programId, folderId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
