package auctionHouse;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: Main class for starting the auction house.
 */

import abstracts.Client;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class AuctionHouseStart {

    private static ArrayBlockingQueue<AuctionHouse> notionOfAH = null;
    private static AuctionHouse ah;

    public static void main(String[] args) {
        String bankHost = args[0];
        int bankPort = Integer.parseInt(args[1]);
        String AHHost = args[2];
        int AHPort = Integer.parseInt(args[3]);

        notionOfAH = new ArrayBlockingQueue<>(100);

        ah = new AuctionHouse(AHHost, AHPort);

        // Start a connection with the bank
        Client AHToBank = new AHToBank(bankHost, bankPort, ah);

        AHToBank.addMessageToQueue("6 "+ ah.getInfo().toString());

        //Start listening for clients
        ServerStarter serverStarter = new ServerStarter(AHPort);
        serverStarter.start();

        Scanner sc = new Scanner(System.in);
        String input = null;
        if (sc.hasNextLine()) {
            input = sc.nextLine();
        }
        assert input != null;
        if (input.equalsIgnoreCase("q")){
            AHToBank.addMessageToQueue("8 " + ah.getInfo().toString());
        }
    }

    /***
     * Gets ah
     * @return ah
     */
    public static synchronized AuctionHouse getAH() {
        return ah;
    }

    /***
     * sets ah
     * @param house ah
     */
    public static synchronized void setAH(AuctionHouse house) {
        ah = house;
    }


    /**
     * Gets the blockingQueue of AH notions
     * @return the blockingQueue of AH notions
     */
    public static ArrayBlockingQueue<AuctionHouse> getNotionOfAH() {
        return notionOfAH;
    }

    /**
     * Get the next version of the AH notion
     * @return The next AH notion
     */
    private static AuctionHouse getNextAHNotion() {
      //  System.out.println("Getting newest AH notion");
        try {
            return notionOfAH.take();
            //return notionOfAH.poll(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static class ServerStarter implements Runnable {

        private final int PORT_NUMBER;

        /***
         * starts the server
         * @param AHPortNumber port number
         */
        ServerStarter(int AHPortNumber) {
            PORT_NUMBER = AHPortNumber;
        }

        /**
         * Starts the thread
         */
        public void start() {
            Thread thread = new Thread(this,
                    "Server making thread");
            thread.start();
        }

        /**
         * Continuously search for Agents that want to connect
         */
        @Override
        public void run() {
            System.out.println("Listening on port: " + PORT_NUMBER);

            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(PORT_NUMBER);
            while(true) {
                Socket clientSocket = serverSocket.accept();
                AHServer ahi = new AHServer(clientSocket);
                Thread t = new Thread(ahi);
                t.start();
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
