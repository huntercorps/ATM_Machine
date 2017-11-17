/* File:    Account.java
* Author:   Hunter T Smith
* Date:     16-Nov-17
* Purpose:  A class to instantiate an Account object. Contains a constructor,
* methods to correspond with GUI buttons, and capability to apply service charges after 4 withdraws
*/

public class Account {
    /* Declare Fields */
    private static int token = 0;
    private double balance;

    /* -------Constructor--------------------------------- */
    public Account(double balance){
        this.balance=balance;
    }

    /* -------Getter and setter methods------------------- */
    public double getBalance(){
        return balance;
    }

    /* -------Instance Methods---------------------------- */

    /* Method to increment token and apply service charges */
    public double getServiceCharge(){
        double svc = token > 4 ? 1.50: 0.00;
        return svc;
    }

    /* Method to withdraw from account */
    public void withdraw(double withdrawAmt) throws InsufficientFunds {
        withdrawAmt = withdrawAmt+getServiceCharge();//service charge added to withdrawAmt
        if (balance >= withdrawAmt){ //check for sufficient funds
            balance = balance-withdrawAmt;
            token++;
        }else
            throw new InsufficientFunds();
    }

    /* Method to add deposit to account balance */
    public void deposit(double depositAmt){
        balance += depositAmt;
    }

    /* Method to adjust balance of receiving account of transfer */
    public void transferTo(double transferAmt){
            balance += transferAmt;
    }

    /* Method to adjust balance of account transfer from */
    public void transferFrom(double transferAmt)throws InsufficientFunds {
        if (transferAmt > balance){ //ensure sufficient funds
            throw new InsufficientFunds();
        }else
            balance -= transferAmt;
    }

}//End Account Class
