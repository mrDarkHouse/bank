package ru.vsu.cs.postnikov.banktask.Services.Operations;


import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Manager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "operation")
@XmlAccessorType(XmlAccessType.NONE)
public abstract class Operation{
    private long id;
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
