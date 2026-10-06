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

import rwanda.healthguard.domain.Sector;
import rwanda.healthguard.service.SectorService;
import rwanda.healthguard.util.ApiResponses;

@RestController
public class SectorController {

    @Autowired
    private SectorService sectorService;

    @PostMapping(value = "/api/sectors/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveSector(@RequestBody Sector sector) {

        String returnedMessage = sectorService.saveSector(sector);

        if ("Sector saved successfully".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.CREATED);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/api/sectors/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sector>> getAllSectors() {

        return new ResponseEntity<>(sectorService.getAllSectors(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/sectors/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSectorById(@PathVariable UUID id) {

        Optional<Sector> sector = sectorService.getSectorById(id);

        if (sector.isEmpty()) {
            return ApiResponses.fromMessage("The sector with that id does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(sector.get(), HttpStatus.OK);
    }

    @PutMapping(value = "/api/sectors/update/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSector(@PathVariable UUID id, @RequestBody Sector sector) {

        String returnedMessage = sectorService.updateSector(id, sector);

        if ("The sector with that id does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(sectorService.getSectorById(id).get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/sectors/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteSector(@PathVariable UUID id) {

        String returnedMessage = sectorService.deleteSector(id);

        if ("The sector with that id does not exist".equals(returnedMessage)) {
            return ApiResponses.fromMessage(returnedMessage, HttpStatus.NOT_FOUND);
        }

        return ApiResponses.fromMessage(returnedMessage, HttpStatus.OK);
    }
}
