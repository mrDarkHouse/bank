package ru.vsu.cs.postnikov.banktask.Model.Operations;

import ru.vsu.cs.postnikov.banktask.Services.Manager;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@DiscriminatorValue("addMoney")
public class AddMoney extends Operation {
    @Column(name = "accountNumber")
    private long accountNumber;
    @Column(name = "money")
    private BigDecimal money;

    public AddMoney(long accountNumber, BigDecimal money) {
        this.accountNumber = accountNumber;
        this.money = money;
    }

    public AddMoney() {
    }

    public long getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String receiveInfo() {
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

    @Override
    public String toString() {
        return "AddMoney{" +
                "id " + id +
                ", accountNumber=" + accountNumber +
                ", money=" + money +
                ", user=" + user +
                ", info='" + info + '\'' +
                '}';
    }
}
