package rwanda.healthguard.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rwanda.healthguard.domain.Sector;

@Repository
public interface SectorRepository extends JpaRepository<Sector, UUID> {

    // D4: derived query
    Optional<Sector> findBySectorNumber(int sectorNumber);

    // C2: JPQL NOT EXISTS subquery, sectors nobody occupies, sorted by sectorNumber
    @Query("SELECT s FROM Sector s WHERE NOT EXISTS "
            + "(SELECT o FROM EnrollmentOfficer o WHERE o.sector = s) ORDER BY s.sectorNumber")
    List<Sector> findFreeSectors();
}
