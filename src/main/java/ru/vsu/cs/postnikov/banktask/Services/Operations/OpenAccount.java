package ru.vsu.cs.postnikov.banktask.Services.Operations;

import ru.vsu.cs.postnikov.banktask.Model.Operation;
import ru.vsu.cs.postnikov.banktask.Services.Manager;

public class OpenAccount extends Operation {
    private long accountNumber;

    public OpenAccount(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public OpenAccount() {
        this(0);
    }

    @Override
    public String initInfo() {
        return user.getLogin() + " открыл счет №" + accountNumber;
    }

    @Override
    public String getSaveCode() {
        return "2/" + accountNumber;
    }

    @Override
    public boolean execute(Manager manager) {
        accountNumber = manager.openAccount(user.getId());
        return true;
    }
}
