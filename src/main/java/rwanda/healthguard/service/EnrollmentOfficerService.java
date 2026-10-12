package rwanda.healthguard.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rwanda.healthguard.domain.EnrollmentOfficer;
import rwanda.healthguard.domain.HouseholdCategory;
import rwanda.healthguard.domain.Sector;
import rwanda.healthguard.repository.EnrollmentOfficerRepository;
import rwanda.healthguard.repository.HouseholdCategoryRepository;
import rwanda.healthguard.repository.SectorRepository;

@Service
public class EnrollmentOfficerService {

    @Autowired
    private EnrollmentOfficerRepository enrollmentOfficerRepository;

    @Autowired
    private HouseholdCategoryRepository householdCategoryRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Transactional
    public String saveEnrollmentOfficer(EnrollmentOfficer enrollmentOfficer) {

        enrollmentOfficer.setId(null);
        enrollmentOfficerRepository.save(enrollmentOfficer);

        return "Enrollment officer saved successfully";
    }

    @Transactional
    public String updateEnrollmentOfficer(UUID id, EnrollmentOfficer enrollmentOfficer) {

        Optional<EnrollmentOfficer> existingEnrollmentOfficer = enrollmentOfficerRepository.findById(id);

        if (existingEnrollmentOfficer.isEmpty()) {
            return "The enrollment officer with that id does not exist";
        }

        EnrollmentOfficer enrollmentOfficerToUpdate = existingEnrollmentOfficer.get();
        enrollmentOfficerToUpdate.setFirstName(enrollmentOfficer.getFirstName());
        enrollmentOfficerToUpdate.setLastName(enrollmentOfficer.getLastName());
        enrollmentOfficerToUpdate.setEmail(enrollmentOfficer.getEmail());

        if (enrollmentOfficer.getSector() != null) {
            enrollmentOfficerToUpdate.setSector(enrollmentOfficer.getSector());
        }

        enrollmentOfficerRepository.save(enrollmentOfficerToUpdate);

        return "Enrollment officer updated successfully";
    }

    @Transactional
    public String deleteEnrollmentOfficer(UUID id) {

        Optional<EnrollmentOfficer> existingEnrollmentOfficer = enrollmentOfficerRepository.findById(id);

        if (existingEnrollmentOfficer.isEmpty()) {
            return "The enrollment officer with that id does not exist";
        }

        enrollmentOfficerRepository.delete(existingEnrollmentOfficer.get());

        return "Enrollment officer deleted successfully";
    }

    public Optional<EnrollmentOfficer> getEnrollmentOfficerById(UUID id) {

        return enrollmentOfficerRepository.findById(id);
    }

    public List<EnrollmentOfficer> getAllEnrollmentOfficers() {

        return enrollmentOfficerRepository.findAll();
    }

    // C1: JPQL, enrollment officers who hold a household category, ignoring letter case
    public List<EnrollmentOfficer> getEnrollmentOfficersByHouseholdCategory(String name) {

        return enrollmentOfficerRepository.findOfficersByHouseholdCategoryName(name);
    }

    // D3: add one household category to an enrollment officer, keep the ones already there
    @Transactional
    public String addHouseholdCategoryToEnrollmentOfficer(UUID enrollmentOfficerId, UUID householdCategoryId) {

        Optional<EnrollmentOfficer> existingEnrollmentOfficer = enrollmentOfficerRepository
                .findById(enrollmentOfficerId);

        if (existingEnrollmentOfficer.isEmpty()) {
            return "The enrollment officer with that id does not exist";
        }

        Optional<HouseholdCategory> existingHouseholdCategory = householdCategoryRepository
                .findById(householdCategoryId);

        if (existingHouseholdCategory.isEmpty()) {
            return "That household category does not exist";
        }

        EnrollmentOfficer enrollmentOfficer = existingEnrollmentOfficer.get();

        if (enrollmentOfficer.getHouseholdCategories() == null) {
            enrollmentOfficer.setHouseholdCategories(new ArrayList<>());
        }

        if (enrollmentOfficer.getHouseholdCategories().contains(existingHouseholdCategory.get())) {
            return "The enrollment officer already has that household category";
        }

        enrollmentOfficer.getHouseholdCategories().add(existingHouseholdCategory.get());
        enrollmentOfficerRepository.save(enrollmentOfficer);

        return "Household category added to the enrollment officer";
    }

    // D4: move an enrollment officer to another sector, the old one becomes free
    @Transactional
    public String changeEnrollmentOfficerSector(UUID enrollmentOfficerId, int sectorNumber) {

        Optional<EnrollmentOfficer> existingEnrollmentOfficer = enrollmentOfficerRepository
                .findById(enrollmentOfficerId);

        if (existingEnrollmentOfficer.isEmpty()) {
            return "The enrollment officer with that id does not exist";
        }

        Optional<Sector> existingSector = sectorRepository.findBySectorNumber(sectorNumber);

        if (existingSector.isEmpty()) {
            return "The sector with that sectorNumber does not exist";
        }

        EnrollmentOfficer enrollmentOfficer = existingEnrollmentOfficer.get();
        Sector newSector = existingSector.get();

        boolean officerAlreadyHasThisSector = enrollmentOfficer.getSector() != null
                && enrollmentOfficer.getSector().getId().equals(newSector.getId());

        if (enrollmentOfficerRepository.existsBySector(newSector) && !officerAlreadyHasThisSector) {
            return "That sector is taken by another enrollment officer";
        }

        enrollmentOfficer.setSector(newSector);
        enrollmentOfficerRepository.save(enrollmentOfficer);

        return "Sector changed successfully";
    }
}
