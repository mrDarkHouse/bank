package ru.vsu.cs.postnikov.banktask.UI;


import org.springframework.beans.factory.annotation.Autowired;
import ru.vsu.cs.postnikov.banktask.Model.Account;
import ru.vsu.cs.postnikov.banktask.Services.Manager;
import ru.vsu.cs.postnikov.banktask.Services.Operations.Operation;

import java.util.List;

public interface UIConnector {

    void showOperationHistory(List<Operation> operations);
    void showUserAccountsInfo(List<Account> accounts);
    void show();
//    void init(Manager manager);
}
