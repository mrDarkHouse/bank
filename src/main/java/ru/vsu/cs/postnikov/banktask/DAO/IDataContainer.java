package ru.vsu.cs.postnikov.banktask.DAO;

import ru.vsu.cs.postnikov.banktask.Model.Account;
import ru.vsu.cs.postnikov.banktask.Model.OperationHistory;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Model.Operations.Operation;

import java.math.BigDecimal;
import java.util.List;

public interface IDataContainer {
    List<User> getUsersList();
    User getUser(long id);
    void addUser(User user);
    void addAccount(Account account);
    void addMoney(long userId, long accountNumber, BigDecimal money);
    boolean transferMoney(long userFromId, long accountNumberFrom, long accountNumberTo, BigDecimal money);
    void deleteAccount(long accountNumber);
    List<Account> getUserAccounts(long userID);
    List<Account> getAllAccounts();
    Account getAccount(long accountNumber);
    long addOperation(Operation operation);
    OperationHistory getOperationHistory(long userID);
}
