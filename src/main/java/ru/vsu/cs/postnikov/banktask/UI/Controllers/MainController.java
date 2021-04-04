package ru.vsu.cs.postnikov.banktask.UI.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.postnikov.banktask.Model.Account;
import ru.vsu.cs.postnikov.banktask.Model.XMLAccountList;
import ru.vsu.cs.postnikov.banktask.Model.OperationHistory;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Manager;
import ru.vsu.cs.postnikov.banktask.Model.Operations.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;


@Controller
@RequestMapping("/cabinet")
public class MainController{
    private Manager manager;

    @Autowired
    public MainController(Manager manager) {
        this.manager = manager;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){
        User user = manager.getUserById(id);
        model.addAttribute("user", user);
        return "cabinet";
    }

    @ResponseBody
    @GetMapping("/accounts/{index}")
    public ResponseEntity<Account> getAcc(@PathVariable("index") int index, HttpSession session) {
        long userID = ((User)session.getAttribute("user")).getId();
        Account account = manager.getAccounts(userID).get(index);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/accounts")
    public XMLAccountList getAccList(HttpSession session) {
        long userID = ((User)session.getAttribute("user")).getId();
        List<Account> accounts = manager.getAccounts(userID);
        XMLAccountList list = new XMLAccountList();
        list.getAccounts().addAll(accounts);
        return list;
    }

    @ResponseBody
    @PostMapping("/accounts/new")
    public void addAcc(HttpSession session) {
        User user = (User)session.getAttribute("user");
        manager.executeOperation(new OpenAccount().setUserAndReturn(user));
    }

    @ResponseBody
    @DeleteMapping("/accounts/{id}")
    public void deleteAcc(@PathVariable("id") long accountID, HttpSession session) {
        User user = (User)session.getAttribute("user");
        manager.executeOperation(new CloseAccount(accountID).setUserAndReturn(user));
    }

    @ResponseBody
    @PostMapping("/addMoney")
    public void addMoney(HttpSession session,
                         @RequestParam("accountID") long accountID,
                         @RequestParam("money") BigDecimal money) {
        User user = ((User)session.getAttribute("user"));
        manager.executeOperation(new AddMoney(accountID, money).setUserAndReturn(user));
    }

    @ResponseBody
    @PostMapping("/transferMoney")
    public void transferMoney(HttpSession session, HttpServletResponse response,
                              @RequestParam("from") long from,
                              @RequestParam("to") long to,
                              @RequestParam("money") BigDecimal money) {
        User user = ((User)session.getAttribute("user"));
        if(!manager.executeOperation(new TransferMoney(from, to, money).setUserAndReturn(user))){
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    @ResponseBody
    @GetMapping("/operations")
    public OperationHistory getOperationHistory(HttpSession session) {
        long userID = ((User)session.getAttribute("user")).getId();
        return manager.getHistory(userID);
    }
}
