package ru.vsu.cs.postnikov.banktask.UI.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Manager;
import ru.vsu.cs.postnikov.banktask.Model.Operations.RegisterUser;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    private Manager manager;

    @Autowired
    public LoginController(Manager manager) {
        this.manager = manager;
    }

    @PostMapping("/register")
    public String sendRegister(@ModelAttribute("user") User user, HttpSession session){
        if(manager.executeOperation(new RegisterUser(user.getLogin(), user.getPassword()))) {
            User newUser = manager.checkLoginCredentials(user.getLogin(), user.getPassword());
            session.setAttribute("user", newUser);
            return "redirect:/cabinet/" + newUser.getId();
        }else return "redirect:/login";
    }

    @PostMapping("/login")
    public String sendLogin(@ModelAttribute("user") User user, HttpSession session){
        User check = manager.checkLoginCredentials(user.getLogin(), user.getPassword());
        if(check == null) return "redirect:/login";
        else {
            session.setAttribute("user", check);
            return "redirect:/cabinet/" + check.getId();
        }
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("user") User user){
        return "login";
    }
}
