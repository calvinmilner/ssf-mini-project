package vttp.ssf.mini_project.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RecipeRepository {

    @Autowired
    @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    // hset recipes username recipeString
    public void saveRecipes(String username, String recipeString) {
        // System.out.println("Saving JSON for user: " + username);
        // System.out.println("JSON Content: " + recipeString);
        template.opsForHash().put("recipes", username, recipeString);
    }

    // hget recipes username
    public String getMyRecipes(String username) {
        Object existingRecipes = template.opsForHash().get("recipes", username);
        return existingRecipes != null ? existingRecipes.toString() : null;
    }
}
