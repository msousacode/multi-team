package com.multiteam.modules.professional;

import com.multiteam.core.context.TenantContext;
import com.multiteam.core.enums.SpecialtyEnum;
import com.multiteam.core.enums.UserEnum;
import com.multiteam.core.models.EmailVO;
import com.multiteam.core.service.EmailService;
import com.multiteam.modules.clinic.ClinicService;
import com.multiteam.modules.professional.dto.ProfessionalDTO;
import com.multiteam.modules.professional.dto.ProfessionalUseTreatmentRequest;
import com.multiteam.modules.professional.dto.ProfessionalUseTreatmentView;
import com.multiteam.modules.treatment.TreatmentService;
import com.multiteam.modules.user.User;
import com.multiteam.modules.user.UserService;
import com.sendgrid.helpers.mail.objects.Content;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.multiteam.core.enums.AuthProviderEnum.local;

@Service
public class ProfessionalService {

    private final Logger logger = LogManager.getLogger(ProfessionalService.class);

    private final ProfessionalRepository professionalRepository;
    private final ClinicService clinicService;
    private final TreatmentService treatmentService;
    private final EmailService emailService;
    private final UserService userService;
    private final TenantContext tenantContext;

    public ProfessionalService(
            ProfessionalRepository professionalRepository,
            ClinicService clinicService,
            TreatmentService treatmentService,
            EmailService emailService,
            UserService userService,
            TenantContext tenantContext) {
        this.professionalRepository = professionalRepository;
        this.clinicService = clinicService;
        this.treatmentService = treatmentService;
        this.emailService = emailService;
        this.userService = userService;
        this.tenantContext = tenantContext;
    }

    @Transactional
    public Boolean createProfessional(ProfessionalDTO professionalDTO) {

        Set<UUID> clinicsIds = getUuids(professionalDTO);
        Assert.isTrue(!clinicsIds.isEmpty(), "clinic list cannot be empty");
        var clinics = clinicService.getClinics(clinicsIds);

        if (clinics.isEmpty()) {
            logger.debug("check if clinic exists. clinicId: {}", professionalDTO.clinicId());
            logger.error("clinic cannot be null. clinicId: {}", professionalDTO.clinicId());
            return Boolean.FALSE;
        }

        var user = userService.createUser(professionalDTO.name(), professionalDTO.email(), local, UserEnum.PROFISSIONAL);

        var builder = new Professional.Builder(
                null,
                professionalDTO.name(),
                SpecialtyEnum.get(professionalDTO.specialty()),
                professionalDTO.cellPhone(),
                professionalDTO.email(),
                true,
                clinics,
                user)
                .build();

        professionalRepository.save(builder);

        String subject = getSubject();
        Content content = getContent(user);

        var email = EmailVO.buildEmail(user, subject, content);

        if (emailService.sendEmailNewUser(email)) {
            logger.warn("user was created , but an error occurred when sending the first login email: {}", professionalDTO.email());
        }

        return Boolean.TRUE;
    }

    public Page<ProfessionalDTO> getAllProfessionals(final UUID clinicId, Pageable pageable) {
        return professionalRepository.findAllProfessionalsByClinicId(clinicId, pageable).map(ProfessionalDTO::fromProfessionalDTO);
    }

    public Optional<Professional> getProfessionalById(final UUID professionalId) {
        return professionalRepository.findById(professionalId);
    }

    public Optional<ProfessionalDTO> getProfessional(UUID professionalId) {
        var professional = professionalRepository.findOneById(professionalId);

        if (professional.isEmpty()) {
            return Optional.empty();
        }

        return professional.map(ProfessionalDTO::fromProfessionalDTO);
    }

    @Transactional
    public Boolean inactiveProfessional(UUID professionalId) {

        var professional = professionalRepository.findById(professionalId);

        if (professional.isEmpty()) {
            logger.error("verify if exists professional with id: {}", professionalId);
            return Boolean.FALSE;
        }

        professional.get().getProfessionals().forEach(t -> treatmentService.inactiveTreatment(t.getTreatment().getId()));
        logger.info("treatments associated with professional {} has been inactivated", professionalId);

        professionalRepository.professionalInactive(professional.get().getId(), tenantContext.getTenantId());
        logger.info("professional has been inactivated, professionalId {}", professionalId);

        return Boolean.TRUE;
    }

    @Transactional
    public Boolean updateProfessional(ProfessionalDTO professionalDTO) {

        var professionalResult = professionalRepository.findById(professionalDTO.id());

        Set<UUID> clinicsIds = getUuids(professionalDTO);
        Assert.isTrue(!clinicsIds.isEmpty(), "clinic list cannot be empty");
        var clinics = clinicService.getClinics(clinicsIds);

        if (professionalResult.isEmpty()) {
            logger.debug("check if professional exists. professionalId: {}", professionalDTO.id());
            logger.error("professional cannot be null. professionalId: {}", professionalDTO.id());
            return Boolean.FALSE;
        }

        var builder = new Professional.Builder(
                professionalResult.get().getId(),
                professionalDTO.name(),
                SpecialtyEnum.get(professionalDTO.specialty()),
                professionalDTO.cellPhone(),
                professionalDTO.email(),
                true,
                clinics,
                professionalResult.get().getUser())
                .build();

        professionalRepository.save(builder);

        logger.info("updated professional: {}", new Professional().toString());

        return Boolean.TRUE;
    }

    private static Set<UUID> getUuids(ProfessionalDTO professionalDTO) {
        Set<UUID> clinicsIds = new HashSet<>();
        professionalDTO.clinicId().forEach(elem -> clinicsIds.add(UUID.fromString(elem)));
        return clinicsIds;
    }

    public List<ProfessionalUseTreatmentView> getProfessionalsUseTreatment(ProfessionalUseTreatmentRequest professionalUseTreatmentDTO) {
        List<UUID> ids = new ArrayList<>();
        professionalUseTreatmentDTO.clinics().forEach(clinicDTO -> ids.add(clinicDTO.id()));
        return professionalRepository.findAllByClinicsId(ids);
    }

    public List<Professional> getAllProfessionalsByClinics(List<UUID> professionals) {
        return professionalRepository.getAllProfessionalsByClinics(professionals);
    }

    private static String getSubject() {
        return "Team Clinic | O Seu Acesso Chegou!";
    }

    private static Content getContent(User user) {
        return new Content("text/plain",
                """
                             Olá! Você foi convidado a fazer parte do Portal Team Clinic. 
                             
                             Para logar no Portal, utilize as credenciais:
                             E-mail: %s
                             Senha: %s
                        """.formatted(user.getEmail(), user.getProvisionalPassword()));
    }

    public Optional<Professional> getProfessionalByUserId(final UUID userId) {
        return professionalRepository.findProfessionalByUser_Id(userId);
    }

    public List<Professional> getProfessionals() {
        return professionalRepository.findAll();
    }

    public List<Professional> getProfessionalsInBatch(List<UUID> professionalsId) {
        return professionalRepository.findAllById(professionalsId);
    }

    public List<Professional> getProfessionalsFoldersById(UUID folderId) {
        return professionalRepository.findAllByFoldersId(folderId);
    }
}
