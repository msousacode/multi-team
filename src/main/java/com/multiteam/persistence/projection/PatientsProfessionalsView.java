package com.multiteam.persistence.projection;

import com.multiteam.enums.SituationEnum;

import java.util.UUID;

public interface PatientsProfessionalsView {

    UUID getPatientId();

    String getClinicName();

    String getPatientName();

    String getProfessionalName();

    SituationEnum getSituation();
}

