package ru.vsu.cs.postnikov.banktask.UI.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
    public String show(@PathVariable("id") int id, Model model){
        User user = manager.getUserById(id);
        List<Account> acc = manager.getAccounts(user.getId());
        OperationHistory hist = manager.getHistory(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("accounts", acc);
        model.addAttribute("history", hist);
        return "cabinet";
    }

    @ResponseBody
    @GetMapping("/accounts/{index}")
    public void getAcc(@PathVariable("index") int index, HttpServletRequest request, HttpServletResponse response) throws IOException {
        long userID = ((User)request.getSession().getAttribute("user")).getId();
//        int index = Integer.parseInt(request.getParameter("index"));
        Account account = manager.getAccounts(userID).get(index);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write("<accounts><account>" +
                "<id>" + account.getId() + "</id>" +
                "<money>" + account.getMoney() + "</money>" +
                "</account></accounts>");
    }

    @ResponseBody
    @GetMapping("/accounts")
    public void getAccList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long userID = ((User)request.getSession().getAttribute("user")).getId();
        List<Account> accounts = manager.getAccounts(userID);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write("<accounts>");
        for (Account a:accounts){
            response.getWriter().write("<account><id>" + a.getId() + "</id>");
            response.getWriter().write("<money>" + a.getMoney() + "</money></account>");
        }
        response.getWriter().write("</accounts>");
    }

    @ResponseBody
    @PostMapping("/accounts/new")
    public void addAcc(HttpServletRequest request) {
        User user = ((User)request.getSession().getAttribute("user"));
        manager.executeOperation(new OpenAccount().setUser(user));
    }

    @ResponseBody
    @DeleteMapping("/accounts/{id}")
    public void deleteAcc(@PathVariable("id") int accountID, HttpServletRequest request) {
        User user = ((User)request.getSession().getAttribute("user"));
//        long accountID = Long.parseLong(request.getParameter("accountID"));
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
    public void getOperationHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long userID = ((User)request.getSession().getAttribute("user")).getId();
        List<Operation> operations = manager.getHistory(userID).getOperations();

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write("<operations>");
        for (Operation o:operations){
            response.getWriter().write("<operation><info>" + o.getInfo() + "</info></operation>");
        }
        response.getWriter().write("</operations>");
    }

//    @ResponseBody
//    @GetMapping("/getAllAccountsInfo")
//    public void getAllAccountsInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        long userID = ((User)request.getSession().getAttribute("user")).getId();
//        List<Account> accounts = manager.getAccounts(userID);
//
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/xml");
//        response.setHeader("Cache-Control", "no-cache");
//        response.getWriter().write("<accounts>");
//        for (Account a:accounts){
//            response.getWriter().write("<account><id>" + a.getId() + "</id>");
//            response.getWriter().write("<money>" + a.getMoney() + "</money></account>");
//        }
//        response.getWriter().write("</accounts>");
//    }
}
