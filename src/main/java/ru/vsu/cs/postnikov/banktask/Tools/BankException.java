package ru.vsu.cs.postnikov.banktask.Tools;

public class BankException extends RuntimeException {

    public BankException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return "BankError: " +  super.getMessage();
    }
}
