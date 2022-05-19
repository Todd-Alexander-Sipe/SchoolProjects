package auctionHouse;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: This is class to make and run a thread that will focus on sending and
 * receiving messages to a server.
 */

import abstracts.Client;

import java.io.IOException;

public class AHToBank extends Client {

    // This is the hostname/port Agents need to connect to the AH server
    AuctionHouse ah;

    AHToBank(String bankHost, int bankPort, AuctionHouse ah) {
        host = bankHost;
        port = bankPort;
        this.ah = ah;
        try {
            startClient();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /***
     * Given a message from the server do tasks
     * @param message A string from a server
     */
    public void processMessageFromServer(String message) {
        String[] tokens = message.split(" ");
        switch (Integer.parseInt(tokens[0])) {
            case 6:
                // Contains account number
                //ah.setAccountNumber(Integer.parseInt(tokens[1]));
                break;
            case 7:
                // Deposit money from a sale
                break;
            case 8:
                // Disconnect from bank
                break;
            default:
                // Unknown message code- ignore

        }
        //updateNotionOfAgent(ah);
    }

}
