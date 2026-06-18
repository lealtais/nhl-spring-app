package com.nhl.nhl_spring_app.controller;

import com.nhl.nhl_spring_app.model.AppUser;
import com.nhl.nhl_spring_app.model.FavoritePlayer;
import com.nhl.nhl_spring_app.repository.AppUserRepository;
import com.nhl.nhl_spring_app.repository.FavoriteGoalRepository;
import com.nhl.nhl_spring_app.repository.FavoritePlayerRepository;
import com.nhl.nhl_spring_app.service.NhlPlayersService;
import com.nhl.nhl_spring_app.service.ShopService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class NhlController {

    private final NhlPlayersService nhlPlayersService;
    private final ShopService shopService;
    private final AppUserRepository userRepository;
    private final FavoriteGoalRepository favoriteGoalRepository;
    private final FavoritePlayerRepository favoritePlayerRepository;

    public NhlController(NhlPlayersService nhlPlayersService, ShopService shopService,
                         AppUserRepository userRepository,
                         FavoriteGoalRepository favoriteGoalRepository,
                         FavoritePlayerRepository favoritePlayerRepository) {
        this.nhlPlayersService = nhlPlayersService;
        this.shopService = shopService;
        this.userRepository = userRepository;
        this.favoriteGoalRepository = favoriteGoalRepository;
        this.favoritePlayerRepository = favoritePlayerRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/jogadores/nhl")
    public String nhlTeams(Model model) {
        model.addAttribute("teams", nhlPlayersService.getTeams());
        return "teams-nhl";
    }

    @GetMapping("/jogadores/nhl/{teamAbbrev}")
    public String nhlPlayersByTeam(@PathVariable String teamAbbrev, Model model) {
        model.addAttribute("players", nhlPlayersService.getPlayersByTeam(teamAbbrev));
        model.addAttribute("teamAbbrev", teamAbbrev);
        return "players-nhl";
    }

    @GetMapping("/jogadores/pwhl")
    public String pwhlPlayers() {
        return "players-pwhl";
    }

    @GetMapping("/leafs-nation-shop")
    public String leafsNationShop() {
        return "redirect:/nhl-shop";
    }

    @GetMapping("/leafs-nation-shop/leafs")
    public String leafsNationLeafsPage() {
        return "redirect:/nhl-shop/TOR";
    }

    @GetMapping("/nhl-shop")
    public String nhlShopHome(
            @RequestParam(value = "query", required = false) String query,
            Model model) {
        
        if (query != null && !query.isEmpty()) {
            model.addAttribute("items", shopService.getFilteredItems(query, null, null));
            model.addAttribute("isSearch", true);
            model.addAttribute("query", query);
        } else {
            model.addAttribute("featuredItems", shopService.getFeaturedItems());
            model.addAttribute("teams", nhlPlayersService.getTeams());
            model.addAttribute("isSearch", false);
        }
        
        return "nhl-shop-home";
    }

    @GetMapping("/nhl-shop/{teamAbbrev}")
    public String nhlShopTeam(
            @PathVariable String teamAbbrev,
            @RequestParam(value = "category", required = false) String category,
            Model model) {
        model.addAttribute("items", shopService.getFilteredItems(null, category, teamAbbrev));
        model.addAttribute("teamAbbrev", teamAbbrev);
        model.addAttribute("category", category);
        return "nhl-shop-team";
    }

    @GetMapping("/estatisticas-de-jogos")
    public String gameStats() {
        return "stats";
    }

    @GetMapping({"/estatisticas", "/estatisticas/"})
    public String showPuckTracker() {
        return "forward:/estatisticas/index.html";
    }

    @GetMapping("/site-oficial-nhl")
    public String nhlOfficialSite() {
        return "redirect:https://www.nhl.com/";
    }

    // ===== FAVORITES ROUTES =====

    private AppUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return userRepository.findByUsername(auth.getName()).orElse(null);
    }

    @GetMapping("/favoritos")
    public String favorites(Model model) {
        AppUser user = getCurrentUser();
        if (user == null) return "redirect:/login";

        model.addAttribute("favoritePlayers", favoritePlayerRepository.findByUserId(user.getId()));
        model.addAttribute("favoriteGoals", favoriteGoalRepository.findByUserId(user.getId()));
        return "favorites";
    }

    @PostMapping("/favoritos/players/add")
    public String addFavoritePlayer(
            @RequestParam("playerId") Long playerId,
            @RequestParam("playerName") String playerName,
            @RequestParam(value = "team", required = false) String team,
            @RequestParam(value = "position", required = false) String position,
            @RequestParam(value = "playerNumber", required = false) Integer playerNumber,
            @RequestParam(value = "image", required = false) String image,
            HttpServletRequest request) {

        AppUser user = getCurrentUser();
        if (user == null) return "redirect:/login";

        String referer = request.getHeader("Referer");
        String redirectUrl = referer != null ? "redirect:" + referer : "redirect:/jogadores/nhl";

        // Don't add duplicates
        if (favoritePlayerRepository.findByUserIdAndPlayerId(user.getId(), playerId).isPresent()) {
            return redirectUrl;
        }

        FavoritePlayer fav = new FavoritePlayer();
        fav.setUser(user);
        fav.setPlayerId(playerId);
        fav.setPlayerName(playerName);
        fav.setTeam(team);
        fav.setPosition(position);
        fav.setPlayerNumber(playerNumber);
        fav.setImage(image);
        favoritePlayerRepository.save(fav);

        return redirectUrl;
    }

    @PostMapping("/favoritos/players/remove/{id}")
    @Transactional
    public String removeFavoritePlayer(@PathVariable Long id, HttpServletRequest request) {
        AppUser user = getCurrentUser();
        if (user == null) return "redirect:/login";

        favoritePlayerRepository.findById(id).ifPresent(fav -> {
            if (fav.getUser().getId().equals(user.getId())) {
                favoritePlayerRepository.deleteById(id);
            }
        });

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/favoritos");
    }

    @PostMapping("/favoritos/goals/remove/{id}")
    @Transactional
    public String removeFavoriteGoal(@PathVariable Long id, HttpServletRequest request) {
        AppUser user = getCurrentUser();
        if (user == null) return "redirect:/login";

        favoriteGoalRepository.findById(id).ifPresent(fav -> {
            if (fav.getUser().getId().equals(user.getId())) {
                favoriteGoalRepository.deleteById(id);
            }
        });

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/favoritos");
    }

    @PostMapping("/favoritos/account/delete")
    @Transactional
    public String deleteAccount(HttpServletRequest request) {
        AppUser user = getCurrentUser();
        if (user == null) return "redirect:/login";

        // Delete user (CASCADE will handle favorites)
        userRepository.delete(user);

        // Invalidate session
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }
}
