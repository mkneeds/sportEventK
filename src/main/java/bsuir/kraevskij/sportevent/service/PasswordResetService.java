package bsuir.kraevskij.sportevent.service;

import bsuir.kraevskij.sportevent.model.Token;
import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.repository.TokenRepository;
import bsuir.kraevskij.sportevent.repository.UserRepository;
import bsuir.kraevskij.sportevent.service.EmailService;
import bsuir.kraevskij.sportevent.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Autowired
    public PasswordResetService(JwtService jwtService, UserRepository userRepository,
                                TokenRepository tokenRepository, EmailService emailService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    public void initiatePasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String resetToken = jwtService.generatePasswordResetToken(user);

            // Сохранить токен сброса пароля в репозитории
            Token token = new Token();
            token.setToken(resetToken);
            token.setUser(user);
            token.setLoggedOut(false);
            tokenRepository.save(token);

            // Отправить электронное письмо с ссылкой для сброса пароля
            String resetLink = "https://localhost:8080/reset-password?token=" + resetToken;
            String emailBody = "Для сброса пароля перейдите по ссылке: " + resetLink;
            emailService.sendEmail(user.getEmail(), "Сброс пароля", emailBody);
        }
    }

    public boolean resetPassword(String token, String newPassword) {
        // Извлечь информацию о пользователе из токена
        Optional<Token> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isPresent()) {
            Token resetToken = tokenOptional.get();
            if (!resetToken.isLoggedOut() && !jwtService.isTokenExpired(token)) {
                User user = resetToken.getUser();
                // Обновить пароль пользователя
                user.setPassword(newPassword);
                userRepository.save(user);
                // Пометить токен сброса пароля как использованный
                resetToken.setLoggedOut(true);
                tokenRepository.save(resetToken);
                return true; // Успешно сброшен пароль
            }
        }
        return false; // Не удалось сбросить пароль
    }
}
