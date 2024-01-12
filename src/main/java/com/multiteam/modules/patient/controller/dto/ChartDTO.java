package com.multiteam.modules.patient.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ChartDTO {
    private String title;
    private Set<String> labels;
    private List<DatasetDTO> datasets;
}
