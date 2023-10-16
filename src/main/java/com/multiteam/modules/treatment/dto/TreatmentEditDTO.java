package com.multiteam.modules.treatment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.multiteam.core.enums.SituationEnum;
import com.multiteam.core.enums.TreatmentEnum;
import com.multiteam.core.utils.Select;
import com.multiteam.modules.clinic.Clinic;
import com.multiteam.modules.clinic.dto.ClinicDTO;
import com.multiteam.modules.program.entity.Folder;
import com.multiteam.modules.program.entity.FolderProfessional;
import com.multiteam.modules.treatment.Treatment;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public record TreatmentEditDTO(
        UUID id,
        Set<ClinicDTO> clinics,
        Set<UUID> professionals,
        TreatmentEnum situation,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate initialDate,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate finalDate,
        String observation,
        List<Select> folderUnallocated,
        List<Select> folderAllocated
) {
    private TreatmentEditDTO(
            Treatment treatment,
            Set<Clinic> clinics,
            Set<UUID> professionals,
            List<Select> folderUnallocated,
            List<Select> folderAllocated
    ) {
        this(
                treatment.getId(),
                clinics.stream().map(ClinicDTO::new).collect(Collectors.toSet()),
                professionals,
                treatment.getSituation(),
                treatment.getInitialDate(),
                treatment.getFinalDate(),
                treatment.getDescription(),
                folderUnallocated,
                folderAllocated
        );
    }

    public static TreatmentEditDTO toDTO(Treatment treatment, Set<Clinic> clinics, Set<UUID> professionals, List<Folder> folders) {

        List<Select> unallocatedList = new ArrayList<>();
        List<Select> allocatedList = new ArrayList<>();

        for(Folder folder : folders) {
            folder.getFolderProfessional().forEach(fp -> {
                if(fp.getSituation().equals(SituationEnum.EM_COLETA)){
                    allocatedList.add(Select.toSelect(folder.getFolderName(), folder.getId().toString()));
                } else {
                    var unallocated = Select.toSelect(folder.getFolderName(), folder.getId().toString());
                    unallocatedList.add(unallocated);
                }
            });
        }

        //Remove duplicidade se houver
        var uniqueUnallocatedList = unallocatedList.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Select::getCode))),
                ArrayList::new));

        //Remove duplicidade se houver
        var uniqueAllocatedList = allocatedList.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Select::getCode))),
                        ArrayList::new));

        return new TreatmentEditDTO(treatment, clinics, professionals, uniqueUnallocatedList, uniqueAllocatedList);
    }
}
