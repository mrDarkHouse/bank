package ru.vsu.cs.postnikov.banktask.UI.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Manager;
import ru.vsu.cs.postnikov.banktask.Services.Operations.RegisterUser;

@Controller
public class LoginController {
    private Manager manager;

    @Autowired
    public LoginController(Manager manager) {
        this.manager = manager;
    }

    @PostMapping("/register")
    public String sendRegister(@ModelAttribute("user") User user){
        if(manager.executeOperation(new RegisterUser(user.getLogin(), user.getPassword()))) {
            User newUser = manager.checkLoginCredentials(user.getLogin(), user.getPassword());
            return "redirect:/cabinet/" + newUser.getId();
        }else return "redirect:/login";
    }

    @PostMapping("/login")
    public String sendLogin(@ModelAttribute("user") User user){
        User check = manager.checkLoginCredentials(user.getLogin(), user.getPassword());
        if(check == null) return "redirect:/login";
        else return "redirect:/cabinet/" + check.getId();
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("user") User user){
        return "login";
    }
}
