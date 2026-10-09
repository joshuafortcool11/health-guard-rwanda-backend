package rwanda.healthguard.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import rwanda.healthguard.domain.HouseholdCategory;

@Repository
public interface HouseholdCategoryRepository extends JpaRepository<HouseholdCategory, UUID> {

    // C3: JPQL IS EMPTY on the officers collection
    @Query("SELECT h FROM HouseholdCategory h WHERE h.officers IS EMPTY")
    List<HouseholdCategory> findUnusedHouseholdCategories();
}
