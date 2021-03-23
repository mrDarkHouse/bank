package ru.vsu.cs.postnikov.banktask.Model;

import ru.vsu.cs.postnikov.banktask.Services.Operations.Operation;

import java.util.ArrayList;
import java.util.List;

public class OperationHistory {
    private List<Operation> operations;

    public OperationHistory() {
        operations = new ArrayList<>();
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void addOperation(Operation o){
        operations.add(o);
    }

}
