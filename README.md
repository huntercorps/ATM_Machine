# ATM_Machine

# CMIS 242 Project 2
A program that emulates an ATM machine. The program utilizes a java GUI for Users to interact with the program.

# Program Description
A Java program that contains three classes GUI, Account, and InsufficientFunds.
## GUI Class
Contains two instances of Account, the main method, a constructor that builds the GUI interface, and event handlers for the four buttons Withdraw, Deposit, Transfer, and Balance.
Withdraw -When clicked several checks are made. The first check ensures the value in the text field can be parsed as a double. Next, a check is made to ensure the amount is a increment of $20. Subsequently, a withdraw is made from the account selected by the radio buttons. If there is insufficient funds to complete the transaction an exception is thrown and a JOptionPane window is displayed explaining the error. If the withdraw succeeds a window is displayed confirming that the withdrawal and displaying the service charge amount.
Deposit- When clicked a check is made to ensure that the input in the textfield is numeric and then funds are deposited in the selected account.
Transfer- When clicked checks are made for sufficient funds and number format. The funds are transferred to the selected account.
Balance- When clicked a JOptionPane window is displayed showing the current balance of the selected account.
## Account Class
Contains a constructor plus methods that corresponds to each of the four buttons in the GUI. In addition, the account class contains logic to deduct a service charge of $1.50 when more than four total withdrawals are made from either account.
## InsufficientFunds Class
Defines a checked exception when insufficient funds are in account.
