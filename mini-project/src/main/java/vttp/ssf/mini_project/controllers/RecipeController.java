package vttp.ssf.mini_project.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import vttp.ssf.mini_project.models.Credentials;
import vttp.ssf.mini_project.models.Information;
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
        String number = String.valueOf(form.getFirst("number"));
        String include = form.getFirst("include");
        String exclude = form.getFirst("exclude");
        List<Information> infoList = recipeServ.getRandom(number, include, exclude);
        List<String> infoStringList = Information.fromInformationToJsonStrings(infoList);
        model.addAttribute("infoList", infoList);
        model.addAttribute("infoStringList", infoStringList);
        return "generate";
    }

    @PostMapping("/saved")
    public String postSavedRecipe(@RequestParam List<String> recipeIds, @RequestParam List<String> recipesJson, HttpSession sess) {
        Map<String, String> recipeMap = new HashMap<>();
        for (int i = 0; i < recipeIds.size(); i++) {
            recipeMap.put(recipeIds.get(i), recipesJson.get(i));
        }
        Credentials credential = (Credentials) sess.getAttribute("user");
        recipeServ.saveRecipes(recipeMap, credential);
        return "success";
    }

    @GetMapping("/my-recipes")
    public String getMyRecipes(Model model) {
        return "my-recipes";
    }
}
