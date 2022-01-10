package com.yared.tacocloud;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.yared.tacocloud.Ingredient;
import com.yared.tacocloud.Ingredient.Type;
import com.yared.tacocloud.Taco;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
@RequiredArgsConstructor
public class DesignTacoController {
    private final IngredientRepository repository;

    @GetMapping
    public String showDesignForm(Model model) {
        Type[] types = Type.values();

        List<Ingredient> ingredients = new ArrayList<>();
        this.repository.findAll().forEach(i -> ingredients.add(i));

        for (Type t : types) {
            model.addAttribute(t.toString().toLowerCase(),
                    filterByType(ingredients, t));
        }

        return "design";
    }

    private Iterable<Ingredient> filterByType(
            List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    @ModelAttribute(name = "tacoOrder")
    public TacoOrder order() {
        return new TacoOrder();
    }

    @PostMapping
    public String processTaco(@Valid Taco taco, @ModelAttribute TacoOrder order, Errors errors) {

        if (errors.hasErrors()) {
            return "design";
        }
        order.addTaco(taco);
        return "redirect:/orders/current";
    }

}
