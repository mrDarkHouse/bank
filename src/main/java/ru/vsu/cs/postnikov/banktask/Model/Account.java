package ru.vsu.cs.postnikov.banktask.Model;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Objects;

@XmlRootElement(name = "account")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "accounts")
public class Account{

    @XmlElement
    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @XmlElement
    @Column(name = "money")
    private BigDecimal money;
    @Column(name = "ownerID")
    private long ownerID;

    public Account(long id, long ownerID) {
        this.id = id;
        this.money = new BigDecimal(0);
        this.ownerID = ownerID;
    }

    public Account() {
    }

    public long getId() {
        return id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
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
