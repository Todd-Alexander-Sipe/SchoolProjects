package abstracts;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: This is an abstract class that contains the structure of a client
 * for the client-server connection.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class Client {

    // Host and port to connect to
    protected String host;
    protected int port;

    // List of strings to send
    ArrayBlockingQueue<String> messagesToSend = null;

    // This string will be sent from the server to terminate the connection
    public final String END = "99";

    /**
     * Add a message to the outgoing queue.
     * Currently uses put -  blocks the thread if not possible
     * @param message The string you want to send
     */
    public void addMessageToQueue(String message) {
        try {
            messagesToSend.put(message);
            //System.out.println("Message added to queue: " + message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the next message in the outgoing queue.
     * Currently uses take - blocks the thread if not possible
     * @return The string that is about to be sent
     */
    private synchronized String getNextMessage() {
        try {
            return messagesToSend.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Start the "conversation" between the client and server
     * @throws IOException Connection exception
     */
    public void startClient() throws IOException {

        // Initialize the queue
        messagesToSend = new ArrayBlockingQueue<>(10);

        // Create a thread to manage conversation
        ClientThread clientThread = new ClientThread();
        clientThread.start();
    }

    /**
     * Given some string from a server, handle it accordingly
     * @param fromServer A string from a server
     */
    protected abstract void processMessageFromServer(String fromServer);

    private class ClientThread implements Runnable {

        /**
         * Starts the thread
         */
        public void start() {
            Thread thread = new Thread(this, "Client thread");
            thread.start();
        }

        /**
         * Begins the conversation loop
         */
        @Override
        public void run() {
            try {
                StartConversation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Starts the loop that sends and receives messages
         * @throws IOException Socket exceptions
         */
        private void StartConversation() throws IOException{
            try (
                    //Create socket
                    Socket socket = new Socket(host, port);

                    //Create output stream
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(),
                                    true);

                    //Create input stream
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()))
            ) {

                //Loop to keep "conversation" between client and server going
                String fromServer = in.readLine();
                while (fromServer != null) {

                    //System.out.println("Received message: " + fromServer);

                    if (fromServer.equals(END)) {
                        System.out.println("CLOSING SOCKET");
                        in.close();
                        out.close();
                        socket.close();
                        break;
                    }
                    processMessageFromServer(fromServer);

                    String fromUser = getNextMessage();
                    if (fromUser != null) {
                        //System.out.println("Sent message: " + fromUser);
                        out.println(fromUser);
                    }
                    fromServer = in.readLine();
                }
            }
        }
    }

}
