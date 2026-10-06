package rwanda.healthguard.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rwanda.healthguard.domain.Sector;
import rwanda.healthguard.repository.SectorRepository;

@Service
public class SectorService {

    @Autowired
    private SectorRepository sectorRepository;

    @Transactional
    public String saveSector(Sector sector) {

        sector.setId(null);
        sectorRepository.save(sector);

        return "Sector saved successfully";
    }

    @Transactional
    public String updateSector(UUID id, Sector sector) {

        Optional<Sector> existingSector = sectorRepository.findById(id);

        if (existingSector.isEmpty()) {
            return "The sector with that id does not exist";
        }

        Sector sectorToUpdate = existingSector.get();
        sectorToUpdate.setSectorNumber(sector.getSectorNumber());
        sectorToUpdate.setName(sector.getName());

        sectorRepository.save(sectorToUpdate);

        return "Sector updated successfully";
    }

    @Transactional
    public String deleteSector(UUID id) {

        Optional<Sector> existingSector = sectorRepository.findById(id);

        if (existingSector.isEmpty()) {
            return "The sector with that id does not exist";
        }

        sectorRepository.delete(existingSector.get());

        return "Sector deleted successfully";
    }

    public Optional<Sector> getSectorById(UUID id) {

        return sectorRepository.findById(id);
    }

    public List<Sector> getAllSectors() {

        return sectorRepository.findAll();
    }
}
