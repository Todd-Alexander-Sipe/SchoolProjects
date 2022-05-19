package agent;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: This is the communication class from agent to AH.
 */

import abstracts.Client;
import auctionHouse.AuctionHouse;
import auctionHouse.AuctionInfo;
import auctionHouse.AuctionItem;

import java.io.IOException;

public class AgentToAH extends Client {

    Agent agent;

    AuctionHouse ah;
    int cost;

    public AuctionHouse getAH() {
        return ah;
    }

    /**
     * Constructor. Initializes Agent/AH and sets connection info
     * @param h The host name
     * @param p The port number
     */
    AgentToAH(String h, int p, Agent a, AuctionInfo info) {
        host = h;
        port = p;
        agent = a;
        this.ah = new AuctionHouse(info);
        try {
            startClient();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void processMessageFromServer(String message) {
        String[] tokens = message.split(" ");
        switch (Integer.parseInt(tokens[0])) {
            case 1:
                // Contains a cost for selected item
                boolean hasEnough = false;
                int bid = Integer.parseInt(tokens[1]);
                if (agent.getFunds() >= bid){
                    hasEnough = true;
                }
                if (hasEnough) {
                    cost = Integer.parseInt(tokens[2]);
                    agent.setBlockedFunds(cost);
                }
                break;
            case 2:
                // AH tells Agent outbid by another Agent
                //String outbid = tokens[1]; // "You have been out-bid"
                break;
            case 3:
                // AH tells Agent auction won
                String auctionWon = tokens[1];
                String itemWon = tokens[2];  //possibly this is an object,
                // not a string??
                int itemNum = Integer.parseInt(tokens[3]);
                ah.removeItem(itemNum);
                agent.setFunds(agent.getFunds() - cost);
                break;
            case 4:
                //send updated notions
                AuctionItem[] items = new AuctionItem[3];
                for (int i = 1; i < tokens.length; i++) {
                    String token = tokens[i];
                    AuctionItem item = AuctionItem.fromString(token);
                    items[i-1] = item;
                }
                ah.setItems(items);
                break;
            case 5:
                //removeItem
                int itemNumber = Integer.parseInt(tokens[1]);
                StringBuilder name = new StringBuilder();
                for (int i = 2; i < tokens.length-2; i++) {
                    name.append(tokens[i] +" ");
                }
                int price = Integer.parseInt(tokens[tokens.length-1]);
                AuctionItem item = new AuctionItem(name.toString(), price);
                ah.replaceItem(itemNumber, item);

                break;
            case 6:
                ah = null;
                break;
            default:
                // Unknown message code- ignore
        }
        updateNotionOfAgent(agent);
        if(ah != null) {
            updateNotionOfAH(ah);
        }
    }

    /**
     * Adds the new notion of the agent to the GUI blocking queue
     * @param a The updated agent
     */
    public void updateNotionOfAgent(Agent a) {
        try {
            //System.out.println("Updating notion of agent");
            AgentGUI.getNotionOfAgent().put(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the new notion of the auction house the GUI blocking queue
     * @param ah The updated auction house
     */
    public void updateNotionOfAH(AuctionHouse ah) {
        try {
            //System.out.println("Updating notion of auction house");
            AgentGUI.getNotionOfAH().put(ah);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
