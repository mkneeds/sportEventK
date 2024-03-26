package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.Token;
import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.model.UserBalance;
import bsuir.kraevskij.sportevent.repository.UserBalanceRepository;
import bsuir.kraevskij.sportevent.repository.UserRepository;
import bsuir.kraevskij.sportevent.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class TokenValidationController {

    private final JwtService jwtService;
    private final UserBalanceRepository userBalanceRepository;
    private final UserRepository userRepository;

    public TokenValidationController(JwtService jwtService, UserBalanceRepository userBalanceRepository, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userBalanceRepository = userBalanceRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("auth/login/validateToken")
    public ResponseEntity<?> validateToken(@RequestBody Token token) {
       try {
            User user = new User();
            user.setUsername(jwtService.extractUsername(token.getToken()));
            if (jwtService.isValid(token.getToken(),user)) {
                Map<String, String> tokenResponse = new HashMap<>();
                Optional<User> userOptional= userRepository.findByUsername(user.getUsername());
                if (userOptional.isPresent()) {
                    user.setId(userOptional.get().getId());
                }
                UserBalance userBalance = userBalanceRepository.findByUser(user);
                tokenResponse.put("message", user.getUsername()+" ("+userBalance.getBalance()+" руб.)");
                tokenResponse.put("role",userOptional.get().getRole().getName());
                return ResponseEntity.ok().body(tokenResponse);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
           }
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Произошла ошибка: " + e.getMessage() + "\"}");
       }
    }
}

