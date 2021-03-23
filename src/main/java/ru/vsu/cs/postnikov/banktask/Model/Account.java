package ru.vsu.cs.postnikov.banktask.Model;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private long id;
    private BigDecimal money;
    private long ownerID;

    public Account(long id, long ownerID) {
        this.id = id;
        this.money = new BigDecimal(0);
        this.ownerID = ownerID;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public long getOwnerID() {
        return ownerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", money=" + money +
                ", ownerID=" + ownerID +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
