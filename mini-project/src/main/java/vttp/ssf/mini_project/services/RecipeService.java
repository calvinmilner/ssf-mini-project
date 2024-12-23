package vttp.ssf.mini_project.services;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonReader;
import vttp.ssf.mini_project.models.Information;
import vttp.ssf.mini_project.repositories.RecipeRepository;
import vttp.ssf.mini_project.utils.Constants;

@Service
public class RecipeService {

    // example of where to put API key:
    // https://api.spoonacular.com/recipes/716429/information?apiKey=YOUR-API-KEY&includeNutrition=true.

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    private RecipeRepository recipeRepo;

    public List<Information> getRandom(String number, String include, String exclude) {
        String url = UriComponentsBuilder.fromUriString(Constants.GET_RANDOM_URL).queryParam("apiKey", apiKey)
                .queryParam("number", number).queryParam("include-tags", include).queryParam("exclude-tags", exclude)
                .toUriString();
        System.out.println(url);
        RequestEntity<Void> req = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        List<Information> infoList = new LinkedList<>();
        try {
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> resp = template.exchange(req, String.class);

            // Get the payload
            String payload = resp.getBody();
            System.out.println(payload);
            infoList = Information.fromJsonToInformation(payload);
            recipeRepo.saveRecentRecipesTemp(infoList);

        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }
        return infoList;
    }

    public Information getRecentRecipe(String id, String title) {
        Object recipe = recipeRepo.getRecentRecipe(id, title);
        List<Information> info = Information.fromJsonToInformation(recipe.toString());
        Information single = info.get(0);
        return single;
    }
}
