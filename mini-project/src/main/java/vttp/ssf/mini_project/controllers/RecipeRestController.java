package vttp.ssf.mini_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vttp.ssf.mini_project.services.RecipeService;

@RestController
@RequestMapping
public class RecipeRestController {
    
    @Autowired
    private RecipeService recipeServ;

    @GetMapping(path = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRandomRecipeJson(@RequestParam int number, @RequestParam MultiValueMap<String,String> form) {
        // String number = String.valueOf(form.getFirst("number"));
        String include = form.getFirst("include");
        String exclude = form.getFirst("exclude");
        return recipeServ.getRandomRecipeJson(number, include, exclude);
    }
}
