package com.multiteam.modules.document;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

  Page<Document> findAllByPatient_IdAndTreatment_Id(UUID patientId, UUID treatmentId, Pageable pageable);
}
