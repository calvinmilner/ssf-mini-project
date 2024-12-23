package vttp.ssf.mini_project.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class RecipeRestController {
    
    @GetMapping(path = "/something", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getSomething() {
        return ResponseEntity.ok("{}");
    }
}
