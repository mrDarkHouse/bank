package ru.vsu.cs.postnikov.banktask.Model.Operations;

import ru.vsu.cs.postnikov.banktask.Services.Manager;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("openAccount")
public class OpenAccount extends Operation {
    @Column(name = "accountNumber")
    private long accountNumber;

    public OpenAccount(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public OpenAccount() {
        this(0);
    }

    public long getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String receiveInfo() {
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

    @Override
    public String toString() {
        return "OpenAccount{" +
                "accountNumber=" + accountNumber +
                ", id=" + id +
                ", user=" + user +
                ", info='" + info + '\'' +
                '}';
    }
}
