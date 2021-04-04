package ru.vsu.cs.postnikov.banktask.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.postnikov.banktask.DAO.IDataContainer;
import ru.vsu.cs.postnikov.banktask.Model.Account;
import ru.vsu.cs.postnikov.banktask.Model.OperationHistory;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Model.Operations.Operation;
import ru.vsu.cs.postnikov.banktask.Tools.BankException;
import ru.vsu.cs.postnikov.banktask.Tools.IdTool;

import java.math.BigDecimal;
import java.util.List;

@Service
public class Manager {
    private IDataContainer data;

    @Autowired
    public Manager(@Qualifier("hibernateRepository") IDataContainer data) {
        this.data = data;
    }

    @Transactional
    public List<User> getUsersList(){
        return data.getUsersList();
    }

    @Transactional
    public User getUserById(long userId){
        User user = data.getUser(userId);
        if(user == null) throw new BankException("No user found with id " + userId);
        return user;
    }

    @Transactional
    public User addUser(String name, String password){
        long id = IdTool.generateRandomUserId();
        User user = new User(id, name, password);
        data.addUser(user);
        return user;
    }

    @Transactional
    public User checkLoginCredentials(String name, String password){
        for (User u:data.getUsersList()){
            if(u.getLogin().equals(name) && u.getPassword().equals(password)) return u;
        }
        return null;
    }

    private Account getUserAccount(User user, long accountNumber){
//        if(data.getUserAccounts(user.getId()).stream().anyMatch(account -> account.getId() == accountNumber)) {
            return data.getAccount(accountNumber);
//        }
//        throw new IllegalArgumentException();
//        List<Account> accounts = getAccounts(user.getId());
//        return accounts.stream()
//                .filter(a -> a.getId() == accountNumber)
//                .findAny()
//                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional
    public List<Account> getAccounts(long userId){
        return data.getUserAccounts(userId);
    }

    @Transactional
    public List<Account> getAllAccounts(){
        return data.getAllAccounts();
    }

    @Transactional
    public long openAccount(long userId){
        long accountId = IdTool.generateUserAccountId();
        Account account = new Account(accountId, userId);
        data.addAccount(account);
        return accountId;
    }

    @Transactional
    public void closeAccount(long userId, long accountNumber){
        if(data.getUserAccounts(userId).stream().anyMatch(account -> account.getId() == accountNumber)){
            data.deleteAccount(accountNumber);
            IdTool.freeAccountId(accountNumber);
        }
    }

    @Transactional
    public void addMoney(long userId, long accountNumber, BigDecimal money){
        data.addMoney(userId, accountNumber, money);
    }

    @Transactional
    public boolean transferMoney(long userFromId, long accountNumberFrom,
                                     long accountNumberTo, BigDecimal money){
        if(money.longValue() < 0) return false;
        User user = getUserById(userFromId);
        Account accountFrom = getUserAccount(user, accountNumberFrom);
        if(accountFrom.getMoney().compareTo(money) < 0) return false;
        return data.transferMoney(userFromId, accountNumberFrom, accountNumberTo, money);
    }

    @Transactional
    public boolean executeOperation(Operation operation){
        if(operation.execute(this)) {
            long id = data.addOperation(operation);
            operation.setId(id);
            return true;
        }
        return false;
    }

    @Transactional
    public OperationHistory getHistory(long userId){
        return data.getOperationHistory(userId);
    }

}
