package ru.vsu.cs.postnikov.banktask.UI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.vsu.cs.postnikov.banktask.Model.Account;
import ru.vsu.cs.postnikov.banktask.Model.Operations.Operation;
import ru.vsu.cs.postnikov.banktask.Model.User;
import ru.vsu.cs.postnikov.banktask.Services.Manager;
import ru.vsu.cs.postnikov.banktask.Model.Operations.*;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

@Component
public class GraphicUI implements UIConnector{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final Font font1 = new Font("Arial", Font.PLAIN, 20);

    private Manager manager;
    private JFrame frame;
    private JTabbedPane registerLoginTabs;
//    private JButton balanceGetButton;
    private UserPanel userPanel;

    private User currentUser;

    public void updateUser(){
        currentUser = manager.getUserById(currentUser.getId());
    }

    private void changeUser(User user){
        this.currentUser = user;
        if(user != null) {
            registerLoginTabs.setVisible(false);
            userPanel.setVisible(true);
            userPanel.updateComponents();
        } else {
            registerLoginTabs.setVisible(true);
            userPanel.setVisible(false);
            userPanel.flush();
        }
    }

    @PostConstruct
    public void showAll(){
        frame = new JFrame();
        frame.setLayout(null);
        JPanel registrationPanel = new JPanel();
        registrationPanel.setLayout(new GridLayout(3, 1));
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 1));
        JTextField usernameRegister = new JTextField();
        JTextField passwordRegister = new JTextField();
        JTextField usernameLogin = new JTextField();
        JTextField passwordLogin = new JTextField();
        JButton registerButton = new JButton();
        JButton loginButton = new JButton();
        registerButton.setText("Register");
        loginButton.setText("Login");
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                User user = manager.addUser(usernameRegister.getText(), passwordRegister.getText());
//                changeUser(user);
                RegisterUser registerUser = new RegisterUser(
                        usernameRegister.getText(),
                        passwordRegister.getText()
                );
                manager.executeOperation(registerUser);
                changeUser(registerUser.getUser());
            }
        });
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                User user = manager.checkLoginCredentials(usernameLogin.getText(), passwordLogin.getText());
                if(user != null) changeUser(user);
                else JOptionPane.showMessageDialog(frame, "Wrong login or password");
            }
        });

        registrationPanel.add(usernameRegister);
        registrationPanel.add(passwordRegister);
        registrationPanel.add(registerButton);
        loginPanel.add(usernameLogin);
        loginPanel.add(passwordLogin);
        loginPanel.add(loginButton);

        registerLoginTabs = new JTabbedPane();
//        registerLoginTabs.set
        registerLoginTabs.addTab("Register", registrationPanel);
        registerLoginTabs.addTab("Login", loginPanel);

        registerLoginTabs.setBounds(300, 200, 200, 100);
//        registerLoginTabs.add(registrationPanel);
//        loginPanel.setBounds(200, 200, 100, 100);
//        registerLoginTabs.add(loginPanel);


        frame.add(registerLoginTabs);

//        balanceInfo = new JLabel();
//        balanceGetButton = new JButton();
//        balanceInfo.setBounds(400, 100, 70, 30);
//        balanceGetButton.setBounds(480, 100, 40, 40);
//        balanceGetButton.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                Manager.getMoney(0, 0);
//            }
//        });

        userPanel = new UserPanel(this);
        userPanel.setBounds(0, 0, WIDTH, HEIGHT);
        frame.add(userPanel);
//        frame.add(balanceInfo);
//        frame.add(balanceGetButton);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
//        frame.setVisible(true);
    }

    @Autowired
    public void init(@Lazy Manager manager){
        this.manager = manager;
    }

    public void show(){
        frame.setVisible(true);
    }

    private static class UserPanel extends JPanel{
        private GraphicUI parent;
        private JLabel userName;
        private JButton logout;
        private JPanel accountsListPanel;
        private JButton addAccount;
        private JPanel selectedAccountInfo;
        private JButton showHistory;
        private JPanel historyPanel;
        private JButton showAllAccountsInfoButton;
        private JPanel allAccountsInfoPanel;
        private JLabel balanceLabel;

        public UserPanel(GraphicUI parent) {
            this.parent = parent;
            initGraphics();
        }

        private void initGraphics(){
            setLayout(null);
            userName = new JLabel();
            userName.setFont(font1);
            userName.setBounds(10, 10, 100, 40);
            logout = new JButton("Log out");
            logout.setFont(font1);
            int width = 140;
            logout.setBounds(GraphicUI.WIDTH - width - 20, 10, width, 30);
            logout.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    parent.changeUser(null);
                }
            });
            balanceLabel = new JLabel();
            accountsListPanel = new JPanel();
            accountsListPanel.setBounds(50, 90, 100, 370);
            accountsListPanel.setLayout(new GridLayout(10, 1));
            accountsListPanel.setBorder(BorderFactory.createTitledBorder("Accounts"));

            addAccount = new JButton("Add account");
            addAccount.setBounds(50, 50, 120, 30);
            addAccount.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
