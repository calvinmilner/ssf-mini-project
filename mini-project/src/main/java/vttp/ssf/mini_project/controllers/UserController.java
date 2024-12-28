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

    @GetMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("credentials", new Credentials());
        return "login";
    }

    @PostMapping("/enter")
    public ModelAndView postLogin(@ModelAttribute("credentials") Credentials credential, HttpSession sess) {
        ModelAndView mav = new ModelAndView();
        if (!userServ.hasUser(credential)) {
            mav.setViewName("login");
            mav.addObject("error", "Wrong username or password");
            return mav;
        }
        sess.setAttribute("user", credential);
        Object user = sess.getAttribute("user");
        mav.addObject("user", user);
        mav.setViewName("redirect:/");
        return mav;
    }

    @GetMapping("/registration")
    public String getRegistration(Model model) {
        model.addAttribute("credentials", new Credentials());
        return "registration";
    }

    @PostMapping("/registration")
    public ModelAndView postRegistration(@Valid @ModelAttribute("credentials") Credentials credentials,
            BindingResult bindings) {
        ModelAndView mav = new ModelAndView();
        if (bindings.hasErrors()) {
            mav.setViewName("registration");
            return mav;
        }
        if (userServ.hasUser(credentials)) {
            mav.setViewName("registration");
            mav.addObject("error", "Username has been registered");
        }
        userServ.saveCredentials(credentials);
        mav.addObject("status", "registered");
        mav.setViewName("general-message");
        return mav;
    }

    @GetMapping("/")
    public String getHome(Model model, HttpSession session) {
        Object user = session.getAttribute("user");
        model.addAttribute("user", user);
        return "home";
    }

    @GetMapping("/logout")
    public String clearSession(HttpSession session, Model model) {
        session.removeAttribute("user");
        session.invalidate();
        model.addAttribute("status", "loggedOut");
        return "general-message";
    }

}