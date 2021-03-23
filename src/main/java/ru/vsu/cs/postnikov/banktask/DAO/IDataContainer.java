package ru.vsu.cs.postnikov.banktask.DAO;

import ru.vsu.cs.postnikov.banktask.Model.Account;
import ru.vsu.cs.postnikov.banktask.Model.OperationHistory;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Operations.Operation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IDataContainer {
    List<User> getUsersList();
    Map<Long, User> getUsersMap();
    void addUser(User user);
    void addAccount(Account account);
    long addOperation(Operation operation);
    void addMoney(long userId, long accountNumber, BigDecimal money);
    boolean transferMoney(long userFromId, long accountNumberFrom, long accountNumberTo, BigDecimal money);
    void deleteAccount(long userId, long accountNumber);
    List<Account> getUserAccounts(long userID);
    List<Account> getAllAccounts();
    OperationHistory getOperationHistory(long userID);
}
