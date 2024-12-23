package vttp.ssf.mini_project.repositories;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.ssf.mini_project.models.Information;

@Repository
public class RecipeRepository {
    
    @Autowired @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    public void saveRecentRecipesTemp(List<Information> info) {
        List<String> jsonList = Information.fromInformationToJsonStrings(info);
        for(int i = 0; i < jsonList.size(); i++) {
            template.opsForHash().put(info.get(i).getRecipeId(), info.get(i).getTitle(), jsonList.get(i));
            template.expire(info.get(i).getRecipeId(), 2, TimeUnit.MINUTES);
        }
    }
    // hget id title
    public Object getRecentRecipe(String id, String title) {
        return template.opsForHash().get(id, title);
    }
}
