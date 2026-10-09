package rwanda.healthguard.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rwanda.healthguard.domain.Citizen;
import rwanda.healthguard.repository.CitizenRepository;
import rwanda.healthguard.repository.EnrollmentRepository;

@Service
public class CitizenService {

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Transactional
    public String saveCitizen(Citizen citizen) {

        citizen.setId(null);
        citizenRepository.save(citizen);

        return "Citizen saved successfully";
    }

    @Transactional
    public String updateCitizen(UUID id, Citizen citizen) {

        Optional<Citizen> existingCitizen = citizenRepository.findById(id);

        if (existingCitizen.isEmpty()) {
            return "The citizen with that id does not exist";
        }

        Citizen citizenToUpdate = existingCitizen.get();
        citizenToUpdate.setFirstName(citizen.getFirstName());
        citizenToUpdate.setLastName(citizen.getLastName());
        citizenToUpdate.setNationalId(citizen.getNationalId());
        citizenToUpdate.setPhone(citizen.getPhone());
        citizenToUpdate.setVillage(citizen.getVillage());

        citizenRepository.save(citizenToUpdate);

        return "Citizen updated successfully";
    }

    @Transactional
    public String deleteCitizen(UUID id) {

        Optional<Citizen> existingCitizen = citizenRepository.findById(id);

        if (existingCitizen.isEmpty()) {
            return "The citizen with that id does not exist";
        }

        citizenRepository.delete(existingCitizen.get());

        return "Citizen deleted successfully";
    }

    public Optional<Citizen> getCitizenById(UUID id) {

        return citizenRepository.findById(id);
    }

    public List<Citizen> getAllCitizens() {

        return citizenRepository.findAll();
    }

    // B1: derived query, village matched ignoring case, ordered by lastName A to Z
    public List<Citizen> getCitizensByVillage(String village) {

        return citizenRepository.findByVillageIgnoreCaseOrderByLastNameAsc(village);
    }

    // C4: JPQL, every citizen that has an enrollment with that enrollment officer, listed once
    public List<Citizen> getCitizensOfEnrollmentOfficer(UUID enrollmentOfficerId) {

        return enrollmentRepository.findCitizensOfEnrollmentOfficer(enrollmentOfficerId);
    }
}
