package vttp.ssf.mini_project.services;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.ssf.mini_project.models.Credentials;
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

    public List<Information> getRandom(int number, String include, String exclude) throws Exception {
        List<Information> combinedResults = new LinkedList<>();
        int remaining = number;
        int batchSize = 10;
        while (remaining > 0) {
            int fetchSize = Math.min(batchSize, remaining);
            String url = UriComponentsBuilder.fromUriString(Constants.GET_RANDOM_URL)
                    .queryParam("apiKey", apiKey)
                    .queryParam("number", fetchSize)
                    .queryParam("include-tags", include)
                    .queryParam("exclude-tags", exclude)
                    .toUriString();

            RequestEntity<Void> req = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
            try {
                RestTemplate template = new RestTemplate();
                ResponseEntity<String> resp = template.exchange(req, String.class);
                List<Information> batchResults = Information.fromJsonToInformation(resp.getBody());
                combinedResults.addAll(batchResults);
                remaining -= fetchSize;
            } catch (Exception ex) {
                throw new Exception("Error in fetching information from API", ex);
            }
        }
        return combinedResults;
    }

    public void saveRecipes(Map<String, String> recipeMap, Credentials credential) {
        String existingRecipesJson = recipeRepo.getMyRecipes(credential.getUsername());

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        // Add existing recipes to the array if they exist
        if (existingRecipesJson != null && !existingRecipesJson.isEmpty()) {
            try (JsonReader reader = Json.createReader(new StringReader(existingRecipesJson))) {
                JsonArray existingRecipes = reader.readArray();
                existingRecipes.forEach(arrayBuilder::add);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        recipeMap.forEach((recipeId, recipeJson) -> {

            // System.out.println("Processing recipe: " + recipeJson);

            try (JsonReader reader = Json.createReader(new StringReader(recipeJson))) {
                JsonValue recipeValue = reader.readValue();

                if (recipeValue.getValueType() == JsonValue.ValueType.OBJECT) {
                    JsonArray singleRecipeArray = Json.createArrayBuilder().add((JsonObject) recipeValue).build();
                    singleRecipeArray.forEach(arrayBuilder::add);
                } else if (recipeValue.getValueType() == JsonValue.ValueType.ARRAY) {
                    JsonArray recipeArray = (JsonArray) recipeValue;
                    recipeArray.forEach(arrayBuilder::add);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        String updatedRecipesJson = arrayBuilder.build().toString();
        // System.out.println(updatedRecipesJson);
        recipeRepo.saveRecipes(credential.getUsername(), updatedRecipesJson);
    }

    public List<Information> getMyRecipes(Credentials credential) {
        String recipesJson = recipeRepo.getMyRecipes(credential.getUsername());

        List<Information> recipeList = new LinkedList<>();

        if (recipesJson != null) {
            try (JsonReader reader = Json.createReader(new StringReader(recipesJson.toString()))) {
                JsonArray recipeArray = reader.readArray();

                for (JsonValue value : recipeArray) {
                    if (value.getValueType() == JsonValue.ValueType.OBJECT) {
                        JsonObject recipeObject = value.asJsonObject();

                        Information recipe = Information.fromJsonObject(recipeObject);
                        recipeList.add(recipe);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return recipeList;
    }

    public Information getMyRecipeById(Credentials credential, String id) {
        String recipesJson = recipeRepo.getMyRecipes(credential.getUsername());


        List<Information> recipeList = new LinkedList<>();

        if (recipesJson != null) {
            try (JsonReader reader = Json.createReader(new StringReader(recipesJson))) {

                JsonArray recipeArray = reader.readArray();

                for (JsonValue value : recipeArray) {
                    if (value.getValueType() == JsonValue.ValueType.OBJECT) {
                        JsonObject recipeObject = value.asJsonObject();

                        Information recipe = Information.fromJsonObject(recipeObject);
                        recipeList.add(recipe);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Information info : recipeList) {
            // System.out.println("Checking Recipe ID: " + info.getRecipeId());
            if (info.getRecipeId().equals(id)) {
                return info;
            }
        }
        return null;
    }

    public void deleteMyRecipeById(Credentials credential, String recipeId) {
        String recipesJson = recipeRepo.getMyRecipes(credential.getUsername());

        List<Information> recipeList = new LinkedList<>();

        if (recipesJson != null) {
            try (JsonReader reader = Json.createReader(new StringReader(recipesJson))) {

                JsonArray recipeArray = reader.readArray();

                for (JsonValue value : recipeArray) {
                    if (value.getValueType() == JsonValue.ValueType.OBJECT) {
                        JsonObject recipeObject = value.asJsonObject();


                        Information recipe = Information.fromJsonObject(recipeObject);
                        recipeList.add(recipe);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            recipeList.removeIf(recipe -> recipe.getRecipeId().equals(recipeId));

            List<String> recipeJsonStrings = Information.fromInformationToJsonStrings(recipeList);

            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            for (String jsonString : recipeJsonStrings) {
                JsonObject jsonObject = Json.createReader(new StringReader(jsonString)).readObject();
                jsonArrayBuilder.add(jsonObject);
            }
            String updatedRecipeString = jsonArrayBuilder.build().toString();

            recipeRepo.saveRecipes(credential.getUsername(), updatedRecipeString);
        }
    }

    public ResponseEntity<String> getRandomRecipeJson(int number, String include, String exclude) {
        String url = UriComponentsBuilder.fromUriString(Constants.GET_RANDOM_URL).queryParam("apiKey",
                apiKey)
                .queryParam("number", number).queryParam("include-tags",
                        include)
                .queryParam("exclude-tags", exclude)
                .toUriString();
        System.out.println(url);
        RequestEntity<Void> req = RequestEntity.get(url).accept(MediaType.APPLICATION_JSON).build();
        try {
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> resp = template.exchange(req, String.class);
            return ResponseEntity.ok(resp.getBody());

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("{}");
        }
    }
}
