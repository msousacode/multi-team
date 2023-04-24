package com.multiteam.persistence.projection;

import com.multiteam.enums.SituationEnum;
import com.multiteam.enums.TreatmentEnum;

import java.time.LocalDate;
import java.util.UUID;

public interface TreatmentView {
    UUID getId();

    TreatmentEnum getTreatmentType();

    SituationEnum getSituation();

    String getPatientName();

    String getPatientMiddleName();

    String getProfessionalName();

    String getProfessionalMiddleName();

    LocalDate getInitialDate();

    LocalDate getFinalDate();

    Boolean isActive();
}

