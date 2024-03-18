package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.Token;
import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TokenValidationController {

    private final JwtService jwtService;

    public TokenValidationController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("auth/login/validateToken")
    public ResponseEntity<?> validateToken(@RequestBody Token token) {
        try {
            User user = new User();
            user.setUsername(jwtService.extractUsername(token.getToken()));
            if (jwtService.isValid(token.getToken(),user)) {
                Map<String, String> tokenResponse = new HashMap<>();
                tokenResponse.put("message", user.getUsername());
                return ResponseEntity.ok().body(tokenResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Произошла ошибка: " + e.getMessage() + "\"}");
        }
    }
}
