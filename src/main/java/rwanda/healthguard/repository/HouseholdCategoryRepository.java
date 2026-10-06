package rwanda.healthguard.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rwanda.healthguard.domain.HouseholdCategory;

@Repository
public interface HouseholdCategoryRepository extends JpaRepository<HouseholdCategory, UUID> {
}
