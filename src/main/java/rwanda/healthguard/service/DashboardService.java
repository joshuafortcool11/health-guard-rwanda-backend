package rwanda.healthguard.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rwanda.healthguard.domain.EnrollmentStatus;
import rwanda.healthguard.repository.CitizenRepository;
import rwanda.healthguard.repository.CoverageVerificationRepository;
import rwanda.healthguard.repository.EnrollmentOfficerRepository;
import rwanda.healthguard.repository.EnrollmentRepository;
import rwanda.healthguard.repository.SectorRepository;

@Service
public class DashboardService {

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private EnrollmentOfficerRepository enrollmentOfficerRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CoverageVerificationRepository coverageVerificationRepository;

    // Bonus: one summary object, LinkedHashMap keeps the order
    public Map<String, Long> getDashboardSummary() {

        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("citizens", citizenRepository.count());
        summary.put("enrollmentOfficers", enrollmentOfficerRepository.count());
        summary.put("freeSectors", (long) sectorRepository.findFreeSectors().size());
        summary.put("enrollmentsToday", enrollmentRepository.countByEnrollmentDate(LocalDate.now()));
        summary.put("applied", enrollmentRepository.countByStatus(EnrollmentStatus.APPLIED));
        summary.put("verifications", coverageVerificationRepository.count());

        return summary;
    }
}
