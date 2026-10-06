package rwanda.healthguard.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rwanda.healthguard.domain.Enrollment;
import rwanda.healthguard.domain.EnrollmentStatus;
import rwanda.healthguard.repository.EnrollmentRepository;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Transactional
    public String saveEnrollment(Enrollment enrollment) {

        enrollment.setId(null);
        enrollment.setStatus(EnrollmentStatus.APPLIED);

        enrollmentRepository.save(enrollment);

        return "Enrollment saved successfully";
    }

    @Transactional
    public String updateEnrollment(UUID id, Enrollment enrollment) {

        Optional<Enrollment> existingEnrollment = enrollmentRepository.findById(id);

        if (existingEnrollment.isEmpty()) {
            return "The enrollment with that id does not exist";
        }

        Enrollment enrollmentToUpdate = existingEnrollment.get();
        enrollmentToUpdate.setEnrollmentDate(enrollment.getEnrollmentDate());
        enrollmentToUpdate.setCoverageYear(enrollment.getCoverageYear());
        enrollmentToUpdate.setPremium(enrollment.getPremium());

        if (enrollment.getCitizen() != null) {
            enrollmentToUpdate.setCitizen(enrollment.getCitizen());
        }

        if (enrollment.getEnrollmentOfficer() != null) {
            enrollmentToUpdate.setEnrollmentOfficer(enrollment.getEnrollmentOfficer());
        }

        enrollmentRepository.save(enrollmentToUpdate);

        return "Enrollment updated successfully";
    }

    @Transactional
    public String deleteEnrollment(UUID id) {

        Optional<Enrollment> existingEnrollment = enrollmentRepository.findById(id);

        if (existingEnrollment.isEmpty()) {
            return "The enrollment with that id does not exist";
        }

        enrollmentRepository.delete(existingEnrollment.get());

        return "Enrollment deleted successfully";
    }

    public Optional<Enrollment> getEnrollmentById(UUID id) {

        return enrollmentRepository.findById(id);
    }

    public List<Enrollment> getAllEnrollments() {

        return enrollmentRepository.findAll();
    }
}
