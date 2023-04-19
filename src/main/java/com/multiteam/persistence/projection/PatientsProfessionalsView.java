package com.multiteam.persistence.projection;

import com.multiteam.enums.SituationType;

import java.util.UUID;

public interface PatientsProfessionalsView {

    UUID getPatientId();

    String getClinicName();

    String getPatientName();

    String getPatientMiddleName();

    String getProfessionalName();

    String getProfessionalMiddleName();

    SituationType getSituation();
}

