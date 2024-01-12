package com.multiteam.modules.patient.controller;

import com.multiteam.modules.patient.model.Reinforcer;
import com.multiteam.modules.patient.repository.ReinforcerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Deprecated(forRemoval = true)
@RequestMapping(
        path = "/v1/reinforcers",
        produces = APPLICATION_JSON_VALUE
)
@RestController
@PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
public class ReinforcersController {

    private final ReinforcerRepository reinforcerRepository;

    public ReinforcersController(ReinforcerRepository reinforcerRepository) {
        this.reinforcerRepository = reinforcerRepository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Reinforcer reinforcer) {
        return ResponseEntity.ok(reinforcerRepository.save(reinforcer));
    }

    @GetMapping("/{reinforcerId}")
    public ResponseEntity<Reinforcer> get(@PathVariable UUID reinforcerId) {
        return reinforcerRepository.findById(reinforcerId)
                .map(reinforcer -> ResponseEntity.ok(reinforcer))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Reinforcer>> get() {
        return ResponseEntity.ok(reinforcerRepository.findAll());
    }

    @DeleteMapping("/{reinforcerId}")
    public ResponseEntity<Void> delete(@PathVariable("reinforcerId") UUID reinforcerId) {
        reinforcerRepository.deleteById(reinforcerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
