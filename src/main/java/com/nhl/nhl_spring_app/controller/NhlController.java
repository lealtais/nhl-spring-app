package com.nhl.nhl_spring_app.controller;

import com.nhl.nhl_spring_app.service.NhlPlayersService;
import com.nhl.nhl_spring_app.service.ShopService;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class NhlController {

    private final NhlPlayersService nhlPlayersService;
    private final ShopService shopService;

    public NhlController(NhlPlayersService nhlPlayersService, ShopService shopService) {
        this.nhlPlayersService = nhlPlayersService;
        this.shopService = shopService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/jogadores/nhl")
    public String nhlPlayers(Model model) {
        model.addAttribute("players", nhlPlayersService.getPlayers());
        return "players-nhl";
    }

    @GetMapping("/jogadores/pwhl")
    public String pwhlPlayers() {
        return "players-pwhl";
    }

    @GetMapping("/leafs-nation-shop")
    public String leafsNationShop() {
        return "redirect:/leafs-nation-shop/leafs";
    }

    @GetMapping("/leafs-nation-shop/leafs")
    public String leafsNationLeafsPage(
            @org.springframework.web.bind.annotation.RequestParam(value = "query", required = false) String query,
            @org.springframework.web.bind.annotation.RequestParam(value = "category", required = false) String category,
            Model model) {
        model.addAttribute("items", shopService.getFilteredItems(query, category));
        return "leafs-shop-leafs";
    }

    @GetMapping("/estatisticas-de-jogos")
    public String gameStats() {
        return "stats";
    }

    @GetMapping("/site-oficial-nhl")
    public String nhlOfficialSite() {
        return "redirect:https://www.nhl.com/";
    }
}

