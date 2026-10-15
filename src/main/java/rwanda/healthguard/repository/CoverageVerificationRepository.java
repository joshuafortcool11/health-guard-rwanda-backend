package rwanda.healthguard.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rwanda.healthguard.domain.CoverageVerification;
import rwanda.healthguard.domain.Enrollment;

@Repository
public interface CoverageVerificationRepository extends JpaRepository<CoverageVerification, UUID> {

    // E3: derived query, can an enrollment no longer be deleted
    boolean existsByEnrollment(Enrollment enrollment);

    // E2: JPQL path navigation, newest verifiedDate first
    @Query("SELECT c FROM CoverageVerification c WHERE c.enrollment.citizen.nationalId = :nationalId "
            + "ORDER BY c.verifiedDate DESC")
    List<CoverageVerification> findCoverageVerificationsOfCitizen(@Param("nationalId") String nationalId);
}
