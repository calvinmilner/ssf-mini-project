package vttp.ssf.mini_project.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String getRandom(HttpSession sess, Model model) {
        Credentials credential = (Credentials) sess.getAttribute("user");
        if (credential == null) {
            model.addAttribute("status", "error");
            model.addAttribute("message", "You are not logged in or your session has expired. Please login to generate a recipe");
            return "general-message";
        }
        Object user = sess.getAttribute("user");
        model.addAttribute("user", user);
        return "random";
    }

    @GetMapping("/generate")
    public String getRandomRecipe(@RequestParam int number, @RequestParam MultiValueMap<String, String> form,
            HttpSession sess,
            Model model) {
        Credentials credential = (Credentials) sess.getAttribute("user");
        if (credential == null) {
            model.addAttribute("status", "error");
            model.addAttribute("message", "Session has ended. Please login again.");
            return "general-message";
        }
        String include = (form.getFirst("include")).toLowerCase();
        String exclude = (form.getFirst("exclude")).toLowerCase();
        try {
            List<Information> infoList = recipeServ.getRandom(number, include, exclude);
            if (infoList.size() == 0) {
                model.addAttribute("status", "generate-error");
                model.addAttribute("message", "Recipe not found");
                return "general-message";
            }
            List<String> infoStringList = Information.fromInformationToJsonStrings(infoList);
            model.addAttribute("infoList", infoList);
            model.addAttribute("infoStringList", infoStringList);
            Object user = sess.getAttribute("user");
            model.addAttribute("user", user);
            return "generate";
        } catch (Exception ex) {
            model.addAttribute("status", "generate-error");
            model.addAttribute("message", "Invalid search tags");
            return "general-message";
        }
    }

    @PostMapping("/saved")
    public String postSavedRecipes(@RequestParam(required = false) List<String> recipeIds,
            @RequestParam List<String> recipesJson,
            HttpSession sess, Model model) {
        Credentials credential = (Credentials) sess.getAttribute("user");
        if (credential == null) {
            model.addAttribute("status", "error");
            model.addAttribute("message", "Session has ended. Please login again.");
            return "general-message";
        }
        if (recipeIds == null || recipeIds.isEmpty()) {
            model.addAttribute("status", "generate-error");
            model.addAttribute("message", "No selected recipes for saving. Please try again.");
            return "general-message";
        }
        Map<String, String> recipeMap = new HashMap<>();
        for (int i = 0; i < recipeIds.size(); i++) {
            recipeMap.put(recipeIds.get(i), recipesJson.get(i));
            // System.out.println("RecipeIds: "+ recipeIds);
            // System.out.println("RecipeJson: "+ recipesJson);
        }
        recipeServ.saveRecipes(recipeMap, credential);
        Object user = sess.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("status", "saved");
        return "general-message";
    }

    @GetMapping("/my-recipes")
    public String getMyRecipes(Model model, HttpSession sess) {
        Credentials credential = (Credentials) sess.getAttribute("user");
        if (credential == null) {
            model.addAttribute("status", "error");
            model.addAttribute("message", "Session has ended. Please login again.");
            return "general-message";
        }
        List<Information> myRecipeList = recipeServ.getMyRecipes(credential);
        model.addAttribute("myRecipeList", myRecipeList);
        Object user = sess.getAttribute("user");
        model.addAttribute("user", user);
        return "my-recipes";
    }

    @GetMapping("/my-recipes/{id}")
    public String getMyRecipeById(@PathVariable String id, Model model, HttpSession sess) {
        Credentials credential = (Credentials) sess.getAttribute("user");
        if (credential == null) {
            model.addAttribute("status", "error");
            model.addAttribute("message", "Session has ended. Please login again.");
            return "general-message";
        }

        Information myRecipe = recipeServ.getMyRecipeById(credential, id);

        if (myRecipe == null) {
            model.addAttribute("status", "error");
            model.addAttribute("message", "Recipe not found");
            return "general-message";
        }

        String instructions = myRecipe.getInstructions();
        List<String> steps = Arrays.asList(instructions.split("\\.\\s*"));
        model.addAttribute("recipe", myRecipe);
        Object user = sess.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("steps", steps);
        return "my-recipe-detail";
    }

    @PostMapping("/my-recipes/delete/{id}")
    public String deleteMyRecipeById(@PathVariable String id, Model model,
            HttpSession sess) {
        Credentials credential = (Credentials) sess.getAttribute("user");
        if (credential == null) {
            model.addAttribute("status", "error");
            model.addAttribute("message", "Session has ended. Please login again.");
            return "general-message";
        }

        recipeServ.deleteMyRecipeById(credential, id);
        Object user = sess.getAttribute("user");
        model.addAttribute("user", user);
        return "redirect:/my-recipes";
    }
}
