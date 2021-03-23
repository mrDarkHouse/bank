package ru.vsu.cs.postnikov.banktask.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.vsu.cs.postnikov.banktask.DAO.IDataContainer;
import ru.vsu.cs.postnikov.banktask.Model.Account;
import ru.vsu.cs.postnikov.banktask.Model.OperationHistory;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Operations.Operation;
import ru.vsu.cs.postnikov.banktask.Tools.BankException;
import ru.vsu.cs.postnikov.banktask.Tools.IdTool;
import ru.vsu.cs.postnikov.banktask.UI.UIConnector;

import java.math.BigDecimal;
import java.util.List;

@Component
public class Manager {
    private IDataContainer data;
//    private UIConnector connector;

    public IDataContainer getData() {
        return data;
    }
//    public UIConnector getConnector() {
//        return connector;
//    }

    @Autowired
    public Manager(@Qualifier("mySqlDB") IDataContainer data/*,
                   @Qualifier("mainController") UIConnector connector*/) {
        this.data = data;
//        this.connector = connector;
    }

    public boolean executeOperation(Operation operation){
        if(operation.execute(this)) {
            long id = data.addOperation(operation);
            operation.setId(id);
            return true;
        }
        return false;
    }

    public User getUserById(long userId){
        User user = data.getUsersMap().get(userId);
        if(user == null) throw new BankException("No user found with id " + userId);
        return user;
    }

    private Account getUserAccount(User user, long accountNumber){
        List<Account> accounts = getAccounts(user.getId());
        return accounts.stream()
                .filter(a -> a.getId() == accountNumber)
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }

    public User addUser(String name, String password){
        long id = IdTool.generateRandomUserId();
        User user = new User(id, name, password);
        data.addUser(user);
        return user;
    }

    public User checkLoginCredentials(String name, String password){
        for (User u:data.getUsersMap().values()){
            if(u.getLogin().equals(name) && u.getPassword().equals(password)) return u;
        }
        return null;
    }

    public long openAccount(long userId){
        long accountId = IdTool.generateUserAccountId();
        Account account = new Account(accountId, userId);
        data.addAccount(account);
        return accountId;
    }

    public void closeAccount(long userId, long accountNumber){
        data.deleteAccount(userId, accountNumber);
        IdTool.freeAccountId(accountNumber);
    }

    public void addMoney(long userId, long accountNumber, BigDecimal money){
        data.addMoney(userId, accountNumber, money);
    }

    public boolean transferMoney(long userFromId, long accountNumberFrom,
                                     long accountNumberTo, BigDecimal money){
        if(money.longValue() < 0) return false;
        User user = getUserById(userFromId);
        Account accountFrom = getUserAccount(user, accountNumberFrom);
        if(accountFrom.getMoney().compareTo(money) < 0) return false;
        return data.transferMoney(userFromId, accountNumberFrom, accountNumberTo, money);
    }

//    public void showHistory(long userId){
//        connector.showOperationHistory(getHistory(userId).getOperations());
//    }

    public OperationHistory getHistory(long userId){
        return data.getOperationHistory(userId);
    }

//    public void showUserAccountsInfo(long userId){
//        connector.showUserAccountsInfo(getAccounts(userId));
//    }

    public List<Account> getAccounts(long userId){
        return data.getUserAccounts(userId);
    }
}
