package vttp.ssf.mini_project.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class Credentials {
    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 5, max = 255, message = "Username must be between 5 to 255 characters")
    private String username;
    
    @NotEmpty(message = "Email cannot be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, max = 255, message = "Password must be between 5 to 255 characters")
    private String password;
    
    public Credentials() {}

    public Credentials(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
