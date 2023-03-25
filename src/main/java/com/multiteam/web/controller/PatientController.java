package com.multiteam.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(path = "/v1/users", produces = APPLICATION_JSON_VALUE)
@RestController
public class PatientController {
}
