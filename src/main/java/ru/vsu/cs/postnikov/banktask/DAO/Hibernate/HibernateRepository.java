package ru.vsu.cs.postnikov.banktask.DAO.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.postnikov.banktask.DAO.IDataContainer;
import ru.vsu.cs.postnikov.banktask.Model.Account;
import ru.vsu.cs.postnikov.banktask.Model.Operation;
import ru.vsu.cs.postnikov.banktask.Model.OperationHistory;
import ru.vsu.cs.postnikov.banktask.Model.User;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class HibernateRepository implements IDataContainer {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> getUsersList() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from User").list();
    }

    @Override
    public User getUser(long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    @Override
    public void addUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
    }

    @Override
    public void addAccount(Account account) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(account);
    }

    @Override
    public void addMoney(long userId, long accountNumber, BigDecimal money) {
        Session session = sessionFactory.getCurrentSession();
        Account account = getAccount(accountNumber);
        account.setMoney(account.getMoney().add(money));
        session.update(account);
    }

    @Override
    public boolean transferMoney(long userFromId, long accountNumberFrom,
                                 long accountNumberTo, BigDecimal money) {
        Account from = getAccount(accountNumberFrom);
        Account to = getAccount(accountNumberTo);
        from.setMoney(from.getMoney().subtract(money));
        to.setMoney(to.getMoney().add(money));
        return true;
    }

    @Override
    public void deleteAccount(long accountNumber) {
        Session session = sessionFactory.getCurrentSession();
        Account account = getAccount(accountNumber);
        session.delete(account);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Account> getUserAccounts(long userID) {
        Session session = sessionFactory.getCurrentSession();
        Query<Account> query;
        query = session.createQuery("from Account where ownerID = :userID");
        query.setParameter("userID", userID);
        return query.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Account> getAllAccounts() {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Account ").list();
    }

    @Override
    public Account getAccount(long accountNumber) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Account.class, accountNumber);
//        return session.createQuery("from Account where ownerID = :userID and id = : accountNumber")
//                .list().get(0);
    }


    @Override
    public long addOperation(Operation operation) {
        return 0;
    }

    @Override
    public OperationHistory getOperationHistory(long userID) {
        return null;
    }

}
