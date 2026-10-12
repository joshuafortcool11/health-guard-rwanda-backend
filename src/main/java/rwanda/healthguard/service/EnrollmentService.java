package rwanda.healthguard.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rwanda.healthguard.domain.Citizen;
import rwanda.healthguard.domain.Enrollment;
import rwanda.healthguard.domain.EnrollmentOfficer;
import rwanda.healthguard.domain.EnrollmentStatus;
import rwanda.healthguard.repository.CitizenRepository;
import rwanda.healthguard.repository.EnrollmentOfficerRepository;
import rwanda.healthguard.repository.EnrollmentRepository;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentOfficerRepository enrollmentOfficerRepository;

    @Autowired
    private CitizenRepository citizenRepository;

    // D1: safe saving, date not in the past, officer free on that date, always starts as APPLIED
    @Transactional
    public String saveEnrollment(Enrollment enrollment) {

        if (enrollment.getEnrollmentDate() == null) {
            return "Enrollment date is required";
        }

        if (enrollment.getEnrollmentDate().isBefore(LocalDate.now())) {
            return "Enrollment date cannot be in the past";
        }

        if (enrollment.getEnrollmentOfficer() == null || enrollment.getEnrollmentOfficer().getId() == null) {
            return "The enrollment officer with that id does not exist";
        }

        Optional<EnrollmentOfficer> existingEnrollmentOfficer = enrollmentOfficerRepository
                .findById(enrollment.getEnrollmentOfficer().getId());

        if (existingEnrollmentOfficer.isEmpty()) {
            return "The enrollment officer with that id does not exist";
        }

        if (enrollment.getCitizen() == null || enrollment.getCitizen().getId() == null) {
            return "The citizen with that id does not exist";
        }

        Optional<Citizen> existingCitizen = citizenRepository.findById(enrollment.getCitizen().getId());

        if (existingCitizen.isEmpty()) {
            return "The citizen with that id does not exist";
        }

        if (enrollmentRepository.existsByEnrollmentOfficerAndEnrollmentDateAndStatusNot(
                existingEnrollmentOfficer.get(), enrollment.getEnrollmentDate(), EnrollmentStatus.REJECTED)) {
            return "Enrollment officer already processes an enrollment on that date";
        }

        enrollment.setId(null);
        enrollment.setEnrollmentOfficer(existingEnrollmentOfficer.get());
        enrollment.setCitizen(existingCitizen.get());
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

    // B2: derived query, earliest enrollmentDate first
    public List<Enrollment> getEnrollmentsByStatus(EnrollmentStatus status) {

        return enrollmentRepository.findByStatusOrderByEnrollmentDateAsc(status);
    }

    // B3: derived query, both dates inclusive, ordered by enrollmentDate
    public List<Enrollment> getEnrollmentsBetween(LocalDate start, LocalDate end) {

        return enrollmentRepository.findByEnrollmentDateBetweenOrderByEnrollmentDate(start, end);
    }

    // D2: the status may only move along the allowed arrows, COVERED and REJECTED are final
    @Transactional
    public String updateEnrollmentStatus(UUID id, EnrollmentStatus newStatus) {

        Optional<Enrollment> existingEnrollment = enrollmentRepository.findById(id);

        if (existingEnrollment.isEmpty()) {
            return "The enrollment with that id does not exist";
        }

        Enrollment enrollment = existingEnrollment.get();
        EnrollmentStatus currentStatus = enrollment.getStatus();

        boolean moveIsAllowed =
                (currentStatus == EnrollmentStatus.APPLIED
                        && (newStatus == EnrollmentStatus.PAYMENT_PENDING || newStatus == EnrollmentStatus.REJECTED))
                || (currentStatus == EnrollmentStatus.PAYMENT_PENDING
                        && (newStatus == EnrollmentStatus.COVERED || newStatus == EnrollmentStatus.REJECTED));

        if (!moveIsAllowed) {
            return "Cannot change status from " + currentStatus + " to " + newStatus;
        }

        enrollment.setStatus(newStatus);
        enrollmentRepository.save(enrollment);

        return "Enrollment status updated successfully";
    }
}
