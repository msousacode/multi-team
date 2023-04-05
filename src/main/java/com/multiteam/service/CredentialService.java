package com.multiteam.service;

import com.multiteam.persistence.entity.Credential;
import com.multiteam.persistence.repository.CredentialRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CredentialService {

    private final CredentialRepository credentialRepository;

    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Transactional
    public Credential createCredential(Credential credential) {
        return credentialRepository.save(credential);
    }

    public boolean checkIfCredentialExists(String email) {
        return credentialRepository.findByUsername(email).isPresent();
    }
}
