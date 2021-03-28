package ru.vsu.cs.postnikov.banktask.Services.Operations;

import ru.vsu.cs.postnikov.banktask.Model.Operation;
import ru.vsu.cs.postnikov.banktask.Services.Manager;

import java.math.BigDecimal;

public class AddMoney extends Operation {
    private long accountNumber;
    private BigDecimal money;

    public AddMoney(long accountNumber, BigDecimal money) {
        this.accountNumber = accountNumber;
        this.money = money;
    }

    @Override
    public String initInfo() {
        return "Зачисление " + money + "$ на счет №"  + accountNumber;
    }

    @Override
    public String getSaveCode() {
        return "0/" + accountNumber + "/" + money;
    }

    @Override
    public boolean execute(Manager manager) {
        manager.addMoney(getUser().getId(), accountNumber, money);
        return true;
    }
}
