package ru.vsu.cs.postnikov.banktask.Services.Operations;

import ru.vsu.cs.postnikov.banktask.Services.Manager;

import java.math.BigDecimal;

public class TransferMoney extends Operation {
    private long accountNumberFrom;
    private long accountNumberTo;
    private BigDecimal money;

    public TransferMoney(long accountNumberFrom, long accountNumberTo, BigDecimal money) {
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.money = money;
    }

    @Override
    public String initInfo() {
        return "Перечисление " + money + "$ со счета " + accountNumberFrom + " на счет " +
                accountNumberTo;
    }

    @Override
    public String getSaveCode() {
        return "4/" + accountNumberFrom + "/" + accountNumberTo + "/" + money;
    }

    @Override
    public boolean execute(Manager manager) {
        return manager.transferMoney(user.getId(), accountNumberFrom, accountNumberTo, money);
    }
}
