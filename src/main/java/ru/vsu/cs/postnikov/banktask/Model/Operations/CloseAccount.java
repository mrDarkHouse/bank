package ru.vsu.cs.postnikov.banktask.Model.Operations;


import ru.vsu.cs.postnikov.banktask.Services.Manager;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("closeAccount")
public class CloseAccount extends Operation {
    @Column(name = "accountNumber")
    private long accountNumber;

    public CloseAccount(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public CloseAccount() {
    }

    public long getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String receiveInfo() {
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
