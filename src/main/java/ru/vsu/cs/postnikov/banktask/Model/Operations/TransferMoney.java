package ru.vsu.cs.postnikov.banktask.Model.Operations;

import ru.vsu.cs.postnikov.banktask.Services.Manager;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("transferMoney")
public class TransferMoney extends Operation {
    @Column(name = "accountNumber")
    private long accountNumberFrom;
    @Column(name = "accountNumberTo")
    private long accountNumberTo;
    @Column(name = "money")
    private BigDecimal money;

    public TransferMoney(long accountNumberFrom, long accountNumberTo, BigDecimal money) {
        this.accountNumberFrom = accountNumberFrom;
        this.accountNumberTo = accountNumberTo;
        this.money = money;
    }

    public TransferMoney() {
    }

    public long getAccountNumberFrom() {
        return accountNumberFrom;
    }
    public void setAccountNumberFrom(long accountNumberFrom) {
        this.accountNumberFrom = accountNumberFrom;
    }
    public long getAccountNumberTo() {
        return accountNumberTo;
    }
    public void setAccountNumberTo(long accountNumberTo) {
        this.accountNumberTo = accountNumberTo;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String receiveInfo() {
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
