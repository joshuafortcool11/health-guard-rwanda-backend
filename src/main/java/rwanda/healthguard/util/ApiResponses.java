package rwanda.healthguard.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponses {

    public static ResponseEntity<?> fromMessage(String message, HttpStatus status) {
        return new ResponseEntity<>(message, status);
    }
}
