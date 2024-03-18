package bsuir.kraevskij.sportevent.controller;

import bsuir.kraevskij.sportevent.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RePasswordController {
    private final PasswordResetService passwordResetService;

    @Autowired
    public RePasswordController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }
    @PostMapping("auth/reset-password-email")
    @ResponseBody
    public String resetPassword(@RequestParam String email) {
        passwordResetService.initiatePasswordReset(email);
        return "Password reset request accepted for email: " + email;
    }


}
