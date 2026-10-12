package rwanda.healthguard.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rwanda.healthguard.domain.EnrollmentOfficer;
import rwanda.healthguard.domain.Sector;

@Repository
public interface EnrollmentOfficerRepository extends JpaRepository<EnrollmentOfficer, UUID> {

    // D4: derived query, is that sector occupied by an enrollment officer
    boolean existsBySector(Sector sector);

    // C1: JPQL join on the many-to-many collection, name compared ignoring case
    @Query("SELECT o FROM EnrollmentOfficer o JOIN o.householdCategories c WHERE LOWER(c.name) = LOWER(:name)")
    List<EnrollmentOfficer> findOfficersByHouseholdCategoryName(@Param("name") String name);
}
