package com.multiteam.modules.program.controller;

import com.multiteam.modules.program.dto.BehaviorDTO;
import com.multiteam.modules.program.mapper.BehaviorMapper;
import com.multiteam.modules.program.service.BehaviorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'SUPERVISOR')")
@RequestMapping(
        path = "/v1/behaviors",
        produces = APPLICATION_JSON_VALUE
)
@RestController
public class BehaviorController {

    private BehaviorService behaviorService;

    public BehaviorController(final BehaviorService behaviorService) {
        this.behaviorService = behaviorService;
    }

    @PostMapping("/program/{programId}")
    public ResponseEntity<Void> createBehavior(
            @PathVariable("programId" ) final UUID programId,
            @RequestBody BehaviorDTO behaviorDTO) {
        if (behaviorService.createBehavior(programId, behaviorDTO)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{behaviorId}")
    public ResponseEntity<Void> updateBehavior(
            @PathVariable("behaviorId" ) final UUID behaviorId,
            @RequestBody BehaviorDTO behaviorDTO) {
        if (behaviorService.updateBehavior(behaviorId, behaviorDTO)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<BehaviorDTO>> getAll(@PathVariable("programId") final UUID programId) {
        return ResponseEntity.ok(behaviorService.getAll(programId).stream().map(i -> BehaviorMapper.MAPPER.toDTO(i)).toList());
    }

    @PostMapping("/program/{programId}/delete")
    public ResponseEntity<Void> createBehavior(
            @PathVariable("programId") final UUID programId,
            @RequestBody List<BehaviorDTO> behaviorDTO) {
        if (behaviorService.deleteBehavior(programId, behaviorDTO)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
