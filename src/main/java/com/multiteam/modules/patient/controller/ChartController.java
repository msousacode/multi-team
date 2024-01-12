package com.multiteam.modules.patient.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequestMapping(path = "/v1/chart")
@RestController
@PreAuthorize("hasAnyRole('OWNER', 'ADMIN', 'PROFESSIONAL')")
public class ChartController {

    /**
     * private ChartService dashService;
     */

    /**
     * Aqui basimente terei uma api para consultar os dash,
     * sendo que eu preciso informar o patientId para carregar as informações do dash
     */
    @GetMapping("/{patientId}")
    public ResponseEntity<?> chart(@PathVariable("patientId") UUID patientId){

        /**
         * Uso o atributo da DashService injetada.
         */


        /**
         * Pensando um pouco no DTO de resposta eu preciso ter um formato parecido com esse:
         *
         * Vou ter que ter um atributo para identificar o programa, consequentemente esse nome da habilidade treinada será o título do chart*
         * Temos as seguintes opções:
         *     HABILIDADE_ATENCAO("Habilidades de Atenção", 1),
         *     HABILIDADE_IMITACAO("Habilidades de Imitação", 2),
         *     HABILIDADE_LINGUAGEM_RECPTIVA("Habilidades de Linguagem Receptiva", 3),
         *     HABILIDADE_LINGUAGEM_EXPRESSIVA("Habilidades de Linguagem Expressiva", 4),
         *     HABILIDADE_PRE_ACADEMICA("Habilidades Pré-Acadêmicas", 5);
         *
         *
         *
         */

        return ResponseEntity.ok().build();
    }
}
