package vttp.ssf.mini_project.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.ssf.mini_project.models.Credentials;

@Repository
public class UserRepository {
    @Autowired @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    public void saveCredentials(Credentials credentials) {
        template.opsForHash().put(credentials.getUsername(), credentials.getPassword(), credentials.getEmail());
    }

    public boolean hasUser(Credentials credential) {
        return template.opsForHash().hasKey(credential.getUsername(), credential.getPassword());
    }
}
