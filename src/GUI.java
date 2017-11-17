/* File:    GUI.java
* Author:   Hunter T Smith
* Date:     16-Nov-17
* Purpose:  Program to create a ATM Machine GUI with ability to withdraw, deposit, transfer money,
* and see balance of account
*/

import javax.swing.*;
import java.awt.*;

public class GUI {
    /* Declare/Init Instance Fields */
    private JFrame frame = new JFrame("ATM Machine");
    private JRadioButton checkingRadio = new JRadioButton("Checking");
    private JTextField inputField = new JTextField("",20);
    private Account savingsAccount = new Account(20.00);
    private Account checkingAccount = new Account(20.00);


    /* -------Constructor--------------------------------- */
    private GUI(){
        setupUI();
        frame.setSize(450,200);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /* -------Main Methods---------------------------- */
    public static void main(String[] args) {
        GUI gui = new GUI();
    }

    /* -------Instance Methods---------------------------- */

    /* Method to determine selected RadioButton and return corresponding account */
    private Account chooseAccount(){
        if (checkingRadio.isSelected()){
            return checkingAccount;
        } else
            return savingsAccount;
    }

    /* Method to ensure the value in the text field is numeric. */
    private double getInput(){
        try {
            double amt = Double.parseDouble(inputField.getText());
            if (amt <= 0){
                throw new NumberFormatException();
            }
            inputField.setText("");
            return amt;
        }
        catch (NumberFormatException ex){
            inputField.setText("");
            JOptionPane.showMessageDialog(frame,"Invalid Input");
            return 0.00;
        }
    }

    /* Method to setup UI and handle events */
    private void setupUI(){
        //Setup frame
        frame.setLayout(new FlowLayout());

        //Setup buttonPanel and add to frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,2));
        frame.add(buttonPanel);
        //Add components to buttonPanel
        JButton withdrawButton = new JButton("Withdraw");
        buttonPanel.add(withdrawButton);
        JButton depositButton = new JButton("Deposit");
        buttonPanel.add(depositButton);
        JButton transferButton = new JButton("Transfer");
        buttonPanel.add(transferButton);
        JButton balanceButton = new JButton("Balance");
        buttonPanel.add(balanceButton);
        buttonPanel.add(checkingRadio);
        JRadioButton savingsRadio = new JRadioButton("Savings");
        buttonPanel.add(savingsRadio);

        //Setup inputPanel and add to frame
        JPanel inputPanel = new JPanel();
        frame.add(inputPanel);
        inputPanel.setLayout(new GridLayout(0,1));
        //Add components to inputPanel
        inputPanel.add(inputField);

        //Create Button group to toggle between RadioButtons and set checking to default
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(checkingRadio);
        buttonGroup.add(savingsRadio);
        checkingRadio.setSelected(true); //by setting as default this avoids null pointers

        /* -------EventHandlers---------------------------- */

        //Event Handler for Withdraw button click
        withdrawButton.addActionListener(e -> {
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
        });

        //Event Handler for Deposit button click
        depositButton.addActionListener(e -> {
            double amt = getInput();
            chooseAccount().deposit(amt);
            if (amt > 0){
                JOptionPane.showMessageDialog(frame,
                        String.format(" $%.2f Deposited in selected account",amt));
            }
        });

        //Event Handler for Balance button click
        balanceButton.addActionListener(e -> {
            //chooseAccount().getBalance();
            JOptionPane.showMessageDialog(frame,
                    String.format("Balance: $%.2f", chooseAccount().getBalance()));
        });

        //Event Handler for Transfer button click
        transferButton.addActionListener(e -> {
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
        });
    }
}//End GUI Class
