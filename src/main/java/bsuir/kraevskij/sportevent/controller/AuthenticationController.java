package bsuir.kraevskij.sportevent.controller;


import bsuir.kraevskij.sportevent.exception.ErrorResponse;
import bsuir.kraevskij.sportevent.model.AuthenticationResponse;
import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User request) {

        if (request.getUsername() == null || request.getUsername().isEmpty() ||
                request.getPassword() == null || request.getPassword().isEmpty() ||
                request.getRole() == null || request.getRole().getName().isEmpty() ||
                request.getFirstName() == null || request.getFirstName().isEmpty() ||
                request.getLastName() == null || request.getLastName().isEmpty()) {
            String errorMessage = "Поля '" +
                    (request.getUsername() == null || request.getUsername().isEmpty() ? "имя пользователя, " : "") +
                    (request.getPassword() == null || request.getPassword().isEmpty() ? "пароль, " : "") +
                    (request.getRole() == null || request.getRole().getName().isEmpty() ? "роль, " : "") +
                    (request.getFirstName() == null || request.getFirstName().isEmpty() ? "имя, " : "") +
                    (request.getLastName() == null || request.getLastName().isEmpty() ? "фамилия, " : "") +
                    "' обязательны для заполнения";
            errorMessage = errorMessage.replaceAll(", $", "");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage));
        }

        AuthenticationResponse response = authService.register(request);

        if (response.getErrorCode() != 0) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", response.getMessage());
            return ResponseEntity.status(response.getErrorCode()).body(errorResponse);
        } else {
            return ResponseEntity.ok(response);
        }



    }



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        try {
            if (request.getUsername() == null || request.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Логин и пароль должны быть указаны.\"}");
            }

            AuthenticationResponse authenticationResponse = authService.authenticate(request);
            return ResponseEntity.ok(authenticationResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Произошла ошибка: " + e.getMessage() + "\"}");
        }
    }
}
