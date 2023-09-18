package com.multiteam.modules.program;

import com.multiteam.modules.clinic.dto.ClinicDTO;
import com.multiteam.modules.program.dto.ProgramDTO;
import com.multiteam.modules.program.dto.ProgramListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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

    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
    @PostMapping
    public ResponseEntity<Void> createProgram(@RequestBody ProgramDTO programDTO) {
        if (programService.createProgram(programDTO)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ProgramListDTO>> getAll() {
        var programs = programService.getAll().stream().map(ProgramListDTO::new).toList();
        return ResponseEntity.ok(programs);
    }
}
