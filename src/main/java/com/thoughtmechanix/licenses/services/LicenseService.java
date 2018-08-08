package com.thoughtmechanix.licenses.services;

import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.repositories.LicensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LicenseService {

    @Autowired
    private LicensesRepository licensesRepository;

    public License getLicense(String licenseId) {
        Optional<License> license = this.licensesRepository.findById(licenseId);
        if (license.isPresent()) {
            return license.get();
        }
        return new License();
    }

    public void saveLicense(License license) {

    }

    public void updateLicense(License license) {

    }

    public void deleteLicense(License license) {

    }

}
