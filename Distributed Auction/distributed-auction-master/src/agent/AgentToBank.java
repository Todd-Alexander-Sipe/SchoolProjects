package agent;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: This is class to make and run a thread that will focus on sending and
 * receiving messages to a server.
 */

import auctionHouse.AuctionInfo;
import abstracts.Client;

import java.io.IOException;
import java.util.ArrayList;


public class AgentToBank extends Client{

    Agent agent;

    /**
     * Constructor. Initializes Agent and sets connection info
     * @param h The host name
     * @param p The port number
     * @param a The agent
     */
    AgentToBank(String h, int p, Agent a) {
        host = h;
        port = p;
        this.agent = a;
        try {
            startClient();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Given a message from the server, updates the notion of the Agent
     * @param message A string from a server
     */
    public void processMessageFromServer(String message) {
        String[] tokens = message.split(" ");
        switch (Integer.parseInt(tokens[0])) {
            case 1:
                // Contains account number
                int accountNumber = Integer.parseInt(tokens[1]);
                agent.setAccountNum(accountNumber);
                break;
            case 2:
                // Contains money info
                int funds = Integer.parseInt(tokens[1]);
                int blockedFunds = Integer.parseInt(tokens[2]);
                ArrayList<AuctionInfo> infos = new ArrayList<>();
                for (int i = 3; i < tokens.length; i++) {
                    String token = tokens[i];
                    AuctionInfo info = AuctionInfo.fromString(token);
                    infos.add(info);
                }
                agent.setFunds(funds);
                agent.setBlockedFunds(blockedFunds);
                agent.setKnownAuctions(infos);
                break;

            case 4:
                break;
            case 5:
                break;
            default:
                // Unknown message code- ignore

        }
        updateNotionOfAgent(agent);
    }

    /**
     * Adds the new notion of the agent to the GUI blocking queue
     * @param a The updated agent
     */
    private void updateNotionOfAgent(Agent a) {
        try {
            //System.out.println("Updating notion of agent");
            AgentGUI.getNotionOfAgent().put(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}