//                    parent.manager.openAccount(parent.currentUser.getId());
                    parent.manager.executeOperation(new OpenAccount().setUserAndReturn(parent.currentUser));
                    parent.updateUser();

                    parent.showOperationHistory(
                            parent.manager.getHistory(parent.currentUser.getId()).getOperations()
                    );
                    parent.showUserAccountsInfo(
                            parent.manager.getAccounts(parent.currentUser.getId())
                    );
//                    parent.manager.showHistory(parent.currentUser.getId());
//                    parent.manager.showUserAccountsInfo(parent.currentUser.getId());
                    updateAccountList();
//                    updateComponents();
                }
            });
            selectedAccountInfo = new JPanel();
            selectedAccountInfo.setBounds(170, 90, 150, 240);
            selectedAccountInfo.setLayout(new GridLayout(6, 1));
            selectedAccountInfo.setBorder(BorderFactory.createTitledBorder("Account info"));
            selectedAccountInfo.setVisible(false);
            historyPanel = new JPanel();
            historyPanel.setBounds(370, 90, 400, 340);
            historyPanel.setLayout(new GridLayout(20, 1));
            historyPanel.setBorder(BorderFactory.createTitledBorder("History"));
            historyPanel.setVisible(false);
            showHistory = new JButton("History");
            showHistory.setBounds(370, 50, 150, 30);
            showHistory.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    allAccountsInfoPanel.setVisible(false);
                    parent.showOperationHistory(
                            parent.manager.getHistory(parent.currentUser.getId()).getOperations()
                    );
//                    parent.manager.showHistory(parent.currentUser.getId());
                    historyPanel.setVisible(true);
                }
            });
            allAccountsInfoPanel = new JPanel();
            allAccountsInfoPanel.setBounds(370, 90, 400, 340);
            allAccountsInfoPanel.setLayout(new GridLayout(10, 1));
            allAccountsInfoPanel.setBorder(BorderFactory.createTitledBorder("Accounts info"));
            allAccountsInfoPanel.setVisible(false);
            showAllAccountsInfoButton = new JButton("All info");
            showAllAccountsInfoButton.setBounds(530, 50, 150, 30);
            showAllAccountsInfoButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    historyPanel.setVisible(false);
                    parent.showUserAccountsInfo(
                            parent.manager.getAccounts(parent.currentUser.getId())
                    );
