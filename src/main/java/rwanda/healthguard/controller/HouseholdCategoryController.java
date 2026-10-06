package rwanda.healthguard.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import rwanda.healthguard.domain.HouseholdCategory;
import rwanda.healthguard.service.HouseholdCategoryService;
import rwanda.healthguard.util.ApiResponses;

@RestController
public class HouseholdCategoryController {

    @Autowired
    private HouseholdCategoryService householdCategoryService;

    @PostMapping(value = "/api/household-categories/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveHouseholdCategory(@RequestBody HouseholdCategory householdCategory) {

        String returnedMessage = householdCategoryService.saveHouseholdCategory(householdCategory);

        if ("Household category saved successfully".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.CREATED);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/api/household-categories/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HouseholdCategory>> getAllHouseholdCategories() {

        return new ResponseEntity<>(householdCategoryService.getAllHouseholdCategories(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/household-categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getHouseholdCategoryById(@PathVariable UUID id) {

        Optional<HouseholdCategory> householdCategory = householdCategoryService.getHouseholdCategoryById(id);

        if (householdCategory.isEmpty()) {
            return ApiResponses.fromMessage("The household category with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(householdCategory.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/api/household-categories/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateHouseholdCategory(@PathVariable UUID id,
            @RequestBody HouseholdCategory householdCategory) {

        String returnedMessage = householdCategoryService.updateHouseholdCategory(id, householdCategory);

        if ("The household category with that id does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(householdCategoryService.getHouseholdCategoryById(id).get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/household-categories/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteHouseholdCategory(@PathVariable UUID id) {

        String returnedMessage = householdCategoryService.deleteHouseholdCategory(id);

        if ("The household category with that id does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.OK);
    }
}
