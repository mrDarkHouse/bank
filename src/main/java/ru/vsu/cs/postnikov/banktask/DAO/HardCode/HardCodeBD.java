package ru.vsu.cs.postnikov.banktask.DAO.HardCode;

import org.springframework.stereotype.Component;
import ru.vsu.cs.postnikov.banktask.DAO.IDataContainer;
import ru.vsu.cs.postnikov.banktask.Model.Account;
import ru.vsu.cs.postnikov.banktask.Model.OperationHistory;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Operations.AddMoney;
import ru.vsu.cs.postnikov.banktask.Services.Operations.OpenAccount;
import ru.vsu.cs.postnikov.banktask.Model.Operation;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class HardCodeBD implements IDataContainer {
    private List<User> users;
    private List<Account> accounts;
    private List<Operation> operations;

//    private static HardCodeBD instance;
//    public static HardCodeBD getInstance(){
//        if(instance == null) instance = new HardCodeBD();
//        return instance;
//    }

    public HardCodeBD() {
        users = new ArrayList<>();
        accounts = new ArrayList<>();
        operations = new ArrayList<>();

        users.add(new User(0, "Oleg", "qwe123"));
        accounts.add(new Account(1200000, 0){{
            setMoney(new BigDecimal(100));
        }});
        accounts.add(new Account(1200001, 0){{
            setMoney(new BigDecimal(300));
        }});
        operations.add(new OpenAccount(1200000).setUser(users.get(0)));
        operations.add(new AddMoney(1200000, new BigDecimal(100)).setUser(users.get(0)));
        operations.add(new OpenAccount(1200001).setUser(users.get(0)));
        operations.add(new AddMoney(1200001, new BigDecimal(300)).setUser(users.get(0)));
    }

    @Override
    public List<User> getUsersList() {
        return users;
    }

    @Override
    public User getUser(long id) {
        for (User user:users) if(user.getId() == id) return user;
        return null;
    }

    @Override
    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public void addAccount(Account account) {
        accounts.add(account);
    }

    @Override
    public void deleteAccount(long accountNumber) {
//        accounts.removeIf(a -> a.getOwnerID() == userId && a.getId() == accountNumber);
    }

    @Override
    public List<Account> getUserAccounts(long userID) {
        return accounts.stream().filter(a -> a.getOwnerID() == userID).collect(Collectors.toList());
    }

    @Override
    public long addOperation(Operation operation) {
        long id = findOperationId();
        operations.add(operation);
        return id;
    }

    @Override
    public void addMoney(long userId, long accountNumber, BigDecimal money) {
        accounts.stream()
                .filter(acc -> acc.getId() == accountNumber)
                .findAny()
                .ifPresent(account -> account.setMoney(account.getMoney().add(money)));
    }

    @Override
    public boolean transferMoney(long userFromId, long accountNumberFrom, long accountNumberTo, BigDecimal money) {
        Account accountFrom = accounts.stream()
                .filter(acc -> acc.getId() == accountNumberFrom)
                .findAny()
                .orElse(null);
        Account accountTo = getAllAccounts().stream()
                .filter(acc -> acc.getId() == accountNumberTo)
                .findAny()
                .orElse(null);
        if(accountFrom == null || accountTo == null) return false;
        accountFrom.setMoney(accountFrom.getMoney().subtract(money));
        accountTo.setMoney(accountTo.getMoney().add(money));
        return true;
    }

    private long findOperationId(){
        List<Operation> copy = new ArrayList<>(operations);
        long id = -1;
        copy.sort(Comparator.comparingLong(Operation::getId));
        for (Operation o:copy){
            if(o.getId() - id > 1) return o.getId() - 1;
        }
        return copy.get(copy.size() - 1).getId() + 1;
    }

    public List<Account> getAllAccounts(){
        return accounts;
    }

    @Override
    public Account getAccount(long accountNumber) {
        return null;
    }

    @Override
    public OperationHistory getOperationHistory(long userID) {
        OperationHistory history = new OperationHistory();
        List<Operation> hist = operations.stream()
                .filter(operation -> operation.getUser().getId() == userID)
                .collect(Collectors.toList());
        for (Operation o:hist) history.addOperation(o);
        return history;
    }


}
