package bank;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: bank.BankInteractions is a class intended for Agents and Auction Houses
 * alike to create an account with which to buy and sell items.
 */

import auctionHouse.AuctionInfo;
import abstracts.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class BankServer extends Server {
    private static ArrayList<AuctionInfo> auctionInfos = new ArrayList<>();
    private BankAccount account;

    /***
     * Constructor.
     *
     * @param clientSocket clientSocket is the socket the client is connecting
     *                     to the bank through.
     * @throws IOException typical io exception.
     */
    public BankServer(Socket clientSocket) throws IOException {
        super(clientSocket);
    }

    /***
     * method processInput handles the different phases of communication our
     * Bank has with the current client.
     *
     * @param input String used to represent what the client communicates to
     *              the Bank.
     * @return returns the output.
     */
    @Override
    protected String processInput(String input) {
       // System.out.println("Got message: " + input);
        String output = null;

        if (input == null) {
            return "10";
        }

        String auctionInfoString;
        AuctionInfo auctionInfo;

        //We can totally change the numbers to reflect whatever numbers we
        // need to, after we finalize the necessary conversations
        String[] tokens = input.split(" ");
        switch (Integer.parseInt(tokens[0])) {
            case 1:
                // Contains name and starting balance
                String name = tokens[1] + tokens[2];
                int startingBalance = Integer.parseInt(tokens[3]);
                account = new BankAccount(name, startingBalance);
                BankAccount.registerActiveID(account);
                output = "1 " + account.getAccountNumber();
                break;
            case 2:
                // Request for account info
                output = "2 " + account.getAvailableFunds() +
                        " " + account.getBlockedFunds();
                for (AuctionInfo info: auctionInfos) {
                    output = output.concat(" " + info.toString());
                }
                break;
            case 3:
                //Block Funds
                account.blockFunds(Integer.parseInt(tokens[1]));
                output = "4";
                break;
            case 4:
                //Restore blocked funds
                account.unblockAllFunds();
                output = "4";
                break;
            case 5:
                //Approve request to disconnect
                output = "99";
                break;


                //The following cases are for auction house <--> bank


            case 6:
                // Set up stuff for an auction house
                // Contains auction info
                auctionInfoString = tokens[1];
                auctionInfo = AuctionInfo.fromString(auctionInfoString);
                auctionInfos.add(auctionInfo);
                account = new BankAccount(auctionInfo.getName(), 0);
                BankAccount.registerActiveID(account);
                output = "6 " + account.getAccountNumber();
                break;
            case 7:
                // Deposit money from a sold item
                // Contains amount of money made from sale
                account.makeDeposit(Integer.parseInt(tokens[1]));
                output = "7 " + account.getAvailableFunds();
            case 8:
                // Disconnect from bank
                // Contains auction info
                auctionInfoString = tokens[1];
                auctionInfo = AuctionInfo.fromString(auctionInfoString);
                auctionInfos.removeIf(info ->
                        info.getName().equals(auctionInfo.getName()));
                output = "99";
            default:
                // Unknown message code- ignore

        }

        return output;

    }
}