package rwanda.healthguard.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rwanda.healthguard.domain.Citizen;
import rwanda.healthguard.domain.Enrollment;
import rwanda.healthguard.domain.EnrollmentOfficer;
import rwanda.healthguard.domain.EnrollmentStatus;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> findByStatusOrderByEnrollmentDateAsc(EnrollmentStatus status);

    List<Enrollment> findByEnrollmentDateBetweenOrderByEnrollmentDate(LocalDate start, LocalDate end);

    // D1: derived query, does the officer already process a non rejected enrollment on that date
    boolean existsByEnrollmentOfficerAndEnrollmentDateAndStatusNot(EnrollmentOfficer enrollmentOfficer,
            LocalDate enrollmentDate, EnrollmentStatus status);

    // Bonus dashboard: derived count queries
    long countByEnrollmentDate(LocalDate enrollmentDate);

    long countByStatus(EnrollmentStatus status);

    // C4: JPQL DISTINCT with path navigation, each citizen appears once
    @Query("SELECT DISTINCT e.citizen FROM Enrollment e WHERE e.enrollmentOfficer.id = :enrollmentOfficerId")
    List<Citizen> findCitizensOfEnrollmentOfficer(@Param("enrollmentOfficerId") UUID enrollmentOfficerId);

    // F1: JPQL GROUP BY, one row per status that has at least one enrollment
    @Query("SELECT e.status, COUNT(e) FROM Enrollment e GROUP BY e.status")
    List<Object[]> countEnrollmentsByStatus();

    // F2: JPQL bulk UPDATE in one query, COVERED enrollments are left untouched
    @Modifying
    @Query("UPDATE Enrollment e SET e.status = rwanda.healthguard.domain.EnrollmentStatus.REJECTED "
            + "WHERE e.enrollmentOfficer.id = :enrollmentOfficerId AND e.enrollmentDate = :date "
            + "AND e.status <> rwanda.healthguard.domain.EnrollmentStatus.COVERED")
    int cancelEnrollmentsOfDay(@Param("enrollmentOfficerId") UUID enrollmentOfficerId,
            @Param("date") LocalDate date);
}
