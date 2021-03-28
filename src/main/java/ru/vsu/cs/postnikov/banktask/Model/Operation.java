package ru.vsu.cs.postnikov.banktask.Model;


import ru.vsu.cs.postnikov.banktask.Services.Manager;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "operation")
@XmlAccessorType(XmlAccessType.NONE)
//@Entity
//@Table(name = "operations")
public abstract class Operation{
//    @Id
//    @Column(name = "id")
    private long id;
//    @Column(name = "ownerID")
    protected User user;
    @XmlElement
    protected String info;

    public Operation() { }

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }

    public User getUser(){
        return user;
    }

    public Operation setUser(User user){
        this.user = user;
        this.info = initInfo();
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getInfo(){
        return info;
    }

    public abstract String initInfo();
    public abstract String getSaveCode();
    public abstract boolean execute(Manager manager);
}
