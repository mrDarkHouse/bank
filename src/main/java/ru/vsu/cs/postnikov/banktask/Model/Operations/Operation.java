package ru.vsu.cs.postnikov.banktask.Model.Operations;


import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Manager;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "operation")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "operations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "bd_type")
public abstract class Operation{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    @ManyToOne
    @JoinColumn(name = "ownerID")
    protected User user;
    @XmlElement
    @Transient
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
    public void setUser(User user) {
        this.user = user;
    }

    public Operation setUserAndReturn(User user){
        this.user = user;
        initInfo();
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    public String getInfo(){
        if(info == null) info = receiveInfo();
        return info;
    }

    @PostLoad
    private void initInfo(){
        this.info = receiveInfo();
    }

    public abstract String receiveInfo();
    public abstract String getSaveCode();
    public abstract boolean execute(Manager manager);
}
