package ru.vsu.cs.postnikov.banktask.Services.Operations;

import ru.vsu.cs.postnikov.banktask.Model.Operation;

import java.math.BigDecimal;

public class OperationFactory {

    public static Operation loadOperation(String data){
        String[] parts = data.split("/");
        int operationType = Integer.parseInt(parts[0]);

        switch (operationType){
            case 0:return new AddMoney(Long.parseLong(parts[1]), new BigDecimal(parts[2]));
            case 1:return new CloseAccount(Long.parseLong(parts[1]));
            case 2:return new OpenAccount(Long.parseLong(parts[1]));
            case 3:return new RegisterUser(parts[1], parts[2]);
            case 4:return new TransferMoney(Long.parseLong(parts[1]), Long.parseLong(parts[2]),
                    new BigDecimal(parts[3]));
            default:throw new IllegalArgumentException("No operation with type " + operationType);
        }
    }
}
