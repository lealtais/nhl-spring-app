package com.nhl.nhl_spring_app.controller;

import com.nhl.nhl_spring_app.model.ShopItem;
import com.nhl.nhl_spring_app.repository.ShopItemRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final ShopItemRepository shopItemRepository;

    public CartController(ShopItemRepository shopItemRepository) {
        this.shopItemRepository = shopItemRepository;
    }

    @SuppressWarnings("unchecked")
    private List<ShopItem> getCart(HttpSession session) {
        List<ShopItem> cart = (List<ShopItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        List<ShopItem> cart = getCart(session);
        BigDecimal total = cart.stream()
                .map(ShopItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@RequestParam("itemId") Long itemId, HttpSession session) {
        System.out.println("DEBUG: Adicionando item: " + itemId);
        Optional<ShopItem> itemOpt = shopItemRepository.findById(itemId);
        if (itemOpt.isPresent()) {
            List<ShopItem> cart = getCart(session);
            cart.add(itemOpt.get());
            System.out.println("DEBUG: Item adicionado com sucesso!");
            return "success";
        }
        System.out.println("DEBUG: Item nao encontrado: " + itemId);
        return "error";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("index") int index, HttpSession session) {
        List<ShopItem> cart = getCart(session);
        if (index >= 0 && index < cart.size()) {
            cart.remove(index);
        }
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart(HttpSession session) {
        session.removeAttribute("cart");
        return "redirect:/cart";
    }
}
