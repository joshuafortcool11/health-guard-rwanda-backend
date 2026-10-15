package rwanda.healthguard.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rwanda.healthguard.domain.CoverageVerification;
import rwanda.healthguard.domain.Enrollment;
import rwanda.healthguard.domain.EnrollmentStatus;
import rwanda.healthguard.repository.CoverageVerificationRepository;
import rwanda.healthguard.repository.EnrollmentRepository;

@Service
public class CoverageVerificationService {

    @Autowired
    private CoverageVerificationRepository coverageVerificationRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // E1: a coverage verification can only be written for a COVERED enrollment
    @Transactional
    public String saveCoverageVerification(UUID enrollmentId, CoverageVerification coverageVerification) {

        Optional<Enrollment> existingEnrollment = enrollmentRepository.findById(enrollmentId);

        if (existingEnrollment.isEmpty()) {
            return "The enrollment with that id does not exist";
        }

        Enrollment enrollment = existingEnrollment.get();

        if (enrollment.getStatus() != EnrollmentStatus.COVERED) {
            return "Coverage verifications are only for COVERED enrollments";
        }

        coverageVerification.setId(null);
        coverageVerification.setVerifiedDate(LocalDate.now());
        coverageVerification.setEnrollment(enrollment);

        coverageVerificationRepository.save(coverageVerification);

        return "Coverage verification saved successfully";
    }

    // E2: every coverage verification of that citizen, newest verifiedDate first
    public List<CoverageVerification> getCoverageVerificationsOfCitizen(String nationalId) {

        return coverageVerificationRepository.findCoverageVerificationsOfCitizen(nationalId);
    }

    // E3: delete a coverage verification
    @Transactional
    public String deleteCoverageVerification(UUID id) {

        Optional<CoverageVerification> existingCoverageVerification = coverageVerificationRepository.findById(id);

        if (existingCoverageVerification.isEmpty()) {
            return "The coverage verification with that id does not exist";
        }

        coverageVerificationRepository.delete(existingCoverageVerification.get());

        return "Coverage verification deleted successfully";
    }
}