//                    parent.manager.showUserAccountsInfo(parent.currentUser.getId());
                    allAccountsInfoPanel.setVisible(true);
                }
            });


            add(userName);
            add(logout);
            add(accountsListPanel);
            add(addAccount);
            add(selectedAccountInfo);
            add(showHistory);
            add(historyPanel);
            add(showAllAccountsInfoButton);
            add(allAccountsInfoPanel);

            setVisible(false);
        }

        private void updateAboutAccount(Account account){
            selectedAccountInfo.removeAll();
            selectedAccountInfo.add(new JLabel(String.valueOf(account.getId())));
            selectedAccountInfo.add(balanceLabel);
            balanceLabel.setText(account.getMoney() + "$");
//            parent.manager.getMoney(parent.currentUser.getId(), account.getId());
//            selectedAccountInfo.add(new JLabel("Balance: " + account.getMoney() + "$"));
            JButton addMoney = new JButton("Add money");
            addMoney.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String num = JOptionPane.showInputDialog(
                            parent.frame, "Enter money value",
                            "Add money",
                            JOptionPane.PLAIN_MESSAGE
                    );
                    try {
                        parent.manager.executeOperation(new AddMoney(
                                account.getId(),
                                new BigDecimal(num)
                                ).setUserAndReturn(parent.currentUser)
                        );
                        parent.updateUser();
//                        parent.manager.addMoney(parent.currentUser.getId(),
//                                account.getId(), Long.parseLong(num));
                        updateAboutAccount(account);
                        parent.showOperationHistory(
                                parent.manager.getHistory(parent.currentUser.getId()).getOperations()
                        );
                        parent.showUserAccountsInfo(
                                parent.manager.getAccounts(parent.currentUser.getId())
                        );
//                        parent.manager.showHistory(parent.currentUser.getId());
//                        parent.manager.showUserAccountsInfo(parent.currentUser.getId());
                    }catch (NumberFormatException exception){
                        JOptionPane.showMessageDialog(parent.frame,
                                "Wrong number of money");
                    }
                }
            });
            selectedAccountInfo.add(addMoney);

            JButton transferMoney = new JButton("Transfer money");
            transferMoney.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    JPanel tmpPanel = new JPanel();
                    tmpPanel.setLayout(new GridLayout(3, 2));
                    JLabel fromLabel = new JLabel("From: ");
                    JLabel toLabel = new JLabel("To: ");
                    JLabel amountLabel = new JLabel("Amount: ");
                    JTextField fromAccount = new JTextField();
                    JTextField toAccount = new JTextField();
                    JTextField amount = new JTextField();

                    tmpPanel.add(fromLabel);
                    tmpPanel.add(fromAccount);
                    tmpPanel.add(toLabel);
                    tmpPanel.add(toAccount);
                    tmpPanel.add(amountLabel);
                    tmpPanel.add(amount);

                    fromAccount.setText(String.valueOf(account.getId()));

                    JOptionPane.showMessageDialog(
                            parent.frame, tmpPanel,
                            "Transfer money",
                            JOptionPane.PLAIN_MESSAGE
                    );

                    try {
                        long from = Long.parseLong(fromAccount.getText());
                        long to = Long.parseLong(toAccount.getText());
                        BigDecimal money = new BigDecimal(Long.parseLong(amount.getText()));
//                        parent.manager.transferMoney(parent.currentUser.getId(), from, to, money);
                        parent.manager.executeOperation(new TransferMoney(
                                from,
                                to,
                                money
                        ).setUserAndReturn(parent.currentUser));
                        parent.updateUser();
                        updateAboutAccount(account);
                        parent.showOperationHistory(
                                parent.manager.getHistory(parent.currentUser.getId()).getOperations()
                        );
                        parent.showUserAccountsInfo(
                                parent.manager.getAccounts(parent.currentUser.getId())
                        );
//                        parent.manager.showHistory(parent.currentUser.getId());
//                        parent.manager.showUserAccountsInfo(parent.currentUser.getId());
                    }catch (NumberFormatException exception){
                        JOptionPane.showMessageDialog(parent.frame,
                                "Wrong input");
                    }
                }
            });
            selectedAccountInfo.add(transferMoney);

            JButton close = new JButton("Close account");
            close.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    parent.manager.executeOperation(
                            new CloseAccount(account.getId()).setUserAndReturn(parent.currentUser)
                    );
                    parent.updateUser();
//                    parent.manager.closeAccount(parent.currentUser.getId(), account.getId());
                    updateAccountList();
                    selectedAccountInfo.setVisible(false);
                    parent.showOperationHistory(
                            parent.manager.getHistory(parent.currentUser.getId()).getOperations()
                    );
                    parent.showUserAccountsInfo(
                            parent.manager.getAccounts(parent.currentUser.getId())
                    );
//                    parent.manager.showHistory(parent.currentUser.getId());
//                    parent.manager.showUserAccountsInfo(parent.currentUser.getId());
                }
            });
            selectedAccountInfo.add(close);

            selectedAccountInfo.setVisible(true);
            selectedAccountInfo.revalidate();
        }

        private void updateAccountList(){
            accountsListPanel.removeAll();
            List<Account> accountList = parent.manager.getAccounts(parent.currentUser.getId());
            for (Account a:accountList){
                JButton b = new JButton(String.valueOf(a.getId()));
                b.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        updateAboutAccount(a);
                    }
                });
                accountsListPanel.add(b);
            }
        }

        public void updateComponents(){
            flush();
            userName.setText(parent.currentUser.getLogin());
            updateAccountList();
        }

        public void flush(){
            userName.setText("");
            accountsListPanel.removeAll();
            selectedAccountInfo.removeAll();
            selectedAccountInfo.setVisible(false);
            historyPanel.removeAll();
            historyPanel.setVisible(false);
            allAccountsInfoPanel.removeAll();
            allAccountsInfoPanel.setVisible(false);
        }
    }

    @Override
    public void showOperationHistory(List<Operation> operations) {
        JPanel historyPanel = userPanel.historyPanel;
        historyPanel.removeAll();
        int maxShowOperations = 20;
        List<Operation> lastOperations = operations.subList(Math.max(0, operations.size() - maxShowOperations),
                operations.size());
        for (Operation o:lastOperations){
            historyPanel.add(new JLabel(o.getInfo()));
        }
//        historyPanel.setVisible(true);
        historyPanel.revalidate();
    }

    @Override
    public void showUserAccountsInfo(List<Account> accounts) {
        JPanel accountsInfoPanel = userPanel.allAccountsInfoPanel;
        accountsInfoPanel.removeAll();
        accountsInfoPanel.add(new JLabel("Открытые счета:"));
        for (Account account:accounts){
            accountsInfoPanel.add(new JLabel("Счет: " + account.getId() + " Баланс: "
                    + account.getMoney() + "$"));
        }
//        accountsInfoPanel.setVisible(true);
        accountsInfoPanel.revalidate();
    }
}
