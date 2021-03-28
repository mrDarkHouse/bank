package ru.vsu.cs.postnikov.banktask.Tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.postnikov.banktask.DAO.IDataContainer;
import ru.vsu.cs.postnikov.banktask.Model.Account;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Manager;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class IdTool {
    private static final long START_ACCOUNT_ID = 1200000;
    private static List<Long> alreadyUsedUsersIds;
    private static List<Long> alreadyUsedAccountIds;
    private Manager manager;

    @Autowired
    public IdTool(Manager manager) {
        this.manager = manager;
    }

    @PostConstruct
    public void init(){
        List<Long> ids = new ArrayList<>();
        List<Long> accountIds = new ArrayList<>();
        for (User u:manager.getUsersList()) {
            ids.add(u.getId());
        }
        for (Account a:manager.getAllAccounts()){
            accountIds.add(a.getId());
        }
        alreadyUsedUsersIds = ids;
        alreadyUsedAccountIds = accountIds;
    }

    public static void freeAccountId(long id){
        alreadyUsedAccountIds.remove(id);
    }

    public static long generateRandomUserId(){
        long id = alreadyUsedUsersIds.size() > 0 ?
                alreadyUsedUsersIds.get(alreadyUsedUsersIds.size() - 1) + 1 : 0;
        alreadyUsedUsersIds.add(id);
        return id;
    }

    public static long generateUserAccountId(){
        long id = START_ACCOUNT_ID;
        while (alreadyUsedAccountIds.contains(id)) id++;
        alreadyUsedAccountIds.add(id);
        return id;
    }
}
