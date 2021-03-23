package ru.vsu.cs.postnikov.banktask.UI.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Manager;

@Controller
@RequestMapping("/cabinet")
public class MainController{
    private Manager manager;

    @Autowired
    public MainController(Manager manager) {
        this.manager = manager;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        User user = manager.getUserById(id);
        model.addAttribute("name", user.getLogin());
        return "cabinet";
    }

}
