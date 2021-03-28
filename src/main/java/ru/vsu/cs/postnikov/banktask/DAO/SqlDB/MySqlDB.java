package ru.vsu.cs.postnikov.banktask.DAO.SqlDB;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import ru.vsu.cs.postnikov.banktask.DAO.IDataContainer;
import ru.vsu.cs.postnikov.banktask.Model.Account;
import ru.vsu.cs.postnikov.banktask.Model.OperationHistory;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Model.Operation;
import ru.vsu.cs.postnikov.banktask.Services.Operations.OperationFactory;
import ru.vsu.cs.postnikov.banktask.Tools.BankException;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@PropertySource("classpath:database.properties")
//@Component
public class MySqlDB implements IDataContainer {
    private Connection con;
    private Statement stmt;

    @Value("${database.connection_url}")
    private String url;
    @Value("${database.user}")
    private String user;
    @Value("${database.pass}")
    private String pass;

    public MySqlDB() {}

    @PostConstruct
    public void init(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            con = DriverManager.getConnection(url, user, pass);
//            con = DBCPDataSource.getConnection();
        } catch (SQLException | ClassNotFoundException | NoSuchMethodException
                | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User user){
        String insert = "INSERT INTO users VALUES (" +
                user.getId() + ",'" +
                user.getLogin() + "', '" +
                user.getPassword() + "')";
        sendUpdate(insert);
    }

    public void addAccount(Account account){
        String insert = "INSERT INTO accounts VALUES (" +
                account.getId() + ", " +
                account.getOwnerID() + ", " +
                account.getMoney() + ")";
        sendUpdate(insert);
    }

    public long addOperation(Operation operation){
        String insert = "INSERT INTO operations (ownerID, data) VALUES (" +
                operation.getUser().getId() + ", '" +
                operation.getSaveCode() + "')";
        sendUpdate(insert);
        return getLastId();
    }

    @Override
    public void addMoney(long userId, long accountNumber, BigDecimal money) {
        String update = "UPDATE accounts SET money = money + " + money + " WHERE id = " +
                accountNumber + " AND ownerID = " + userId;
        sendUpdate(update);
    }

    @Override
    public boolean transferMoney(long userFromId, long accountNumberFrom, long accountNumberTo, BigDecimal money) {
        if(getAccountMoney(accountNumberFrom).compareTo(money) < 0) return false;
        String transfer =
                "START TRANSACTION; " +
                "UPDATE accounts SET money = money - " + money + " WHERE id = " + accountNumberFrom + "; " +
                "UPDATE accounts SET money = money + " + money + " WHERE id = " + accountNumberTo + "; " +
                "COMMIT; ";
        sendUpdate(transfer);
        return true;
    }

    @Override
    public void deleteAccount(long accountNumber) {
        String delete = "DELETE FROM accounts WHERE id = " + accountNumber;
//                + " AND ownerID = " + userId;
        sendUpdate(delete);
    }

    private void sendUpdate(String query){
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            if(stmt != null) try { stmt.close(); } catch(SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private long getLastId(){
        String getId = "SELECT LAST_INSERT_ID();";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(getId);
            if(rs.next()) return rs.getLong(1);
            else throw new IllegalArgumentException("No last id inserted");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            if(stmt != null) try { stmt.close(); } catch(SQLException ex) {
                ex.printStackTrace();
            }
        }
        throw new IllegalArgumentException("No last id inserted");
    }

    private BigDecimal getAccountMoney(long id){
        String getId = "SELECT money " + " FROM accounts " + " WHERE id = " + id;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(getId);
            if(rs.next()) return rs.getBigDecimal(1);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            if(stmt != null) try { stmt.close(); } catch(SQLException ex) {
                ex.printStackTrace();
            }
        }
        throw new BankException("No account with id " + id);
    }

    @Override
    public List<User> getUsersList() {
        List<User> users = new ArrayList<>();
        String select = "SELECT * FROM users";
        try {
            stmt = con.createStatement();
            ResultSet rs =  stmt.executeQuery(select);
            while (rs.next()){
                users.add(new User(
                                rs.getInt("id"),
                                rs.getString("login"),
                                rs.getString("password")
                        )
                );
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            if(stmt != null) try { stmt.close(); } catch(SQLException ex) {
                ex.printStackTrace();
            }
        }
        return users;
    }

    @Override
    public User getUser(long id) {
        String select = "SELECT * FROM users WHERE id=" + id;
        try {
            stmt = con.createStatement();
            ResultSet rs =  stmt.executeQuery(select);
            if(rs.next()){
                return (new User(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            if(stmt != null) try { stmt.close(); } catch(SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

//    @Override
//    public Map<Long, User> getUsersMap() {
//        List<User> users = getUsersList();
//        Map<Long, User> userMap = new HashMap<>();
//        for (User user:users) userMap.put(user.getId(), user);
//        return userMap;
//    }

    public List<Account> getUserAccounts(long userID){
        List<Account> list = new ArrayList<>();
        String select = "SELECT * FROM accounts WHERE ownerID = " + userID;
        return getAccounts(list, select);
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> list = new ArrayList<>();
        String select = "SELECT * FROM accounts";
        return getAccounts(list, select);
    }

    @Override
    public Account getAccount(long accountNumber) {
        return null;
    }

    private List<Account> getAccounts(List<Account> list, String select) {
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            while (rs.next()){
                Account account = new Account(
                        rs.getLong("id"),
                        rs.getLong("ownerID")
                );
                account.setMoney(rs.getBigDecimal("money"));

                list.add(account);
            }
            return list;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            if(stmt != null) try { stmt.close(); } catch(SQLException ex) {
                ex.printStackTrace();
            }
        }
        throw new BankException("Can't connect to database");
    }

    public OperationHistory getOperationHistory(long userID){
        OperationHistory history = new OperationHistory();
        String select =
                "SELECT * FROM(" +
                    "SELECT operations.id, operations.ownerID, operations.data, users.login, users.password " +
                    "FROM operations LEFT OUTER JOIN users ON operations.ownerID = users.id" +
                ") AS oper WHERE oper.ownerID = " + userID + ";";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            while (rs.next()){
                Operation operation = OperationFactory.loadOperation(
                        rs.getString("data")
                );
                operation.setId(rs.getLong("id"));
                User user = new User(
                        rs.getInt("ownerID"),
                        rs.getString("login"),
                        rs.getString("password")
                );
                operation.setUser(user);
                history.addOperation(operation);
            }
            return history;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        } finally {
            if(stmt != null) try { stmt.close(); } catch(SQLException ex) {
                ex.printStackTrace();
            }
        }
        throw new BankException("Can't connect to database");
    }
}
