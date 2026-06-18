package com.nhl.nhl_spring_app.controller;

import com.nhl.nhl_spring_app.model.AppUser;
import com.nhl.nhl_spring_app.model.PasswordResetToken;
import com.nhl.nhl_spring_app.repository.AppUserRepository;
import com.nhl.nhl_spring_app.repository.PasswordResetTokenRepository;
import com.nhl.nhl_spring_app.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordResetController {

    private final AppUserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(AppUserRepository userRepository,
                                   PasswordResetTokenRepository tokenRepository,
                                   EmailService emailService,
                                   PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    @Transactional
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        // Find user by email
        // We will do a manual filter since we don't have findByEmail in the repository yet
        Optional<AppUser> userOpt = userRepository.findAll().stream()
                .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                .findFirst();

        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            // Clear existing tokens
            tokenRepository.deleteByUserId(user.getId());

            String token = UUID.randomUUID().toString();
            PasswordResetToken resetToken = new PasswordResetToken(token, user);
            tokenRepository.save(resetToken);

            emailService.sendPasswordResetEmail(user.getEmail(), token);
            
            String resetUrl = "http://localhost:8081/reset-password?token=" + token;
            model.addAttribute("resetUrl", resetUrl);
        }

        // We always show success message to prevent email enumeration attacks
        model.addAttribute("successMsg", "Se o e-mail existir na nossa base, enviaremos um link de recuperação.");
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);
        if (resetToken.isEmpty() || resetToken.get().isExpired()) {
            model.addAttribute("errorMsg", "Link de recuperação inválido ou expirado.");
            return "login";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String password,
                                       Model model) {
        Optional<PasswordResetToken> resetTokenOpt = tokenRepository.findByToken(token);
        if (resetTokenOpt.isEmpty() || resetTokenOpt.get().isExpired()) {
            model.addAttribute("errorMsg", "Link de recuperação inválido ou expirado.");
            return "login";
        }

        AppUser user = resetTokenOpt.get().getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        
        tokenRepository.delete(resetTokenOpt.get());

        model.addAttribute("successMsg", "Sua senha foi redefinida com sucesso! Faça login.");
        return "login";
    }
}
