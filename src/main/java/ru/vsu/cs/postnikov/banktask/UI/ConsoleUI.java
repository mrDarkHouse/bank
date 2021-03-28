package ru.vsu.cs.postnikov.banktask.UI;

import ru.vsu.cs.postnikov.banktask.Model.Account;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Manager;
import ru.vsu.cs.postnikov.banktask.Model.Operation;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI implements UIConnector {
    private Scanner sc;
    private Manager manager;
    private HashMap<String, Operation> commands;

    public ConsoleUI() {
        sc = new Scanner(System.in);
        initCommands();
//        listen();
    }

    private void initCommands(){
        commands = new HashMap<>();
//        commands.put("addUser", Manager::addUser);
//        commands.put("openAccount", new OpenAccount());
    }

    private void listen(){
        System.out.println("enter command");
        do {
            String n = sc.next();
            try {
                int num = Integer.parseInt(n);
                switch (num){
                    case 0: //
                        break;
                    case 1: //
                        break;
                }

            }catch (NumberFormatException e){
                System.out.println("error");
                System.out.println("enter command");
            }

        }while (sc.hasNext());

//        while (sc.hasNext()){
//
//        }
    }

    private void processInput(String input){
        for (String s:commands.keySet()){
            if(input.contains(s)){
//                commands.get(s).process();
//                String out = commands.get(s).process();
//                if(!out.isEmpty()) System.out.println(out);
            }
        }
    }


//    @Override
//    public void showBalance(long balance) {
//        System.out.println("Баланс: " + balance);
//    }

    @Override
    public void showOperationHistory(List<Operation> operations) {
        StringBuilder s = new StringBuilder();
        for (Operation operation:operations){
            s.append(System.lineSeparator());
            s.append(operation.getInfo());
        }
        System.out.println(s);
    }

    @Override
    public void showUserAccountsInfo(List<Account> accounts) {
        StringBuilder s = new StringBuilder();
        for (Account account:accounts){
            User user = manager.getUserById(account.getOwnerID());
            s.append("Владелец: ").append(user.getLogin()).append(System.lineSeparator());
            s.append("Баланс счета:").append(account.getMoney()).append(System.lineSeparator());
        }
        System.out.println(s);
    }

    @Override
    public void show() {
        listen();
    }

    //    @Override
    public void init(Manager manager) {
        this.manager = manager;
    }
}
