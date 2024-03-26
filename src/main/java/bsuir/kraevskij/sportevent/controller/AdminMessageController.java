package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.model.AdminMessage;
import bsuir.kraevskij.sportevent.model.User;
import bsuir.kraevskij.sportevent.repository.UserRepository;
import bsuir.kraevskij.sportevent.service.AdminMessageService;
import bsuir.kraevskij.sportevent.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class AdminMessageController {

    private final AdminMessageService adminMessageService;
    private  final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public AdminMessageController(AdminMessageService adminMessageService, JwtService jwtService, UserRepository userRepository) {
        this.adminMessageService = adminMessageService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/sendAdminMessage")
    public String sendAdminMessage(@RequestParam String title,
                                   @RequestParam String description, HttpServletRequest request) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDate = currentDate.format(formatter);
        String token = jwtService.extractTokenFromCookie(request);
        Optional<User> user = userRepository.findByUsername(jwtService.extractUsername(token));
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setDate(formattedDate);
        adminMessage.setTitle(title);
        adminMessage.setDescription(description);
        adminMessage.setUser(user.get());
        adminMessage.setDuration("1 day");
        adminMessageService.saveAdminMessage(adminMessage);
        return "redirect:/profile-admin";
    }
}
