package vttp.ssf.mini_project.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.ssf.mini_project.models.Credentials;

@Repository
public class UserRepository {
    @Autowired
    @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    public void saveCredentials(Credentials credentials) {
        template.opsForHash().put(credentials.getUsername(), "username", credentials.getUsername());
        template.opsForHash().put(credentials.getUsername(), "password", credentials.getPassword());
        template.opsForHash().put(credentials.getUsername(), "address", credentials.getAddress());
    }

    public boolean hasUser(Credentials credential) {
        if (template.opsForHash().hasKey(credential.getUsername(), "username"))
            return true;
        return false;
    }

    public boolean checkCredentials(Credentials credential) {
        if (template.opsForHash().hasKey(credential.getUsername(), "password")) {

            String storedPassword = (String) template.opsForHash().get(credential.getUsername(), "password");
            return storedPassword != null && storedPassword.equals(credential.getPassword());
        }
        return false;
    }
}