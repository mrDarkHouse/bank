package ru.vsu.cs.postnikov.banktask.Services.Operations;


import ru.vsu.cs.postnikov.banktask.Model.Operation;
import ru.vsu.cs.postnikov.banktask.Services.Manager;

public class CloseAccount extends Operation {
    private long accountNumber;

    public CloseAccount(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String initInfo() {
        return user.getLogin() + " закрыл счет №" + accountNumber;
    }

    @Override
    public String getSaveCode() {
        return "1/" + accountNumber;
    }

    @Override
    public boolean execute(Manager manager) {
        manager.closeAccount(user.getId(), accountNumber);
        return true;
    }
}
