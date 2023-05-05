package com.multiteam.persistence.projection;

import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.enums.TreatmentEnum;

import java.time.LocalDate;
import java.util.UUID;

public interface TreatmentView {
    UUID getId();

    TreatmentEnum getTreatmentType();

    SituationEnum getSituation();

    String getPatientName();

    String getProfessionalName();

    String getProfessionalMiddleName();

    LocalDate getInitialDate();

    LocalDate getFinalDate();

    Boolean isActive();
}

