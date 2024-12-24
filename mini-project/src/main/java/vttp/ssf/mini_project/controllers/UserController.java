package vttp.ssf.mini_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp.ssf.mini_project.models.Credentials;
import vttp.ssf.mini_project.services.UserService;

@Controller
@RequestMapping
public class UserController {
    
    @Autowired
    private UserService userServ;
    
    @GetMapping("/")
    public String getLogin(Model model) {
        model.addAttribute("credentials", new Credentials());
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView postLogin(@Valid @ModelAttribute("credentials") Credentials credential, BindingResult bindings, HttpSession sess) {
        ModelAndView mav = new ModelAndView();
        if(!userServ.hasUser(credential)) {
            mav.setViewName("login");
            return mav;
        }
        sess.setAttribute("user", credential);
        mav.setViewName("home");
        return mav;
    }

    @GetMapping("/registration")
    public String getRegistration(Model model) {
        model.addAttribute("credentials", new Credentials());
        return "registration";
    }

    @PostMapping("/registration")
    public ModelAndView postRegistration(@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult bindings) {
        ModelAndView mav = new ModelAndView();
        if(bindings.hasErrors()) {
            mav.setViewName("registration");
            return mav;
        }
        if(userServ.hasUser(credentials)) {
            mav.setViewName("registration");
            mav.addObject("message", "Username has been registered");
        }
        userServ.saveCredentials(credentials);
        mav.setViewName("registered");
        return mav;
    }

    @GetMapping("/home")
    public String getHome() {
        return "home";
    }
    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }
}