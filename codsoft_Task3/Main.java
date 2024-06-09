import java.util.InputMismatchException;
import java.util.Scanner;

class BankAccount {

    private String holderName;
    private String accountNo;
    private double accountBalance;
    private String secret;

    public BankAccount(String holderName, String accountNo, double accountBalance, String secret) {
        this.holderName = holderName;
        this.accountNo = accountNo;
        this.accountBalance = accountBalance;
        this.secret = secret;
    }

    public String getHolderName() {
        return this.holderName;
    }

    public String getAccountNo() {
        return this.accountNo;
    }

    public double getAccountBalance() {
        return this.accountBalance;
    }

    public String getSecret() {
        return this.secret;
    }

    public void depositMoney(double amount) {
        this.accountBalance += amount;
        System.out.println("Deposited: " + amount);
    }

    public void withdrawMoney(double amount) {
        if (this.accountBalance - amount < 0 || this.accountBalance < 0) {
            System.out.println("Insufficient balance. Withdrawal cannot be processed.");
        } else {
            this.accountBalance -= amount;
            System.out.println("Withdrawn: " + amount);
        }
    }
}

class ATM {

    private BankAccount currentuser;
    public static boolean usageState = false;
    private static final Scanner scanner = new Scanner(System.in);

    private ATM(BankAccount user) {
        currentuser = user;
        usageState = true;
    }

    public static ATM useATM(BankAccount acc, String atmPin) {
        if (usageState) {
            System.out.println("ATM is currently in use. Please wait.");
            return null;
        }
        if (acc.getSecret().equals(atmPin)) {
            return new ATM(acc);
        } else {
            System.out.println("Invalid PIN. Please try again.");
            return null;
        }
    }

    public void checkBalance() {
        System.out.println("Current balance: " + currentuser.getAccountBalance());
    }

    public void withdraw(double amount) {
        currentuser.withdrawMoney(amount);
        System.out.println("Updated balance: " + currentuser.getAccountBalance());
        askForReceipt("withdraw", amount);
    }

    public void deposit(double amount) {
        currentuser.depositMoney(amount);
        System.out.println("Updated balance: " + currentuser.getAccountBalance());
        askForReceipt("deposit", amount);
    }

    private void askForReceipt(String mode, double amount) {
        System.out.print("Do you need a printed receipt? (Y/N): ");
        String response = scanner.next();
        if (response.equalsIgnoreCase("Y")) {
            printReceipt(mode, amount);
        }
    }

    public void printReceipt(String mode, double amount) {
        System.out.println("Printing receipt...");
        System.out.println("\nAccount No:" + currentuser.getAccountNo());
        System.out.println("\nAccount Name:" + currentuser.getHolderName());
        System.out.println("\nAccount Balance:" + currentuser.getAccountBalance());
        System.out.println(mode.toLowerCase().equals("withdraw") ? "Withdrawn Amount: Rs." + amount : "Deposited Amount: Rs." + amount);
    }

    public void leaveATM() {
        if (currentuser == null || !usageState) {
            System.out.println("ATM is already empty.");
        } else {
            this.currentuser = null;
            usageState = false;
            System.out.println("You have left the ATM. Have a great day!");
        }
    }

}

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            System.out.print("Enter your account number: ");
            String accountNumber = scanner.nextLine();
            System.out.print("Enter your initial balance: ");
            double initialBalance = scanner.nextDouble();
            System.out.print("Set your ATM PIN: ");
            scanner.nextLine();
            String atmPin = scanner.nextLine();
            BankAccount accountUser = new BankAccount(name, accountNumber, initialBalance, atmPin);
            System.out.println("Bank account created successfully!");
            System.out.println("Welcome to Bank ATM");
            System.out.print("Enter your ATM PIN: ");
            String inputPin = scanner.nextLine();

            ATM atm = ATM.useATM(accountUser, inputPin);
            if (atm == null) {
                System.out.println("Incorrect PIN. Exiting program.");
                return;
            }

            boolean continueUsingATM = true;
            while (continueUsingATM) {
                try {
                    System.out.println("\nATM Menu:");
                    System.out.println("1. Check Balance");
                    System.out.println("2. Withdraw Money");
                    System.out.println("3. Deposit Money");
                    System.out.println("4. Leave ATM");
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            atm.checkBalance();
                            break;
                        case 2:
                            System.out.print("Enter amount to withdraw: ");
                            double withdrawAmount = scanner.nextDouble();
                            atm.withdraw(withdrawAmount);
                            break;
                        case 3:
                            System.out.print("Enter amount to deposit: ");
                            double depositAmount = scanner.nextDouble();
                            atm.deposit(depositAmount);
                            break;
                        case 4:
                            atm.leaveATM();
                            continueUsingATM = false;
                            break;
                        default:
                            System.out.println("Invalid option. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please start over and enter the correct data.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
