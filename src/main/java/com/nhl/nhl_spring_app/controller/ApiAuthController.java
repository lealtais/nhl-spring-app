package com.nhl.nhl_spring_app.controller;

import com.nhl.nhl_spring_app.model.AppUser;
import com.nhl.nhl_spring_app.model.AppUserDAO;
import com.nhl.nhl_spring_app.repository.AppUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final AppUserRepository userRepository;
    private final AppUserDAO userDAO;
    private final AuthenticationManager authenticationManager;

    public ApiAuthController(AppUserRepository userRepository, AppUserDAO userDAO, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userDAO = userDAO;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String username = body.get("username");
        String password = body.get("password");
        String email = body.get("email");

        if (username == null || username.isBlank() || password == null || password.isBlank() || email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuário, senha e e-mail são obrigatórios."));
        }

        Optional<AppUser> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Usuário já existe."));
        }

        userDAO.inserirUsuarioManual(username, password, email);

        // Auto-login after registration
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        } catch (AuthenticationException e) {
            // Registration succeeded but auto-login failed - user can login manually
        }

        AppUser user = userRepository.findByUsername(username).orElse(null);
        return ResponseEntity.ok(Map.of(
            "message", "Conta criada com sucesso!",
            "username", username,
            "id", user != null ? user.getId() : 0
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuário e senha são obrigatórios."));
        }

        try {
            AppUser userBeforeAuth = userRepository.findByUsername(username).orElse(null);
            if (userBeforeAuth != null && userBeforeAuth.isBanned()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Esta conta foi banida."));
            }

            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            AppUser user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                user.setLastLogin(java.time.LocalDateTime.now());
                userRepository.save(user);
            }
            
            return ResponseEntity.ok(Map.of(
                "message", "Login realizado com sucesso!",
                "username", username,
                "id", user != null ? user.getId() : 0,
                "role", user != null ? user.getRole() : "USER"
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Usuário ou senha inválidos."));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Não autenticado."));
        }

        String username = auth.getName();
        AppUser user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuário não encontrado."));
        }

        return ResponseEntity.ok(Map.of(
            "username", user.getUsername(),
            "id", user.getId()
        ));
    }

    @DeleteMapping("/account")
    public ResponseEntity<?> deleteAccount(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Não autenticado."));
        }

        String username = auth.getName();
        Optional<AppUser> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuário não encontrado."));
        }

        // Delete user (CASCADE will delete all favorites)
        userRepository.delete(userOpt.get());

        // Invalidate session
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok(Map.of("message", "Conta excluída com sucesso."));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(Map.of("message", "Logout realizado com sucesso."));
    }
}
