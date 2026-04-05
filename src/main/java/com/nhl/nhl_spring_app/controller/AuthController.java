package com.nhl.nhl_spring_app.controller;

import com.nhl.nhl_spring_app.model.AppUserDAO;
import com.nhl.nhl_spring_app.repository.AppUserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AppUserRepository userRepository;
    private final AppUserDAO userDAO;

    public AuthController(AppUserRepository userRepository, AppUserDAO userDAO) {
        this.userRepository = userRepository;
        this.userDAO = userDAO;
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                         @RequestParam(value = "registered", required = false) String registered,
                         @RequestParam(value = "logout", required = false) String logoutParam,
                         Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Usuário ou senha inválidos.");
        }
        if (registered != null) {
            model.addAttribute("successMsg", "Conta criada com sucesso! Faça login abaixo.");
        }
        if (logoutParam != null) {
            model.addAttribute("successMsg", "Você saiu da sua conta.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                Model model) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            model.addAttribute("errorMsg", "Preencha todos os campos!");
            return "register";
        }

        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("errorMsg", "O nome de usuário já está em uso!");
            return "register";
        }

        // Requisito: INSERT realizado com SQL puro (JDBC) via DAO
        userDAO.inserirUsuarioManual(username.trim(), password);

        return "redirect:/leafs-nation-shop/leafs?registered";
    }
}
