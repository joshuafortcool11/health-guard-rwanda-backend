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

import rwanda.healthguard.domain.Citizen;
import rwanda.healthguard.service.CitizenService;
import rwanda.healthguard.util.ApiResponses;

@RestController
public class CitizenController {

    @Autowired
    private CitizenService citizenService;

    @PostMapping(value = "/api/citizens/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveCitizen(@RequestBody Citizen citizen) {

        String returnedMessage = citizenService.saveCitizen(citizen);

        if ("Citizen saved successfully".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.CREATED);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/api/citizens/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Citizen>> getAllCitizens() {

        return new ResponseEntity<>(citizenService.getAllCitizens(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/citizens/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCitizenById(@PathVariable UUID id) {

        Optional<Citizen> citizen = citizenService.getCitizenById(id);

        if (citizen.isEmpty()) {
            return ApiResponses.fromMessage("The citizen with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(citizen.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/api/citizens/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCitizen(@PathVariable UUID id, @RequestBody Citizen citizen) {

        String returnedMessage = citizenService.updateCitizen(id, citizen);

        if ("The citizen with that id does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(citizenService.getCitizenById(id).get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/citizens/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCitizen(@PathVariable UUID id) {

        String returnedMessage = citizenService.deleteCitizen(id);

        if ("The citizen with that id does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.OK);
    }
}
