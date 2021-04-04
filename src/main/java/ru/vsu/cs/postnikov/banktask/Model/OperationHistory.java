package ru.vsu.cs.postnikov.banktask.Model;

import ru.vsu.cs.postnikov.banktask.Model.Operations.Operation;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "operations")
public class OperationHistory{
    private List<Operation> operations;

    public OperationHistory() {
        operations = new ArrayList<>();
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public void addOperation(Operation o){
        operations.add(o);
    }

}
