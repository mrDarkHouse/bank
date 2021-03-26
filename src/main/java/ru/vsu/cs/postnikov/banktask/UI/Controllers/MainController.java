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
import ru.vsu.cs.postnikov.banktask.Services.Operations.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public ResponseEntity<Account> getAcc(@PathVariable("index") int index, HttpServletRequest request) {
        long userID = ((User)request.getSession().getAttribute("user")).getId();
        Account account = manager.getAccounts(userID).get(index);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/accounts")
    public XMLAccountList getAccList(HttpServletRequest request) {
        long userID = ((User)request.getSession().getAttribute("user")).getId();
        List<Account> accounts = manager.getAccounts(userID);
        XMLAccountList list = new XMLAccountList();
        list.getAccounts().addAll(accounts);
        return list;
    }

    @ResponseBody
    @PostMapping("/accounts/new")
    public void addAcc(HttpServletRequest request) {
        User user = ((User)request.getSession().getAttribute("user"));
        manager.executeOperation(new OpenAccount().setUser(user));
    }

    @ResponseBody
    @DeleteMapping("/accounts/{id}")
    public void deleteAcc(@PathVariable("id") long accountID, HttpServletRequest request) {
        User user = ((User)request.getSession().getAttribute("user"));
        manager.executeOperation(new CloseAccount(accountID).setUser(user));
    }

    @ResponseBody
    @PostMapping("/addMoney")
    public void addMoney(HttpServletRequest request) {
        User user = ((User)request.getSession().getAttribute("user"));
        long accountID = Long.parseLong(request.getParameter("accountID"));
        BigDecimal money = new BigDecimal(request.getParameter("money"));
        manager.executeOperation(new AddMoney(accountID, money).setUser(user));
    }

    @ResponseBody
    @PostMapping("/transferMoney")
    public void transferMoney(HttpServletRequest request, HttpServletResponse response) {
        User user = ((User)request.getSession().getAttribute("user"));
        long from = Long.parseLong(request.getParameter("from"));
        long to = Long.parseLong(request.getParameter("to"));
        BigDecimal money = new BigDecimal(request.getParameter("money"));
        if(!manager.executeOperation(new TransferMoney(from, to, money).setUser(user))){
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    @ResponseBody
    @GetMapping("/operations")
    public OperationHistory getOperationHistory(HttpServletRequest request) {
        long userID = ((User)request.getSession().getAttribute("user")).getId();
        return manager.getHistory(userID);
    }
}
