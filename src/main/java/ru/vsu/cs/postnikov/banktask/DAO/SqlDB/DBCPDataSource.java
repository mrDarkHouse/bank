package ru.vsu.cs.postnikov.banktask.DAO.SqlDB;//package ru.vsu.cs.postnikov.banktask.DAO.SqlDB;
//
//import org.apache.commons.dbcp.BasicDataSource;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//public class DBCPDataSource {
//    private static final String url = "jdbc:mysql://localhost:3306/bank_task" +
//            "?serverTimezone=UTC&autoReconnect=true&allowMultiQueries=true";
//    private static final String user = "bank_user";
//    private static final String password = "qwe123";
//
//    private static BasicDataSource ds = new BasicDataSource();
//
//    static {
//        ds.setUrl(url);
//        ds.setUsername(user);
//        ds.setPassword(password);
//        ds.setMinIdle(5);
//        ds.setMaxIdle(10);
//        ds.setMaxOpenPreparedStatements(100);
//    }
//
//    public static Connection getConnection() throws SQLException {
//        return ds.getConnection();
//    }
//
//    private DBCPDataSource(){}
//
//}
