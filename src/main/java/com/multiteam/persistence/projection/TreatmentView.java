package com.multiteam.persistence.projection;

import com.multiteam.persistence.types.SituationType;
import com.multiteam.persistence.types.TreatmentType;

import java.util.UUID;

public interface TreatmentView {
    UUID getId();

    TreatmentType getTreatmentType();

    SituationType getSituation();

    String getPatientName();

    String getPatientMiddleName();

    String getProfessionalName();

    String getProfessionalMiddleName();
}

