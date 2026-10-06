package rwanda.healthguard.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rwanda.healthguard.domain.EnrollmentOfficer;
import rwanda.healthguard.repository.EnrollmentOfficerRepository;

@Service
public class EnrollmentOfficerService {

    @Autowired
    private EnrollmentOfficerRepository enrollmentOfficerRepository;

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
}
