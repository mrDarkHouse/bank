package ru.vsu.cs.postnikov.banktask.Services.Operations;


import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Manager;

public abstract class Operation {
    private long id;
    protected User user;

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }

    public Operation setUser(User user){
        this.user = user;
        return this;
    }
    public User getUser(){
        return user;
    }

    public abstract String getInfo();
    public abstract String getSaveCode();
    public abstract boolean execute(Manager manager);
}
