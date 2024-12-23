package vttp.ssf.mini_project.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp.ssf.mini_project.models.Information;
import vttp.ssf.mini_project.models.Ingredients;
import vttp.ssf.mini_project.services.RecipeService;

@Controller
@RequestMapping
public class RecipeController {
    
    @Autowired
    private RecipeService recipeServ;
    
    @GetMapping("/random")
    public String getRandom() {
        return "random";
    }
    @GetMapping("/generate")
    public String getRandomRecipe(@RequestParam MultiValueMap<String, String> form, Model model) {
        String number = form.getFirst("number");
        String include = form.getFirst("include");
        String exclude = form.getFirst("exclude");
        List<Information> infoList = recipeServ.getRandom(number, include, exclude);
        // for(int i = 0; i < infoList.size(); i++) {
            // System.out.printf("%s, %s, %s, %s\n", infoList.get(i).getTitle(),infoList.get(i).getFoodImage(),infoList.get(i).getServings(),infoList.get(i).getReadyInMinutes());
            // for(Ingredients j : infoList.get(i).getExtendedIngredients())
            // System.out.printf("URL: %s", j.getImages());
        // }
        
        model.addAttribute("infoList", infoList);
        return "generate";
    }
    @GetMapping("/saved")
    public String getRecentRecipe() {
        return "";
    }
}
