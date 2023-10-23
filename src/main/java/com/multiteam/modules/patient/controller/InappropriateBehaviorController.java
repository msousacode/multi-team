package com.multiteam.modules.patient.controller;

import com.multiteam.modules.patient.model.InappropriateBehavior;
import com.multiteam.modules.patient.repository.InappropriateBehaviorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        path = "/v1/inappropriate-behaviors",
        produces = APPLICATION_JSON_VALUE
)
@RestController
@PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
public class InappropriateBehaviorController {

    private final InappropriateBehaviorRepository behaviorRepository;

    public InappropriateBehaviorController(InappropriateBehaviorRepository behaviorRepository) {
        this.behaviorRepository = behaviorRepository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody InappropriateBehavior behavior) {
        return ResponseEntity.ok(behaviorRepository.save(behavior));
    }

    @GetMapping("/{behaviorId}")
    public ResponseEntity<InappropriateBehavior> get(@PathVariable UUID behaviorId) {
        return behaviorRepository.findById(behaviorId)
                .map(reinforcer -> ResponseEntity.ok(reinforcer))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<InappropriateBehavior>> get() {
        return ResponseEntity.ok(behaviorRepository.findAll());
    }

    @DeleteMapping("/{behaviorIdId}")
    public ResponseEntity<Void> delete(@PathVariable("behaviorId") UUID behaviorId) {
        behaviorRepository.deleteById(behaviorId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
