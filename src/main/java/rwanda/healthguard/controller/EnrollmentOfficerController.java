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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rwanda.healthguard.domain.EnrollmentOfficer;
import rwanda.healthguard.service.EnrollmentOfficerService;
import rwanda.healthguard.util.ApiResponses;

@RestController
public class EnrollmentOfficerController {

    @Autowired
    private EnrollmentOfficerService enrollmentOfficerService;

    @PostMapping(value = "/api/enrollment-officers/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveEnrollmentOfficer(@RequestBody EnrollmentOfficer enrollmentOfficer) {

        String returnedMessage = enrollmentOfficerService.saveEnrollmentOfficer(enrollmentOfficer);

        if ("Enrollment officer saved successfully".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.CREATED);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/api/enrollment-officers/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EnrollmentOfficer>> getAllEnrollmentOfficers() {

        return new ResponseEntity<>(enrollmentOfficerService.getAllEnrollmentOfficers(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/enrollment-officers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEnrollmentOfficerById(@PathVariable UUID id) {

        Optional<EnrollmentOfficer> enrollmentOfficer = enrollmentOfficerService.getEnrollmentOfficerById(id);

        if (enrollmentOfficer.isEmpty()) {
            return ApiResponses.fromMessage("The enrollment officer with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(enrollmentOfficer.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/api/enrollment-officers/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateEnrollmentOfficer(@PathVariable UUID id,
            @RequestBody EnrollmentOfficer enrollmentOfficer) {

        String returnedMessage = enrollmentOfficerService.updateEnrollmentOfficer(id, enrollmentOfficer);

        if ("The enrollment officer with that id does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(enrollmentOfficerService.getEnrollmentOfficerById(id).get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/enrollment-officers/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteEnrollmentOfficer(@PathVariable UUID id) {

        String returnedMessage = enrollmentOfficerService.deleteEnrollmentOfficer(id);

        if ("The enrollment officer with that id does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.OK);
    }

    // C1: enrollment officers who hold that household category, matched ignoring letter case
    @GetMapping(value = "/api/enrollment-officers/by-household-category", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<EnrollmentOfficer>> getEnrollmentOfficersByHouseholdCategory(@RequestParam String name) {

        return new ResponseEntity<>(enrollmentOfficerService.getEnrollmentOfficersByHouseholdCategory(name), HttpStatus.OK);
    }

    // D3: add one household category to an enrollment officer, keep the ones already there
    @PutMapping(value = "/api/enrollment-officers/{enrollmentOfficerId}/household-categories/{householdCategoryId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addHouseholdCategoryToEnrollmentOfficer(@PathVariable UUID enrollmentOfficerId,
            @PathVariable UUID householdCategoryId) {

        String returnedMessage = enrollmentOfficerService
                .addHouseholdCategoryToEnrollmentOfficer(enrollmentOfficerId, householdCategoryId);

        if ("The enrollment officer with that id does not exist".equals(returnedMessage)
                || "That household category does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if ("The enrollment officer already has that household category".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.CONFLICT);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.OK);
    }

    // D4: move an enrollment officer to another sector, the old one becomes free
    @PatchMapping(value = "/api/enrollment-officers/{enrollmentOfficerId}/sector",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeEnrollmentOfficerSector(@PathVariable UUID enrollmentOfficerId,
            @RequestParam int sectorNumber) {

        String returnedMessage = enrollmentOfficerService.changeEnrollmentOfficerSector(enrollmentOfficerId,
                sectorNumber);

        if ("The enrollment officer with that id does not exist".equals(returnedMessage)
                || "The sector with that sectorNumber does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if ("That sector is taken by another enrollment officer".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.CONFLICT);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.OK);
    }
}
