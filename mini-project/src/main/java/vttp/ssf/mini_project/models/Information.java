package vttp.ssf.mini_project.models;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

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
    private String instructions;

    public Information() {
    }

    public Information(String recipeId, String title, String foodImage, int servings, int readyInMinutes,
            boolean dairyFree, boolean glutenFree, boolean vegan, boolean vegetarian,
            List<Ingredients> extendedIngredients, String instructions) {
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
        this.instructions = instructions;
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

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public static List<String> fromInformationToJsonStrings(List<Information> info) {
        List<String> jsonList = new LinkedList<>();
        for (Information recipe : info) {
            JsonArrayBuilder jArr = Json.createArrayBuilder();
            for (Ingredients ingredient : recipe.getExtendedIngredients()) {
                JsonObject jIngredient = Json.createObjectBuilder()
                        .add("originalName", ingredient.getOriginalName())
                        .add("image", ingredient.getImage())
                        .build();
                jArr.add(jIngredient);
            }

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
                    .add("instructions", recipe.getInstructions())
                    .build();

            jsonList.add(jObj.toString());
            // System.out.println("Generated JSON: " + jObj.toString());
        }

        return jsonList;
    }

    public static List<Information> fromJsonToInformation(String payload) {

        List<Information> infoList = new LinkedList<>();

        try (JsonReader reader = Json.createReader(new StringReader(payload))) {
            JsonArray recipeArr = reader.readObject().getJsonArray("recipes");
            for (int i = 0; i < recipeArr.size(); i++) {
                List<Ingredients> ingredientList = new LinkedList<>();
                String id = UUID.randomUUID().toString().substring(0, 8);
                JsonArray ingredientArr = recipeArr.getJsonObject(i).getJsonArray("extendedIngredients");

                for (int j = 0; j < ingredientArr.size(); j++) {
                    JsonObject jsonIngredient = ingredientArr.getJsonObject(j);

                    String image = jsonIngredient.containsKey("image")
                            && jsonIngredient.get("image").getValueType() == JsonValue.ValueType.STRING
                                    ? jsonIngredient.getString("image")
                                    : "";
                    String originalName = jsonIngredient.containsKey("originalName")
                            && jsonIngredient.get("originalName").getValueType() == JsonValue.ValueType.STRING
                                    ? jsonIngredient.getString("originalName")
                                    : "Unknown Ingredient";

                    Ingredients ingredient = new Ingredients(Constants.GET_IMG_URL + image, originalName);
                    ingredientList.add(ingredient);
                }

                JsonObject jsonRecipe = recipeArr.getJsonObject(i);

                String title = jsonRecipe.containsKey("title")
                        && jsonRecipe.get("title").getValueType() == JsonValue.ValueType.STRING
                                ? jsonRecipe.getString("title")
                                : "Unknown Title";
                String image = jsonRecipe.containsKey("image")
                        && jsonRecipe.get("image").getValueType() == JsonValue.ValueType.STRING
                                ? jsonRecipe.getString("image")
                                : "";
                int servings = jsonRecipe.containsKey("servings")
                        && jsonRecipe.get("servings").getValueType() == JsonValue.ValueType.NUMBER
                                ? jsonRecipe.getInt("servings")
                                : 0;
                int readyInMinutes = jsonRecipe.containsKey("readyInMinutes")
                        && jsonRecipe.get("readyInMinutes").getValueType() == JsonValue.ValueType.NUMBER
                                ? jsonRecipe.getInt("readyInMinutes")
                                : 0;
                boolean dairyFree = jsonRecipe.containsKey("dairyFree")
                        && jsonRecipe.get("dairyFree").getValueType() == JsonValue.ValueType.TRUE
                                ? jsonRecipe.getBoolean("dairyFree")
                                : false;
                boolean glutenFree = jsonRecipe.containsKey("glutenFree")
                        && jsonRecipe.get("glutenFree").getValueType() == JsonValue.ValueType.TRUE
                                ? jsonRecipe.getBoolean("glutenFree")
                                : false;
                boolean vegan = jsonRecipe.containsKey("vegan")
                        && jsonRecipe.get("vegan").getValueType() == JsonValue.ValueType.TRUE
                                ? jsonRecipe.getBoolean("vegan")
                                : false;
                boolean vegetarian = jsonRecipe.containsKey("vegetarian")
                        && jsonRecipe.get("vegetarian").getValueType() == JsonValue.ValueType.TRUE
                                ? jsonRecipe.getBoolean("vegetarian")
                                : false;
                String instructions = jsonRecipe.containsKey("instructions")
                        && jsonRecipe.get("instructions").getValueType() == JsonValue.ValueType.STRING
                                ? jsonRecipe.getString("instructions")
                                : "";
                String cleanInstructions = removeHtmlTags(instructions);
                Information info = new Information(id, title, image, servings, readyInMinutes, dairyFree, glutenFree,
                        vegan, vegetarian, ingredientList, cleanInstructions);
                infoList.add(info);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return List.of();
        }
        return infoList;
    }

    public static Information fromJsonObject(JsonObject jsonRecipe) {
        String recipeId = jsonRecipe.getString("recipeId", "");
        String title = jsonRecipe.getString("title", "Unknown Title");
        String image = jsonRecipe.getString("image", "");
        int servings = jsonRecipe.getInt("servings", 0);
        int readyInMinutes = jsonRecipe.getInt("readyInMinutes", 0);
        boolean dairyFree = jsonRecipe.getBoolean("dairyFree", false);
        boolean glutenFree = jsonRecipe.getBoolean("glutenFree", false);
        boolean vegan = jsonRecipe.getBoolean("vegan", false);
        boolean vegetarian = jsonRecipe.getBoolean("vegetarian", false);
        String instructions = jsonRecipe.getString("instructions", "");

        // Parse ingredients
        List<Ingredients> ingredientList = new ArrayList<>();
        if (jsonRecipe.containsKey("extendedIngredients")) {
            JsonArray ingredientArray = jsonRecipe.getJsonArray("extendedIngredients");
            for (JsonValue ingredientValue : ingredientArray) {
                if (ingredientValue.getValueType() == JsonValue.ValueType.OBJECT) {
                    JsonObject ingredientObject = ingredientValue.asJsonObject();
                    String ingredientName = ingredientObject.getString("originalName", "Unknown Ingredient");
                    String ingredientImage = ingredientObject.getString("image", "");
                    Ingredients ingredient = new Ingredients(ingredientImage, ingredientName);
                    ingredientList.add(ingredient);
                }
            }
        }

        return new Information(recipeId, title, image, servings, readyInMinutes, dairyFree, glutenFree, vegan,
                vegetarian, ingredientList, instructions);
    }

    public static String removeHtmlTags(String input) {
        if (input == null || input.isEmpty()) {
            return input; 
        }
        return input.replaceAll("<[^>]*>", "").trim();
    }
}
