package auctionHouse;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: auctionHouse.AuctionHouse is the representation of an Auction House
 */

public class AuctionHouse {

    private AuctionInfo info;
    private AuctionItem[] items;
    private int accountNumber;

    /**
     * Auction house constructor
     */
    public AuctionHouse() {
        items = new AuctionItem[]{null, null, null};
        fillItems();
    }

    /**
     * Constructor for an Auction House
     * @param hostName The host name of its server
     * @param port The port number
     */
    public AuctionHouse(String hostName, int port) {
        info = new AuctionInfo("Auction House " +
                port, hostName, port);
        items = new AuctionItem[]{null, null, null};
        fillItems();
    }

    /***
     * Constructor for an Auction House
     * @param info info for the ah
     */
    public AuctionHouse(AuctionInfo info) {
        this.info = info;
        AuctionItem voidItem = new AuctionItem(false) ;
        items = new AuctionItem[]{voidItem, voidItem, voidItem};
    }

    /**
     * If there are any items that have been sold, make a new item
     */
    private void fillItems() {
        for (int i = 0; i < 3; i++) {
            if (items[i] == null) {
                items[i] = new AuctionItem();
            }
        }
    }

    /**
     * Gets the AuctionInfo
     * @return The AuctionInfo
     */
    public AuctionInfo getInfo() {
        return info;
    }

    public AuctionItem[] getItems() {
        return items;
    }

    public void setItems(AuctionItem[] items) {
        this.items = items;
    }

    public String getItemName(int itemNum) {
        return items[itemNum].getName();
    }

    public int getItemPrice(int itemNum) {
        return items[itemNum].getValue();
    }


    public void removeItem(int itemNum){
        items[itemNum] = new AuctionItem();
    }
    public void replaceItem(int itemNum, AuctionItem item){
        items[itemNum] = item;
    }

    /**
     * Set the associated bank account number
     * @param accountNumber The account number
     */
    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Get the auctions house's bank account number
     * @return The account number
     */
    public int getAccountNumber() {
        return accountNumber;
    }
}
