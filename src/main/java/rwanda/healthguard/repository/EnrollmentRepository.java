package rwanda.healthguard.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
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

    // C4: JPQL DISTINCT with path navigation, each citizen appears once
    @Query("SELECT DISTINCT e.citizen FROM Enrollment e WHERE e.enrollmentOfficer.id = :enrollmentOfficerId")
    List<Citizen> findCitizensOfEnrollmentOfficer(@Param("enrollmentOfficerId") UUID enrollmentOfficerId);
}
