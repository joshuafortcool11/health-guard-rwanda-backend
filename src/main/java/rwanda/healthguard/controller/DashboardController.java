package rwanda.healthguard.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import rwanda.healthguard.service.DashboardService;

@RestController
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // Bonus: HealthGuard Rwanda dashboard summary
    @GetMapping(value = "/api/dashboard/summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> getDashboardSummary() {

        return new ResponseEntity<>(dashboardService.getDashboardSummary(), HttpStatus.OK);
    }
}
