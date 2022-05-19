package auctionHouse;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: bank Auctions is a class intended for Agents and Auction Houses
 * alike to create an account with which to buy and sell items.
 */

import abstracts.Server;

import java.io.IOException;
import java.net.Socket;

public class AHServer extends Server {

    AuctionHouse ah;

    /***
     * Constructor.
     *
     * @param clientSocket clientSocket is the socket the client is connecting
     *                     to the bank through.
     * @throws IOException typical io exception.
     */
    public AHServer(Socket clientSocket) throws IOException {
        super(clientSocket);
    }

    /***
     * method processInput handles the different phases of communication
     * between the AH and agent, and between the AH and bank.
     *
     * @param input String used to represent what the agent communicates to
     *              the AH.
     * @return returns the output.
     */
    @Override
    protected String processInput(String input) {
        String output = null;

        ah = AuctionHouseStart.getAH();

        if (input == null) {
            return "10";
        }

        String[] tokens = input.split(" ");
        switch (Integer.parseInt(tokens[0])) {
            case 1:
                int itemNum = Integer.parseInt(tokens[1]);
                int agentAccount = Integer.parseInt(tokens[2]);
                ah.getItems()[itemNum].bid(agentAccount);
                output = "4";
                for (AuctionItem item: ah.getItems()) {
                    output = output.concat(" " + item.toString());
                }
                break;
            case 2:
                // Bid was won - remove item
                int itemNumber = Integer.parseInt(tokens[1]);
               // System.out.println("Removing item " + itemNumber);
                ah.removeItem(itemNumber);
                output =
                        "5 "+ itemNumber+ " " + ah.getItemName(itemNumber) +
                    " " + ah.getItemPrice(itemNumber);
                break;
            case 3:
                output = "Connect with Agent";
                break;
            case 4:
                output = "4";
                for (AuctionItem item: ah.getItems()) {
                    output = output.concat(" " + item.toString());
                }

                break;
            case 5:
            case 98:
                //Approve request to disconnect
                output = "99";
                break;
            case 6:
                output = "Bid Rejected";
                break;
            case 7:
                output = "Block funds";
                break;
            case 10:
                output = "10";
                break;
            case 97:
                output = "6";
                break;
        }
        AuctionHouseStart.setAH(ah);
        return output;
    }

}
