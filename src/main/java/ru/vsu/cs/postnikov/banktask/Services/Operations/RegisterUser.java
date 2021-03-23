package ru.vsu.cs.postnikov.banktask.Services.Operations;

import ru.vsu.cs.postnikov.banktask.Services.Manager;

public class RegisterUser extends Operation {
    private String userName;
    private String password;

    public RegisterUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String getInfo() {
        return "Регистрация пользователя " + userName;
    }

    @Override
    public String getSaveCode() {
        return "3/" + userName + "/" + password;
    }

    @Override
    public boolean execute(Manager manager) {
        user = manager.addUser(userName, password);
        return true;
    }
}
