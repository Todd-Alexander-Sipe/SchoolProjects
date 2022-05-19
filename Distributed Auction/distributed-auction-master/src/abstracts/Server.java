package abstracts;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: This is an abstract class that contains the structure of a server
 * for the client-server connection.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class Server implements Runnable {
    protected static final String END = "99";

    private final Socket clientSocket;
    protected final PrintWriter out;
    protected final BufferedReader in;

    /***
     * Constructor.
     *
     * @param clientSocket clientSocket is the socket the client is connecting
     *                     to the bank through.
     * @throws IOException typical io exception.
     */
    public Server(Socket clientSocket) throws IOException{
        this.clientSocket = clientSocket;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new
                InputStreamReader(clientSocket.getInputStream()));
    }


    /**
     * Run method from Runnable
     * Checks to send and receive messages
     */
    @Override
    public void run() {
        String inputLine = null;
        String outputLine;

        do {
            outputLine = processInput(inputLine);
            out.println(outputLine);

            if (outputLine.equals(END)){
                break;
            }
            try {
                inputLine = in.readLine();
            } catch (IOException exc) {
                inputLine = null;
            }
        } while (inputLine != null);
    }

    /**
     * Given some string from a client, handle it accordingly
     * @param fromClient A string from a client
     */
    protected abstract String processInput(String fromClient);


}
