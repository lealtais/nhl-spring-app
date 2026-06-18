package com.nhl.nhl_spring_app.controller;

import com.nhl.nhl_spring_app.model.AppUser;
import com.nhl.nhl_spring_app.repository.AppUserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AppUserRepository userRepository;

    public AdminController(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        List<AppUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @PostMapping("/users/{id}/ban")
    public String toggleBanUser(@PathVariable Long id) {
        Optional<AppUser> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            AppUser user = userOpt.get();
            // Não permite banir o próprio admin principal
            if (!"admin".equals(user.getUsername())) {
                user.setBanned(!user.isBanned());
                userRepository.save(user);
            }
        }
        return "redirect:/admin";
    }
}
