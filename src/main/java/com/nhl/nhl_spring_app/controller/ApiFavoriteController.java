package com.nhl.nhl_spring_app.controller;

import com.nhl.nhl_spring_app.model.AppUser;
import com.nhl.nhl_spring_app.model.FavoriteGoal;
import com.nhl.nhl_spring_app.model.FavoritePlayer;
import com.nhl.nhl_spring_app.repository.AppUserRepository;
import com.nhl.nhl_spring_app.repository.FavoriteGoalRepository;
import com.nhl.nhl_spring_app.repository.FavoritePlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
public class ApiFavoriteController {

    private final FavoriteGoalRepository favoriteGoalRepository;
    private final FavoritePlayerRepository favoritePlayerRepository;
    private final AppUserRepository userRepository;

    public ApiFavoriteController(FavoriteGoalRepository favoriteGoalRepository,
                                  FavoritePlayerRepository favoritePlayerRepository,
                                  AppUserRepository userRepository) {
        this.favoriteGoalRepository = favoriteGoalRepository;
        this.favoritePlayerRepository = favoritePlayerRepository;
        this.userRepository = userRepository;
    }

    private AppUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return userRepository.findByUsername(auth.getName()).orElse(null);
    }

    // ==================== GOALS ====================

    @GetMapping("/goals")
    public ResponseEntity<?> getGoals() {
        AppUser user = getCurrentUser();
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Não autenticado."));

        List<FavoriteGoal> goals = favoriteGoalRepository.findByUserId(user.getId());
        List<Map<String, Object>> result = goals.stream().map(g -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", g.getId());
            map.put("gameId", g.getGameId());
            map.put("eventId", g.getEventId());
            map.put("playerName", g.getPlayerName());
            map.put("playerPhoto", g.getPlayerPhoto());
            map.put("team", g.getTeam());
            map.put("period", g.getPeriod());
            map.put("timeInPeriod", g.getTimeInPeriod());
            map.put("x", g.getX());
            map.put("y", g.getY());
            map.put("shotType", g.getShotType());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/goals")
    public ResponseEntity<?> addGoal(@RequestBody Map<String, Object> body) {
        AppUser user = getCurrentUser();
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Não autenticado."));

        Long gameId = toLong(body.get("gameId"));
        Integer eventId = toInt(body.get("eventId"));

        if (gameId == null || eventId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "gameId e eventId são obrigatórios."));
        }

        // Check if already favorited
        Optional<FavoriteGoal> existing = favoriteGoalRepository.findByUserIdAndGameIdAndEventId(user.getId(), gameId, eventId);
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Gol já está nos favoritos."));
        }

        FavoriteGoal goal = new FavoriteGoal();
        goal.setUser(user);
        goal.setGameId(gameId);
        goal.setEventId(eventId);
        goal.setPlayerName((String) body.get("playerName"));
        goal.setPlayerPhoto((String) body.get("playerPhoto"));
        goal.setTeam((String) body.get("team"));
        goal.setPeriod(toInt(body.get("period")));
        goal.setTimeInPeriod((String) body.get("timeInPeriod"));
        goal.setX(toDouble(body.get("x")));
        goal.setY(toDouble(body.get("y")));
        goal.setShotType((String) body.get("shotType"));

        FavoriteGoal saved = favoriteGoalRepository.save(goal);
        return ResponseEntity.ok(Map.of("message", "Gol favoritado!", "id", saved.getId()));
    }

    @DeleteMapping("/goals/{id}")
    @Transactional
    public ResponseEntity<?> removeGoal(@PathVariable Long id) {
        AppUser user = getCurrentUser();
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Não autenticado."));

        Optional<FavoriteGoal> goal = favoriteGoalRepository.findById(id);
        if (goal.isEmpty() || !goal.get().getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Favorito não encontrado."));
        }

        favoriteGoalRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Gol removido dos favoritos."));
    }

    // ==================== PLAYERS ====================

    @GetMapping("/players")
    public ResponseEntity<?> getPlayers() {
        AppUser user = getCurrentUser();
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Não autenticado."));

        List<FavoritePlayer> players = favoritePlayerRepository.findByUserId(user.getId());
        List<Map<String, Object>> result = players.stream().map(p -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", p.getId());
            map.put("playerId", p.getPlayerId());
            map.put("playerName", p.getPlayerName());
            map.put("team", p.getTeam());
            map.put("position", p.getPosition());
            map.put("playerNumber", p.getPlayerNumber());
            map.put("image", p.getImage());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/players")
    public ResponseEntity<?> addPlayer(@RequestBody Map<String, Object> body) {
        AppUser user = getCurrentUser();
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Não autenticado."));

        Long playerId = toLong(body.get("playerId"));
        if (playerId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "playerId é obrigatório."));
        }

        Optional<FavoritePlayer> existing = favoritePlayerRepository.findByUserIdAndPlayerId(user.getId(), playerId);
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Jogador já está nos favoritos."));
        }

        FavoritePlayer player = new FavoritePlayer();
        player.setUser(user);
        player.setPlayerId(playerId);
        player.setPlayerName((String) body.get("playerName"));
        player.setTeam((String) body.get("team"));
        player.setPosition((String) body.get("position"));
        player.setPlayerNumber(toInt(body.get("playerNumber")));
        player.setImage((String) body.get("image"));

        FavoritePlayer saved = favoritePlayerRepository.save(player);
        return ResponseEntity.ok(Map.of("message", "Jogador favoritado!", "id", saved.getId()));
    }

    @DeleteMapping("/players/{id}")
    @Transactional
    public ResponseEntity<?> removePlayer(@PathVariable Long id) {
        AppUser user = getCurrentUser();
        if (user == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Não autenticado."));

        Optional<FavoritePlayer> player = favoritePlayerRepository.findById(id);
        if (player.isEmpty() || !player.get().getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Favorito não encontrado."));
        }

        favoritePlayerRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Jogador removido dos favoritos."));
    }

    // ==================== HELPERS ====================

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).longValue();
        try { return Long.parseLong(value.toString()); } catch (NumberFormatException e) { return null; }
    }

    private Integer toInt(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).intValue();
        try { return Integer.parseInt(value.toString()); } catch (NumberFormatException e) { return null; }
    }

    private Double toDouble(Object value) {
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).doubleValue();
        try { return Double.parseDouble(value.toString()); } catch (NumberFormatException e) { return null; }
    }
}
