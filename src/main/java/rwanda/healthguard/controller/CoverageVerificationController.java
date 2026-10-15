package rwanda.healthguard.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rwanda.healthguard.domain.CoverageVerification;
import rwanda.healthguard.service.CoverageVerificationService;
import rwanda.healthguard.util.ApiResponses;

@RestController
public class CoverageVerificationController {

    @Autowired
    private CoverageVerificationService coverageVerificationService;

    // E1: write a coverage verification for a COVERED enrollment
    @PostMapping(value = "/api/coverage-verifications/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCoverageVerification(@RequestParam UUID enrollmentId,
            @RequestBody CoverageVerification coverageVerification) {

        String returnedMessage = coverageVerificationService.saveCoverageVerification(enrollmentId,
                coverageVerification);

        if ("The enrollment with that id does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        if ("Coverage verifications are only for COVERED enrollments".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.BAD_REQUEST);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.CREATED);
    }

    // E2: coverage verification history of a citizen, newest verifiedDate first
    @GetMapping(value = "/api/coverage-verifications/citizen/{nationalId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CoverageVerification>> getCoverageVerificationsOfCitizen(
            @PathVariable String nationalId) {

        return new ResponseEntity<>(coverageVerificationService.getCoverageVerificationsOfCitizen(nationalId),
                HttpStatus.OK);
    }

    // E3: delete a coverage verification
    @DeleteMapping(value = "/api/coverage-verifications/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCoverageVerification(@PathVariable UUID id) {

        String returnedMessage = coverageVerificationService.deleteCoverageVerification(id);

        if ("The coverage verification with that id does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.OK);
    }
}
