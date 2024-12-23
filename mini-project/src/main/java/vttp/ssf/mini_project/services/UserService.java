package vttp.ssf.mini_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.ssf.mini_project.models.Credentials;
import vttp.ssf.mini_project.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public void saveCredentials(Credentials credentials) {
        userRepo.saveCredentials(credentials);
    }
    public boolean hasUser(Credentials credential) {
        return userRepo.hasUser(credential);
    }
}
