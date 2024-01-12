package com.multiteam.modules.patient.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChartDTO {
    private List<String> labels;
    private List<DatasetDTO> datasets;
}
