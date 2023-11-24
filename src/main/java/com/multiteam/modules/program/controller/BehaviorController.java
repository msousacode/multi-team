package com.multiteam.modules.program.controller;

import com.multiteam.modules.program.dto.BehaviorDTO;
import com.multiteam.modules.program.mapper.BehaviorCollectMapper;
import com.multiteam.modules.program.mapper.BehaviorMapper;
import com.multiteam.modules.program.service.BehaviorCollectService;
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
    private BehaviorCollectService behaviorCollectService;

    public BehaviorController(
            BehaviorService behaviorService,
            BehaviorCollectService behaviorCollectService) {
        this.behaviorService = behaviorService;
        this.behaviorCollectService = behaviorCollectService;
    }

    @PostMapping("/program/{programId}")
    public ResponseEntity<Void> createBehavior(
            @PathVariable("programId" ) UUID programId,
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

    @DeleteMapping("/{behaviorId}")
    public ResponseEntity<Void> delete(@PathVariable("behaviorId") UUID behaviorId) {
        behaviorService.deleteBehavior(behaviorId);
        return ResponseEntity.ok().build();
    }

    /*@GetMapping("/patient/{patientId}/collects")
    public ResponseEntity<List<BehaviorDTO>> getCollectsByPatientId(@PathVariable("patientId") UUID patientId) {
        return ResponseEntity.ok(behaviorService.getCollectsByPatientId(patientId).stream().map(i -> BehaviorCollectMapper.MAPPER.toDTO(i)).toList());
    }*/
}
