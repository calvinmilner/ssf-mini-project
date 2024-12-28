package vttp.ssf.mini_project.bootstrap;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class AppBootStrap implements CommandLineRunner {

    // @Value("classpath:/ingredients.txt")
    // Resource ingredients;

    @Autowired @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    @Override
    public void run(String... args) throws IOException {
        
        // Generate some default accounts for use
        template.opsForHash().put("reference96", "Password123", "reference96@gmail.com");
        template.opsForHash().put("default", "Password123", "default@gmail.com");
    }
}
