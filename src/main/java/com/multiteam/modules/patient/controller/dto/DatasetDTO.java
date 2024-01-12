package com.multiteam.modules.patient.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DatasetDTO {
    private String label;
    private List<Integer> data;
    private boolean fill = false;
    private String backgroundColor;
    private String borderColor;
    private double tension = 0.4;
}
