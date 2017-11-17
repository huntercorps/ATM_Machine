/* File:    GUI.java
* Author:   Hunter T Smith
* Date:     16-Nov-17
* Purpose:  Program to create a ATM Machine GUI with ability to withdraw, deposit, transfer money,
* and see balance of account
*/

import javax.swing.*;
import java.awt.*;

public class GUI{
    /* Class Fields */
    private JFrame frame;
    private JTextField inputField;
    private JButton withdrawButton, depositButton, transferButton ,balanceButton;
    private JRadioButton savingsRadio, checkingRadio;
    // Create Accounts
    private Account savingsAccount = new Account(20.00);
    private Account checkingAccount = new Account(20.00);

    /* Constructor */
    private GUI(){
        setupUI();
    }

    /* Main Method */
    public static void main(String[] args) {
        GUI gui = new GUI();
    }

    /* Instance Methods */

    /* Method to setup UI and handle events */
    private void setupUI(){
        //Setup frame
        frame = new JFrame("ATM Machine");
        frame.setLayout(new FlowLayout());
        setupButtonPanel();
        setupInputPanel();
        setupButtonGroup();
        setupEventHandlers();
        frame.setSize(450,200);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void setupButtonPanel(){
        JPanel buttonPanel = new JPanel();
        frame.add(buttonPanel);
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
    }

    private void setupInputPanel(){
        //Setup inputPanel and add to frame
        JPanel inputPanel = new JPanel();
        frame.add(inputPanel);
        inputPanel.setLayout(new GridLayout(0,1));
        //Add components to inputPanel
        inputField = new JTextField("",20);
        inputPanel.add(inputField);
    }

    private void setupButtonGroup(){
        //Create Button group to toggle between RadioButtons and set checking to default
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(checkingRadio);
        buttonGroup.add(savingsRadio);
        checkingRadio.setSelected(true); //by setting as default this avoids null pointers
    }

    /* Method to ensure the value in the text field is numeric. */
    private double getInput(){
        double amt = 0;
        try {
            amt = Double.parseDouble(inputField.getText());
            if (amt <= 0){
                throw new NumberFormatException();
            }
        }
        catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(frame,"Invalid Input");
        }finally {
            inputField.setText("");
        }
        return amt;
    }

    /* Method to determine selected RadioButton and return corresponding account */
    private Account chooseAccount(){
        if (checkingRadio.isSelected()){
            return checkingAccount;
        }
        else {
            return savingsAccount;
        }
    }

    private void setupEventHandlers(){
         /* -------EventHandlers---------------------------- */

        //Event Handler for Withdraw button click
        withdrawButton.addActionListener(e -> {actionWithdraw();});

        //Event Handler for Deposit button click
        depositButton.addActionListener(e -> { actionDeposit();});

        //Event Handler for Balance button click
        balanceButton.addActionListener(e -> {actionBalance();});

        //Event Handler for Transfer button click
        transferButton.addActionListener(e -> {actionTransfer();});
    }

    private void actionWithdraw(){
        double amt = getInput();
        //check for pre-conditions
        if (amt <= 0)
            return;
        if (amt % 20 != 0){ //check for pre-conditions
            JOptionPane.showMessageDialog(frame,"Increments of $20.00 only");
            return;
        }
        try {
            chooseAccount().withdraw(amt);
            JOptionPane.showMessageDialog(frame,
                    String.format("$%.2f Withdrawn\n($%.2f Service Charge applied)",amt,chooseAccount().getServiceCharge()));
        }catch (InsufficientFunds ex){
            JOptionPane.showMessageDialog(frame,"Insufficient Funds");
        }
    }
    private void actionDeposit(){
        double amt = getInput();
        chooseAccount().deposit(amt);
        if (amt > 0){
            JOptionPane.showMessageDialog(frame,
                    String.format(" $%.2f Deposited in selected account",amt));
        }
    }

    private void actionBalance(){
        //chooseAccount().getBalance();
        JOptionPane.showMessageDialog(frame,
                String.format("Balance: $%.2f", chooseAccount().getBalance()));
    }

    private void actionTransfer(){
        double amt = getInput();
        //ignore attempt to transfer no money
        if (amt <= 0)
            return;
        try {
            if (chooseAccount() == checkingAccount){
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
}//End GUI Class
