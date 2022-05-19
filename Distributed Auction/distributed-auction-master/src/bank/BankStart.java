package bank;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: bank.BankServer is a class designed to act as the server to auction
 * houses and agents.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BankStart {

    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);

        BankAccount.createIDs();

        System.out.println("Listening on port: " + portNumber);

        ServerSocket serverSocket = new ServerSocket(portNumber);
        while(true) {
            Socket clientSocket = serverSocket.accept();
            BankServer bi = new BankServer(clientSocket);
            Thread t = new Thread(bi);
            t.start();
        }
    }
}