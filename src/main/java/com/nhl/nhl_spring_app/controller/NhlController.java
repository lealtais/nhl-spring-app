package com.nhl.nhl_spring_app.controller;

import com.nhl.nhl_spring_app.service.NhlPlayersService;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class NhlController {

    private final NhlPlayersService nhlPlayersService;

    public NhlController(NhlPlayersService nhlPlayersService) {
        this.nhlPlayersService = nhlPlayersService;
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
        return "leafs-shop";
    }

    @GetMapping("/leafs-nation-shop/leafs")
    public String leafsNationLeafsPage() {
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

