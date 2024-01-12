package com.multiteam.modules.patient.controller;

import com.multiteam.modules.patient.controller.dto.ChartDTO;
import com.multiteam.modules.patient.service.ChartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping(path = "/v1/charts")
@RestController
@PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
public class ChartController {

    private final ChartService dashService;

    public ChartController(ChartService dashService) {
        this.dashService = dashService;
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ChartDTO> chart(@PathVariable("patientId") UUID patientId) {
        return ResponseEntity.ok().body(dashService.findChart(patientId));
    }
}
