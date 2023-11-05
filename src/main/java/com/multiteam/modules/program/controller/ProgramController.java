package com.multiteam.modules.program.controller;

import com.multiteam.modules.program.dto.ProgramDTO;
import com.multiteam.modules.program.dto.ProgramListDTO;
import com.multiteam.modules.program.dto.ProgramPostDTO;
import com.multiteam.modules.program.mapper.ProgramPostMapper;
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
        path = "/v1/programs",
        produces = APPLICATION_JSON_VALUE
)
@RestController
public class ProgramController {

    private ProgramService programService;

    public ProgramController(final ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping
    public ResponseEntity<Void> createProgram(@RequestBody ProgramPostDTO programDTO) {
        if (programService.createProgram(programDTO)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping
    public ResponseEntity<Void> updateClinic(@RequestBody ProgramDTO programDTO) {
        if (programService.updateProgram(programDTO)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<ProgramListDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(value = "sort", defaultValue = "programName") String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(programService.getAll(PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(direction), sort)))
                        .map(ProgramListDTO::new));
    }

    @GetMapping("/{programId}")
    public ResponseEntity<ProgramDTO> getById(@PathVariable("programId") final UUID programId) {
        return programService.getById(programId)
                .map(program -> ResponseEntity.status(HttpStatus.OK).body(ProgramPostMapper.MAPPER.toDTO(program)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{programId}")
    public ResponseEntity<Void> delete(@PathVariable("programId") UUID programId) {
        programService.delete(programId);
        return ResponseEntity.ok().build();
    }
}
