package agent;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: This is a class for the information of an agent
 */

import auctionHouse.AuctionInfo;

import java.util.ArrayList;
import java.util.Random;

public class Agent {

    private String name;
    private int funds;
    private int blockedFunds;
    private int accountNum;
    private ArrayList<AuctionInfo> knownAuctions;
    private final String[] FIRST_NAMES = {"James", "John", "Robert", "Mike",
            "Will", "Mary", "Patricia", "Jennifer", "Linda", "Liz"};
    private final String[] LAST_NAMES = {"Smith", "Jones", "Miller", "Garcia",
            "Davis", "Anderson", "Nguyen", "Lee", "Moore", "Martinez"};

    /**
     * Constructor for an agent
     */
    Agent() {
        setName();
        setStartingBalance();
        setKnownAuctions(new ArrayList<>());
    }

    /**
     * Sets the name of the new agent
     */
    private void setName() {
        Random rand = new Random();

        int firstIndex = rand.nextInt(FIRST_NAMES.length - 1);
        String nameString = FIRST_NAMES[firstIndex] + " ";

        int lastIndex = rand.nextInt(LAST_NAMES.length - 1);
        name =  nameString.concat(LAST_NAMES[lastIndex]);
    }

    /**
     * Gets the name of this agent
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the starting balance of the new agent to some random value.
     * The value is between 500 and 1000.
     */
    private void setStartingBalance() {
        Random rand = new Random();
        funds = 500 + rand.nextInt(500);
    }

    /**
     * Gets the balance of the agent
     * @return The balance
     */
    public int getFunds() {
        return funds;
    }

    /**
     * Sets the funds of the agent
     * @param f The funds
     */
    public void setFunds(int f) {
        funds = f;
    }

    /**
     * Get the locked funds of this agent
     * @return The locked funds
     */
    public int getBlockedFunds() {
        return blockedFunds;
    }

    /**
     * Sets the locked funds of the agent
     * @param f The locked funds
     */
    public void setBlockedFunds(int f) {
        blockedFunds = f;
    }

    /**
     * Sets the account number
     * @param accountNum The account number
     */
    public void setAccountNum(int accountNum) {
        this.accountNum = accountNum;
    }

    /**
     * Gets the account number
     * @return The account number
     */
    public int getAccountNum() {
        return accountNum;
    }

    /**
     * Sets the list of known Auction Houses
     * @param knownAuctions The list
     */
    public void setKnownAuctions(ArrayList<AuctionInfo> knownAuctions) {
        this.knownAuctions = knownAuctions;
    }

    /**
     * Gets the list of known Auction Houses
     * @return The list
     */
    public ArrayList<AuctionInfo> getKnownAuctions() {
        return knownAuctions;
    }
}
