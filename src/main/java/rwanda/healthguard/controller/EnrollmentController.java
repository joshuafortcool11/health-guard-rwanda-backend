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

import rwanda.healthguard.domain.Enrollment;
import rwanda.healthguard.service.EnrollmentService;
import rwanda.healthguard.util.ApiResponses;

@RestController
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping(value = "/api/enrollments/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveEnrollment(@RequestBody Enrollment enrollment) {

        String returnedMessage = enrollmentService.saveEnrollment(enrollment);

        if ("Enrollment saved successfully".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.CREATED);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/api/enrollments/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {

        return new ResponseEntity<>(enrollmentService.getAllEnrollments(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/enrollments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getEnrollmentById(@PathVariable UUID id) {

        Optional<Enrollment> enrollment = enrollmentService.getEnrollmentById(id);

        if (enrollment.isEmpty()) {
            return ApiResponses.fromMessage("The enrollment with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(enrollment.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/api/enrollments/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateEnrollment(@PathVariable UUID id, @RequestBody Enrollment enrollment) {

        String returnedMessage = enrollmentService.updateEnrollment(id, enrollment);

        if ("The enrollment with that id does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(enrollmentService.getEnrollmentById(id).get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/enrollments/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteEnrollment(@PathVariable UUID id) {

        String returnedMessage = enrollmentService.deleteEnrollment(id);

        if ("The enrollment with that id does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.OK);
    }
}
