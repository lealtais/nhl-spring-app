package com.nhl.nhl_spring_app.controller;

import com.nhl.nhl_spring_app.model.AppUser;
import com.nhl.nhl_spring_app.repository.AppUserRepository;
import com.nhl.nhl_spring_app.repository.FavoriteGoalRepository;
import com.nhl.nhl_spring_app.repository.FavoritePlayerRepository;
import com.nhl.nhl_spring_app.repository.UserPurchaseRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/perfil")
public class ProfileController {

    private final AppUserRepository userRepository;
    private final FavoriteGoalRepository favoriteGoalRepository;
    private final FavoritePlayerRepository favoritePlayerRepository;
    private final UserPurchaseRepository userPurchaseRepository;

    private static final String UPLOAD_DIR = "./uploads/";

    public ProfileController(AppUserRepository userRepository,
                             FavoriteGoalRepository favoriteGoalRepository,
                             FavoritePlayerRepository favoritePlayerRepository,
                             UserPurchaseRepository userPurchaseRepository) {
        this.userRepository = userRepository;
        this.favoriteGoalRepository = favoriteGoalRepository;
        this.favoritePlayerRepository = favoritePlayerRepository;
        this.userPurchaseRepository = userPurchaseRepository;
    }

    @GetMapping
    public String showProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = auth.getName();
        AppUser user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("purchases", userPurchaseRepository.findByUserIdOrderByPurchaseDateDesc(user.getId()));
        model.addAttribute("favoriteGoals", favoriteGoalRepository.findByUserId(user.getId()));
        model.addAttribute("favoritePlayers", favoritePlayerRepository.findByUserId(user.getId()));

        return "profile";
    }

    @PostMapping("/upload-foto")
    public String uploadProfilePicture(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMsg", "Por favor, selecione um arquivo.");
            return "redirect:/perfil";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = userRepository.findByUsername(auth.getName()).orElse(null);

        if (user != null) {
            try {
                // Lê os bytes da imagem enviada
                byte[] bytes = file.getBytes();
                // Converte para Base64
                String base64Image = java.util.Base64.getEncoder().encodeToString(bytes);
                String mimeType = file.getContentType();
                // Monta a string no formato Data URL para o HTML renderizar direto
                String dataUrl = "data:" + mimeType + ";base64," + base64Image;

                // Salva o texto gigante direto no banco de dados (que tem tipo TEXT)
                user.setProfilePicture(dataUrl);
                userRepository.save(user);

                redirectAttributes.addFlashAttribute("successMsg", "Foto atualizada com sucesso!");

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("errorMsg", "Erro ao fazer upload da imagem.");
            }
        }

        return "redirect:/perfil";
    }
}
