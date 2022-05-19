package auctionHouse;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: auctionHouse.AuctionInfo contains the information needed to connect
 * to an Auction House
 */

public class AuctionInfo {
    private String name;
    private final String HOST_NAME;
    private final int PORT_NUMBER;

    /**
     * Constructor for auction info
     * @param n The name of the auction house
     * @param host The host name of the server
     * @param port The port number to connect to
     */
    AuctionInfo(String n, String host, int port) {
        name = n;
        HOST_NAME = host;
        PORT_NUMBER = port;
    }

    /**
     * Gets the name
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the host name of the server.
     * @return The host name
     */
    public String getHOST_NAME() {
        return HOST_NAME;
    }

    /**
     * Get the port number of the server
     * @return The port number
     */
    public int getPORT_NUMBER() {
        return PORT_NUMBER;
    }

    /**
     * Convert to a string so it can be sent in a message
     * @return The string version
     */
    @Override
    public String toString() {
        return name.replace(' ', '-') +
                "/" + HOST_NAME +
                "/" + PORT_NUMBER;
    }

    /**
     * Create an AuctionInfo from a
     * @param string The string to convert to AuctionInfo
     * @return An AuctionInfo
     */
    static public AuctionInfo fromString(String string) {
        String[] tokens = string.split("/");
        String name = String.join(" ", tokens[0].split("-"));
        String hostName = tokens[1];
        int portNumber = Integer.parseInt(tokens[2]);
        return new AuctionInfo(name, hostName, portNumber);
    }
}
