package com.multiteam.modules.patient.controller;

import com.multiteam.modules.patient.model.EvolutonParameter;
import com.multiteam.modules.patient.model.Reinforcer;
import com.multiteam.modules.patient.repository.EvolutionParameterRepository;
import com.multiteam.modules.patient.repository.ReinforcerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(
        path = "/v1/parameters",
        produces = APPLICATION_JSON_VALUE
)
@RestController
@PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
public class EvolutionParameterController {

    private final EvolutionParameterRepository evolutionParameterRepository;

    public EvolutionParameterController(EvolutionParameterRepository evolutionParameterRepository) {
        this.evolutionParameterRepository = evolutionParameterRepository;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody EvolutonParameter parameter) {
        return ResponseEntity.ok(evolutionParameterRepository.save(parameter));
    }

    @GetMapping("/{parameterId}")
    public ResponseEntity<EvolutonParameter> get(@PathVariable UUID parameterId) {
        return evolutionParameterRepository.findById(parameterId)
                .map(parameter -> ResponseEntity.ok(parameter))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<EvolutonParameter>> get() {
        return ResponseEntity.ok(evolutionParameterRepository.findAll());
    }

    @DeleteMapping("/{parameterId}")
    public ResponseEntity<Void> delete(@PathVariable("parameterId") UUID parameterId) {
        evolutionParameterRepository.deleteById(parameterId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
