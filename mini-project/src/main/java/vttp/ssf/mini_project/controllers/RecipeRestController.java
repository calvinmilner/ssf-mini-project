package vttp.ssf.mini_project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import vttp.ssf.mini_project.models.Credentials;
import vttp.ssf.mini_project.models.Information;
import vttp.ssf.mini_project.services.RecipeService;

@RestController
@RequestMapping
public class RecipeRestController {

    @Autowired
    private RecipeService recipeServ;

    @GetMapping(path = "/api/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRandomRecipeJson(
            @RequestParam int number,
            @RequestParam MultiValueMap<String, String> form, HttpSession sess) {

        Credentials credential = (Credentials) sess.getAttribute("user");
        if (credential == null) {
            String errorJson = """
                    {
                        "status": "error",
                        "message": "Session has ended. Please login again."
                    }
                    """;
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorJson);
        }

        String include = form.getFirst("include");
        String exclude = form.getFirst("exclude");
        try {
            // Fetch the list of Information objects
            List<Information> infoList = recipeServ.getRandom(number, include, exclude);

            // Check if the list is empty
            if (infoList.isEmpty()) {
                // Return a JSON error response as a String
                String errorJson = """
                        {
                            "status": "error",
                            "message": "No results found"
                        }
                        """;
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorJson);
            }

            // Convert the list of Information objects to a JSON string
            String jsonResponse = Information.fromInformationToJsonStrings(infoList).toString();

            // Return the JSON string as the response body
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception ex) {
            // Return a JSON error response as a String
            String errorJson = """
                    {
                        "status": "error",
                        "message": "An error occurred while fetching data"
                    }
                    """;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorJson);
        }
    }
}
