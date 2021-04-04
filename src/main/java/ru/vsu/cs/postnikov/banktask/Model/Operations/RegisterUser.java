package ru.vsu.cs.postnikov.banktask.Model.Operations;

import ru.vsu.cs.postnikov.banktask.Services.Manager;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("registerUser")
public class RegisterUser extends Operation {
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    public RegisterUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RegisterUser() {
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String receiveInfo() {
        return "Регистрация пользователя " + username;
    }

    @Override
    public String getSaveCode() {
        return "3/" + username + "/" + password;
    }

    @Override
    public boolean execute(Manager manager) {
        user = manager.addUser(username, password);
        return true;
    }

    @Override
    public String toString() {
        return "RegisterUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", id=" + id +
                ", user=" + user +
                ", info='" + info + '\'' +
                '}';
    }
}
