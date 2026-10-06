package rwanda.healthguard.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rwanda.healthguard.domain.HouseholdCategory;
import rwanda.healthguard.repository.HouseholdCategoryRepository;

@Service
public class HouseholdCategoryService {

    @Autowired
    private HouseholdCategoryRepository householdCategoryRepository;

    @Transactional
    public String saveHouseholdCategory(HouseholdCategory householdCategory) {

        householdCategory.setId(null);
        householdCategoryRepository.save(householdCategory);

        return "Household category saved successfully";
    }

    @Transactional
    public String updateHouseholdCategory(UUID id, HouseholdCategory householdCategory) {

        Optional<HouseholdCategory> existingHouseholdCategory = householdCategoryRepository.findById(id);

        if (existingHouseholdCategory.isEmpty()) {
            return "The household category with that id does not exist";
        }

        HouseholdCategory householdCategoryToUpdate = existingHouseholdCategory.get();
        householdCategoryToUpdate.setName(householdCategory.getName());

        householdCategoryRepository.save(householdCategoryToUpdate);

        return "Household category updated successfully";
    }

    @Transactional
    public String deleteHouseholdCategory(UUID id) {

        Optional<HouseholdCategory> existingHouseholdCategory = householdCategoryRepository.findById(id);

        if (existingHouseholdCategory.isEmpty()) {
            return "The household category with that id does not exist";
        }

        householdCategoryRepository.delete(existingHouseholdCategory.get());

        return "Household category deleted successfully";
    }

    public Optional<HouseholdCategory> getHouseholdCategoryById(UUID id) {

        return householdCategoryRepository.findById(id);
    }

    public List<HouseholdCategory> getAllHouseholdCategories() {

        return householdCategoryRepository.findAll();
    }
}
