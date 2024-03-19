package bsuir.kraevskij.sportevent.service;

import bsuir.kraevskij.sportevent.model.Token;
import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.repository.TokenRepository;
import bsuir.kraevskij.sportevent.repository.UserRepository;
import bsuir.kraevskij.sportevent.service.EmailService;
import bsuir.kraevskij.sportevent.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public PasswordResetService(JwtService jwtService, UserRepository userRepository,
                                TokenRepository tokenRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }
    public boolean validatePasswordResetToken(String token) {
        Optional<Token> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            Token resetToken = tokenOptional.get();

            if (!resetToken.isLoggedOut() && !jwtService.isTokenExpired(token)) {
                LocalDateTime tokenCreationTime = resetToken.getCreatedAt();
                LocalDateTime now = LocalDateTime.now();
                return tokenCreationTime.isAfter(now.minusHours(24));
            }
        }
        return false;
    }


    public void initiatePasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String resetToken = jwtService.generatePasswordResetToken(user);

            Token token = new Token();
            token.setToken(resetToken);
            token.setUser(user);
            token.setLoggedOut(false);
            tokenRepository.save(token);

            String resetLink = "http://localhost:8080/auth/reset-password-form?token=" + resetToken;
            String emailBody = "Для сброса пароля перейдите по ссылке: " + resetLink;
            emailService.sendEmail(user.getEmail(), "Сброс пароля", emailBody);
        }
    }

    public boolean resetPassword(String token, String newPassword) {

        Optional<Token> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            Token resetToken = tokenOptional.get();
            if (!resetToken.isLoggedOut() && !jwtService.isTokenExpired(token)) {
                User user = resetToken.getUser();
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                resetToken.setLoggedOut(true);
                tokenRepository.save(resetToken);
                return true;
            }
        }
        return false;
    }
}
