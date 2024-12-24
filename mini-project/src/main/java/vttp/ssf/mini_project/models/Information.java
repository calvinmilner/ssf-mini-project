package vttp.ssf.mini_project.models;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.ssf.mini_project.utils.Constants;

public class Information {
    private String recipeId;
    private String title;
    private String foodImage;
    private int servings;
    private int readyInMinutes;
    private boolean dairyFree;
    private boolean glutenFree;
    private boolean vegan;
    private boolean vegetarian;
    private List<Ingredients> extendedIngredients = new LinkedList<>();

    public Information() {
    }

    public Information(String recipeId, String title, String foodImage, int servings, int readyInMinutes,
            boolean dairyFree, boolean glutenFree, boolean vegan, boolean vegetarian,
            List<Ingredients> extendedIngredients) {
        this.recipeId = recipeId;
        this.title = title;
        this.foodImage = foodImage;
        this.servings = servings;
        this.readyInMinutes = readyInMinutes;
        this.dairyFree = dairyFree;
        this.glutenFree = glutenFree;
        this.vegan = vegan;
        this.vegetarian = vegetarian;
        this.extendedIngredients = extendedIngredients;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getReadyInMinutes() {
        return readyInMinutes;
    }

    public void setReadyInMinutes(int readyInMinutes) {
        this.readyInMinutes = readyInMinutes;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public void setDairyFree(boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public List<Ingredients> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<Ingredients> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    public static List<String> fromInformationToJsonStrings(List<Information> info) {
        List<String> jsonList = new LinkedList<>();
        for (Information recipe : info) {
            // Create a new JsonArrayBuilder for each recipe
            JsonArrayBuilder jArr = Json.createArrayBuilder();
            for (Ingredients ingredient : recipe.getExtendedIngredients()) {
                JsonObject jIngredient = Json.createObjectBuilder()
                        .add("originalName", ingredient.getOriginalName())
                        .add("image", ingredient.getImage())
                        .build();
                jArr.add(jIngredient);
            }
    
            // Build the recipe JSON object
            JsonObject jObj = Json.createObjectBuilder()
                    .add("recipeId", recipe.getRecipeId())
                    .add("title", recipe.getTitle())
                    .add("image", recipe.getFoodImage())
                    .add("servings", recipe.getServings())
                    .add("readyInMinutes", recipe.getReadyInMinutes())
                    .add("dairyFree", recipe.isDairyFree())
                    .add("glutenFree", recipe.isGlutenFree())
                    .add("vegan", recipe.isVegan())
                    .add("vegetarian", recipe.isVegetarian())
                    .add("extendedIngredients", jArr.build())
                    .build();
    
            // Add the serialized JSON string to the list
            jsonList.add(jObj.toString());
        }
    
        return jsonList;
        // JsonArrayBuilder jArr = Json.createArrayBuilder();
        // for (int i = 0; i < info.size(); i++) {
        //     for(Ingredients ingredient : info.get(i).getExtendedIngredients()) {
        //         JsonObject jIngredient = Json.createObjectBuilder().add("originalName", ingredient.getOriginalName()).add("image",ingredient.getImage()).build();
        //         jArr.add(jIngredient);
        //     }
        //     JsonObject jObj = Json.createObjectBuilder().add("recipeId", info.get(i).getRecipeId())
        //             .add("title", info.get(i).getTitle()).add("image", info.get(i).getFoodImage())
        //             .add("servings", info.get(i).getServings()).add("readyInMinutes", info.get(i).getReadyInMinutes())
        //             .add("dairyFree", info.get(i).isDairyFree()).add("glutenFree", info.get(i).isGlutenFree())
        //             .add("vegan", info.get(i).isVegan()).add("vegetarian", info.get(i).isVegetarian()).add("extendedIngredients", jArr.build()).build();
        //             jsonList.add(jObj.toString());
        // }
        // return jsonList;
    }

    public static List<Information> fromJsonToInformation(String payload) {
        List<Ingredients> ingredientList = new LinkedList<>();
        List<Information> infoList = new LinkedList<>();

        try (JsonReader reader = Json.createReader(new StringReader(payload))) {
            JsonArray recipeArr = reader.readObject().getJsonArray("recipes");
            for (int i = 0; i < recipeArr.size(); i++) {
                String id = UUID.randomUUID().toString().substring(0, 8);
                JsonArray ingredientArr = recipeArr.getJsonObject(i).getJsonArray("extendedIngredients");
                for (int j = 0; j < ingredientArr.size(); j++) {
                    JsonObject jsonIngredient = ingredientArr.getJsonObject(j);
                    Ingredients ingredient = new Ingredients(Constants.GET_IMG_URL + jsonIngredient.getString("image"),
                            jsonIngredient.getString("originalName"));
                    ingredientList.add(ingredient);
                }
                JsonObject jsonRecipe = recipeArr.getJsonObject(i);
                Information info = new Information(id, jsonRecipe.getString("title"), jsonRecipe.getString("image"),
                        jsonRecipe.getInt("servings"), jsonRecipe.getInt("readyInMinutes"),
                        jsonRecipe.getBoolean("dairyFree"), jsonRecipe.getBoolean("glutenFree"),
                        jsonRecipe.getBoolean("vegan"), jsonRecipe.getBoolean("vegetarian"), ingredientList);
                infoList.add(info);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }
        return infoList;
    }
}
