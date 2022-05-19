package auctionHouse;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: This creates an item in auction houses.
 */

import java.util.Random;

public class AuctionItem {

    private String name;
    private int value;
    private int currentWinner;

    private final String[] PREFIXES ={"Moderately disgusting", "Glowing",
            "Jiggly", "Dull", "VERY used", "Well loved", "Ordinary",
            "Brand new", "Discarded",
            "Rare"};
    private final String[] ITEMS = {"cat toy", "dot matrix printer",
            "calendar for the year 1997", "neon sign", "broom", "report card",
            "china set", "wedding gift", "empty box", "work of... art??"};
    private final String[] SUFFIXES = {"", "", "", "", "with missing parts",
            "that's gone bad", "of general usefulness", "in need of repair",
            "that once belonged to Joe", "that still smells the same"};

    /**
     * Create a new random item
     */
    public AuctionItem() {
        setName();
        setValue();
        currentWinner = 0;
    }

    /**
     * Create a temporary item
     * @param b Bool
     */
    public AuctionItem(boolean b) {
        name = "Fetching information now...";
        value = 0;
    }

    /**
     * Used to create an AuctionItem from a string
     * @param name The name of the item
     * @param value The value
     */
    private AuctionItem(String name, int value, int winnerNum) {
        this.name = name;
        this.value = value;
        this.currentWinner = winnerNum;
    }

    /**
     * Used to create an AuctionItem from a string
     * @param name The name of the item
     * @param value The value
     */
    public AuctionItem(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Generates a random name
     */
    private void setName() {
        Random rand = new Random();

        int p = rand.nextInt(PREFIXES.length - 1);
        String prefix = PREFIXES[p] + " ";

        int i = rand.nextInt(ITEMS.length - 1);
        String item = ITEMS[i] + " ";

        int s = rand.nextInt(SUFFIXES.length - 1);
        String suffix = SUFFIXES[s];

        name = prefix + item + suffix;
    }

    /**
     * Get the item's name
     * @return The item's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the item to a random number
     */
    private void setValue(){
        Random rand = new Random();
        value = 50 + rand.nextInt(300);
    }

    /**
     * Gets the value of the item
     * @return The cost
     */
    public int getValue() {
        return value;
    }

    /**
     * Bid on this item.
     * Adds 25 to the price and sets the agent as the current winner
     * @param accountNumber The account of the agent who is bidding
     */
    public void bid(int accountNumber) {
        value += 25;
        currentWinner = accountNumber;
    }

    /**
     * See who successfully bid last
     * @return The current winner's bank account number
     */
    public int getCurrentWinner() {
        return currentWinner;
    }

    /**
     * Convert to a string so it can be sent in a message
     * @return The string version
     */
    @Override
    public String toString() {
        return name.replace(' ', '-') + "/" + value +
                "/" + currentWinner;
    }

    /**
     * Create an AuctionInfo from a
     * @param string The string to convert to AuctionInfo
     * @return An AuctionInfo
     */
    static public AuctionItem fromString(String string) {
        String[] tokens = string.split("/");
        String name = String.join(" ", tokens[0].split("-"));
        int value = Integer.parseInt(tokens[1]);
        int winnerNum = Integer.parseInt(tokens[2]);
        return new AuctionItem(name, value, winnerNum);
    }

}
