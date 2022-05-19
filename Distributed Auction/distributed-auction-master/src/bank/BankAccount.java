package bank;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: Bank  Accounts are created by ah and agents alike to track funds.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class BankAccount {

    private static final ArrayList<Integer> IDS = new ArrayList<>();
    private static final HashMap<Integer, BankAccount> activeIDs
            = new HashMap<>();
    private final String NAME;
    private final int ACCOUNT_NUM;
    private int blockedFunds;
    private int availableFunds;

    public BankAccount(String name, int balance) {
        this.ACCOUNT_NUM = assignID();
        NAME = name;
        this.availableFunds = balance;
        this.blockedFunds = 0;
    }

    /***
     * method createIDs makes the array and shuffles it to randomize the
     * account numbers assigned to the bank's clients.
     */
    public static void createIDs(){
        for (int i = 100000; i < 999999; i++) {
            IDS.add(i);
        }
        Collections.shuffle(IDS);
    }

    /***
     * method assignID is what is called to give the client an ID number.
     * @return id is the number that represents a clients account number with
     * the bank.
     */
    private static int assignID(){
        int id = IDS.get(0);
        IDS.remove(0);
        return id;
    }

    /***
     * gets the name
     * @return the name
     */
    public String getName() {
        return NAME;
    }

    /***
     * makes a deposit
     * @param deposit the deposit amount
     */
    public void makeDeposit(int deposit) {
        this.availableFunds += deposit;
    }

    /***
     * Make a withdrawal
     * @param withdrawal the amount
     */
    public void makeWithdrawal(int withdrawal) {
        this.availableFunds -= withdrawal;
    }

    /**
     * Gets the blocked funds
     * @return The blocked funds
     */
    public int getBlockedFunds() {
        return blockedFunds;
    }

    /**
     * Block a certain amount of funds
     * @param toBlock The amount to block
     */
    public void blockFunds(int toBlock) {
        if(toBlock != 0) {
            this.blockedFunds += toBlock;
            this.availableFunds -= toBlock;
        }
        else{
            this.blockedFunds = 0;
        }
    }

    /**
     * Restore all blocked funds to unblocked
     */
    public void unblockAllFunds() {
        this.availableFunds += this.blockedFunds;
        this.blockedFunds = 0;
    }

    /**
     * Get the available funds
     * @return The available funds
     */
    public int getAvailableFunds() {
        return availableFunds;
    }

    /**
     * Get the account number
     * @return The account number
     */
    public int getAccountNumber() {
        return ACCOUNT_NUM;
    }


    /**
     * Account lookup
     * @param accountNum The account number
     * @return The account
     */
    public static BankAccount getAccountFromID(int accountNum) {
        return activeIDs.get(accountNum);
    }

    /**
     * Register a new account number as being currently used
     * @param account The account
     */
    public static void registerActiveID(BankAccount account) {
        activeIDs.put(account.getAccountNumber(), account);
    }

    /**
     * Blocks funds
     * @param accountNum The account number of the account whose funds to block
     * @param toBlock The amount to block
     */
    public static void blockFunds(int accountNum, int toBlock) {
        BankAccount account = activeIDs.get(accountNum);
        account.availableFunds += -toBlock;
        account.blockedFunds += toBlock;
    }

    /**
     * Unblocks all funds
     * @param accountNum The number of the account whose funds to unblock
     */
    public static void unblockAllFunds(int accountNum) {
        BankAccount account = activeIDs.get(accountNum);
        account.availableFunds += account.blockedFunds;
        account.blockedFunds = 0;
    }

    /**
     * Transfers funds from one account to another
     * @param senderNum The account number of the sender
     * @param recipientNum The account number of the recipient
     * @param amount The amount to send
     */
    public static void transferFunds(int senderNum,
                                     int recipientNum,
                                     int amount) {
        BankAccount sender = activeIDs.get(senderNum);
        BankAccount recipient = activeIDs.get(recipientNum);
        sender.blockedFunds += -amount;
        recipient.availableFunds += amount;
    }
}
