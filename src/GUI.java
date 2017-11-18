/* File:    GUI.java
* Author:   Hunter T Smith
* Date:     16-Nov-17
* Purpose:  Program to create a ATM Machine GUI with ability to withdraw, deposit, transfer money,
* and see balance of account
*/
import javax.swing.*;
import java.awt.*;

public class GUI{
    /* declare Class Fields */
    private JFrame frame;
    private JPanel buttonPanel, inputPanel;
    private JTextField inputField;
    private JButton withdrawButton, depositButton, transferButton ,balanceButton;
    private JRadioButton savingsRadio, checkingRadio;
    private Account savingsAccount, checkingAccount;

    /* Constructor */
    private GUI(double checkingBalance, double savingsBalance){
        setupFrame();
        createAccounts(checkingBalance,savingsBalance);
    }

    /* Main Method */
    public static void main(String[] args) {
        GUI gui = new GUI(20.00,20.00);
    }

    /* Method to Setup frame and add properties/components */
    private void setupFrame(){
        frame = new JFrame("ATM Machine");
        frame.setLayout(new FlowLayout());
        setupButtonPanel();
        setupInputPanel();
        frame.add(buttonPanel); //must setupPanel first else null pointer
        frame.add(inputPanel); //must be setupPanel first else null pointer
        setupButtonGroup();
        setupEventListeners();
        frame.setSize(450,200);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /* Method to Setup buttonPanel and add components */
    private void setupButtonPanel(){
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,2));
        //Add components to buttonPanel
        withdrawButton = new JButton("Withdraw");
        buttonPanel.add(withdrawButton);
        depositButton = new JButton("Deposit");
        buttonPanel.add(depositButton);
        transferButton = new JButton("Transfer");
        buttonPanel.add(transferButton);
        balanceButton = new JButton("Balance");
        buttonPanel.add(balanceButton);
        checkingRadio = new JRadioButton("Checking");
        buttonPanel.add(checkingRadio);
        savingsRadio = new JRadioButton("Savings");
        buttonPanel.add(savingsRadio);
        //frame.add(buttonPanel);
    }

    /* Method to Setup inputPanel and add components */
    private void setupInputPanel(){
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0,1));
        //Add components to inputPanel
        inputField = new JTextField("",20);
        inputPanel.add(inputField);
        //frame.add(inputPanel);
    }
    /* Method to Create ButtonGroup to toggle between RadioButtons and set checking to default */
    private void setupButtonGroup(){
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(checkingRadio);
        buttonGroup.add(savingsRadio);
        checkingRadio.setSelected(true); //by setting as default this avoids null pointers
    }

    /* Method to create accounts and pass in initial balances */
    private void createAccounts(double checkingBalance, double savingsBalance){
        checkingAccount = new Account(checkingBalance);
        savingsAccount = new Account(savingsBalance);
    }

    /* Method to determine selected RadioButton and return corresponding account */
    private Account getActiveAccount(){
        return checkingRadio.isSelected() ? checkingAccount : savingsAccount;
    }

    /* Method to determine selected RadioButton and return corresponding account name string */
    private String getAccountName(){
        return checkingRadio.isSelected() ? "Checking" : "Savings";
    }

    /* Method for checking generic preconditions before handling events */
    private boolean checkPreConditions(){
        boolean pass = false;
        try {
            //check if value is <=0 or can be parsed as a double
            double amt = Double.parseDouble(inputField.getText());
            if (amt <= 0){
                throw new NumberFormatException();
            }
            pass = true;
        }
        catch (NumberFormatException ex){
            inputField.setText("");
            JOptionPane.showMessageDialog(frame,"Invalid Input");
        }
        return pass;
    }

    /* Method to ensure the value in the text field is numeric. */
    //can throw exception. make sure it is either caught or proceeded by checkPreConditions
    private double getInput() throws NumberFormatException{
        double amt = Double.parseDouble(inputField.getText());
        inputField.setText("");
        return amt;
    }

    /* Method to setup Listeners and pass to appropriate methods */
    private void setupEventListeners(){
        //Event Handler for Withdraw button click
        withdrawButton.addActionListener(e -> handleWithdraw());

        //Event Handler for Deposit button click
        depositButton.addActionListener(e -> handleDeposit());

        //Event Handler for Balance button click
        balanceButton.addActionListener(e -> handleBalance());

        //Event Handler for Transfer button click
        transferButton.addActionListener(e -> handleTransfer());
    }

    /* Method For handling withdraws */
    private void handleWithdraw(){
        if (checkPreConditions()) {
            try {
                double amt = getInput();
                if (amt % 20 != 0){ //check if increment of 20
                    JOptionPane.showMessageDialog(frame,"Increments of $20.00 only");
                    return;
                }
                getActiveAccount().withdraw(amt);
                JOptionPane.showMessageDialog(frame,
                        String.format("$%.2f Withdrawn from %s\n($%.2f Service Charge applied)",amt,getAccountName(), getActiveAccount().getServiceCharge()));
            }catch (InsufficientFunds ex){
                JOptionPane.showMessageDialog(frame,"Insufficient Funds");
            }
        }
    }

    /* Method For handling deposits */
    private void handleDeposit(){
        if (checkPreConditions()){
            double amt = getInput();
            getActiveAccount().deposit(amt);
            JOptionPane.showMessageDialog(frame,String.format(" $%.2f Deposited in %s",amt,getAccountName()));
        }
    }

    /* Method For handling balance queries */
    private void handleBalance(){
        JOptionPane.showMessageDialog(frame,String.format("%s Account Balance: $%.2f",getAccountName(), getActiveAccount().getBalance()));
    }

    /* Method For handling transfers */
    private void handleTransfer(){
        if (checkPreConditions()){
            try {
                double amt = getInput();
                if (getActiveAccount() == checkingAccount){
                    checkingAccount.transferTo(amt);
                    savingsAccount.transferFrom(amt);
                    JOptionPane.showMessageDialog(frame,
                            String.format("$%.2f Transferred\nTo: Checking\nFrom: Savings",amt));
                }
                else {
                    savingsAccount.transferTo(amt);
                    checkingAccount.transferFrom(amt);
                    JOptionPane.showMessageDialog(frame,
                            String.format("$%.2f Transferred\nTo: Savings\nFrom: Checking",amt));
                }
            }
            catch (InsufficientFunds ex){
                JOptionPane.showMessageDialog(frame,"Insufficient Funds");
            }
        }
    }

}//End GUI Class
