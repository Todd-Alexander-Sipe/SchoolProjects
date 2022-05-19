package agent;
/***
 * Project 5: Auction Houses
 * Team members: Anthony Sharma, Todd Sipe, Manuel Lucero, Sehaj Singh.
 * Dates worked: 4/20/2020 - 5/15/2020.
 * Class: This is a class for the GUI of the Agent.
 */

import auctionHouse.AuctionHouse;
import auctionHouse.AuctionInfo;
import abstracts.Client;
import auctionHouse.AuctionItem;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class AgentGUI extends Application {

    private static ArrayBlockingQueue<Agent> notionOfAgent = null;
    private static ArrayBlockingQueue<AuctionHouse> notionOfAH = null;
    private static Agent agent;
    private static AuctionHouse auctionHouse = null;
    static AuctionHouse lastAH;
    private static Client agentToBank;
    static List<AgentToAH> allAtoAhClients = new ArrayList<>();
    static AgentToAH agentToAH = null;
    private Label bidStatus;
    private Label item1 = new Label();
    private Label item2 = new Label();
    private Label item3 = new Label();
    private Label item1Cost = new Label();
    private Label item2Cost = new Label();
    private Label item3Cost = new Label();
    private ObservableList<String> itemsCBOptions =
            FXCollections.observableArrayList();
    private static Label houseStatusActual;
    int counter = 30;
    String itemSelection;

    /***
     * Main method for the AgentGUI.
     * @param args 0: host name, 1: port name.
     */
    public static void main(String[] args) {
        String bankHostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        notionOfAgent = new ArrayBlockingQueue<>(200000);
        notionOfAH = new ArrayBlockingQueue<>(200000);

        agent = new Agent();

        // Start client
        agentToBank = new AgentToBank(bankHostName, portNumber, agent);

        agentToBank.addMessageToQueue("1 " + agent.getName()
                + " " + agent.getFunds());

        launch(args);

    }

    /***
     * Method for getting the notion of an Agent.
     * @return the next iteration of the values for an Agent.
     */
    public static ArrayBlockingQueue<Agent> getNotionOfAgent() {
        return notionOfAgent;
    }

    /***
     * Gets next Agent notion
     * @return next agent notion
     */
    private synchronized Agent getNextAgentNotion() {
        try {
            return notionOfAgent.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return notionOfAgent.poll();
    }

    /***
     * Gets next notion of AH
     * @return next ah notion
     */
    public static ArrayBlockingQueue<AuctionHouse> getNotionOfAH() {
        return notionOfAH;
    }

    /***
     * Gets next AH notion
     * @return next ah notion
     */
    private synchronized AuctionHouse getNextAHNotion() {
        return notionOfAH.poll();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle(agent.getName());

        GridPane root = new GridPane();
        root.setPrefSize(900,400);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setVgap(10);
        root.setHgap(10);
        Label timerLabel = new Label("Time remaining on bid: ");

        //LEFTHAND SIDE
        Label bankStatus = new Label("Bank status:");
        GridPane.setConstraints(bankStatus, 0, 0);

        Label bankStatusActual = new Label("Not connected.");
        bankStatusActual.setTextFill(Color.RED);
        GridPane.setConstraints(bankStatusActual, 0, 1);

        Label bankAccount = new Label("Account Number: not established.");
        GridPane.setConstraints(bankAccount, 0, 2);

        Label funds = new Label("Funds: 0");
        GridPane.setConstraints(funds, 0, 3);

        Label locked = new Label("Locked Funds: 0");
        GridPane.setConstraints(locked, 0, 4);

        ComboBox auctionHouseChoice = new ComboBox();
        ObservableList<String> auctionHouseCBOptions =
                FXCollections.observableArrayList();
        auctionHouseChoice.setItems(auctionHouseCBOptions);
        auctionHouseChoice.setPromptText("Auction House");
        GridPane.setConstraints(auctionHouseChoice, 0, 5);

        Button houseConnectButton =
                new Button("Connect to auction house");
        GridPane.setConstraints(houseConnectButton, 0, 6);
        houseConnectButton.setOnAction(e -> {
            String nameAH = (String) auctionHouseChoice.getValue();
            ArrayList<AuctionInfo> infos = agent.getKnownAuctions();
            for (AuctionInfo info:infos) {
                if (info.getName().equals(nameAH)) {
                    if(agentToAH == null) {
                        agentToAH = new AgentToAH(info.getHOST_NAME(),
                                info.getPORT_NUMBER(), agent, info);
                        allAtoAhClients.add(agentToAH);
                    }
                    for (int i = 0; i < allAtoAhClients.size(); i++) {
                        if(allAtoAhClients.get(i).getAH() ==
                                agentToAH.getAH()){ agentToAH =
                                allAtoAhClients.get(i);
                        }
                        else{
                            agentToAH = new AgentToAH(info.getHOST_NAME(),
                                    info.getPORT_NUMBER(), agent, info);
                            allAtoAhClients.add(agentToAH);
                        }
                    }
                    try {
                        agentToAH.startClient();
                        agentToAH.addMessageToQueue("4");
                        houseStatusActual.setText("Connected.");
                        houseStatusActual.setTextFill(Color.GREEN);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                        houseStatusActual.setText("Not connected.");
                        houseStatusActual.setTextFill(Color.RED);
                    }
                    break;
                }
            }
        });

        Button disconnectFromAH =
                new Button("Disconnect from auction house");
        GridPane.setConstraints(disconnectFromAH, 0, 7);
        disconnectFromAH.setOnAction(e -> {
            agentToAH.addMessageToQueue("97");
            agentToAH.addMessageToQueue("98");
            allAtoAhClients.remove(agentToAH);
            agentToAH = null;
            auctionHouse = null;
            notionOfAH.clear();
            houseStatusActual.setText("Not connected.");
            houseStatusActual.setTextFill(Color.RED);
            item1.setText("Item 1: ___");
            item2.setText("Item 2: ___");
            item3.setText("Item 3: ___");
            item1Cost.setText("Cost: ");
            item2Cost.setText("Cost: ");
            item3Cost.setText("Cost: ");
            itemsCBOptions.clear();
        });

        Button exit = new Button("Exit");
        GridPane.setConstraints(exit, 0, 8);
        exit.setOnAction(e ->{
            //Request to disconnect from Bank
            agentToBank.addMessageToQueue("5");
            primaryStage.close();
        });


        //RIGHT HAND SIDE
        Label houseStatus = new Label("Auction house status:");
        GridPane.setConstraints(houseStatus, 1, 0);

        houseStatusActual = new Label("Not connected.");
        houseStatusActual.setTextFill(Color.RED);
        GridPane.setConstraints(houseStatusActual, 1, 1);

        item1.setText("Item 1: ___");
        GridPane.setConstraints(item1, 1, 2);

        item2.setText("Item 2: ___");
        GridPane.setConstraints(item2, 1, 3);

        item3.setText("Item 3: ___");
        GridPane.setConstraints(item3, 1, 4);

        ComboBox items = new ComboBox();
        items.setItems(itemsCBOptions);
        items.setPromptText("Items for auction");
        GridPane.setConstraints(items, 1, 5);

        Button bid = new Button("Bid");
        GridPane.setConstraints(bid, 1, 6);
        bid.setOnAction(e -> {
            itemSelection = (String) items.getValue();
            int selector = 0;
            for (int i = 0; i < itemsCBOptions.size(); i++) {
                if (itemSelection.equalsIgnoreCase(itemsCBOptions.get(i))){
                    selector = i;
                }
            }
            int bidAmount = lastAH.getItemPrice(selector);
            int availFunds = agent.getFunds();
            if(availFunds >= bidAmount) {
                counter = 30;
                agentToBank.addMessageToQueue("3 " + bidAmount);
                agentToAH.addMessageToQueue("1 " + selector + " " +
                        agent.getAccountNum());
            } else {
                bidStatus.setText("Bid status: Not enough funds to bid");
            }

        });

        bidStatus = new Label("Bid status: No active bids");
        GridPane.setConstraints(bidStatus, 1, 7);

        GridPane.setConstraints(timerLabel, 1, 8);


        //THIRD COLUMN OF ITEMS
        item1Cost.setText("Cost: ");
        GridPane.setConstraints(item1Cost, 2, 2);

        item2Cost.setText("Cost: ");
        GridPane.setConstraints(item2Cost, 2, 3);

        item3Cost.setText("Cost: ");
        GridPane.setConstraints(item3Cost, 2, 4);


        root.getChildren().addAll(bankStatus, bankAccount, funds, locked,
                exit, houseConnectButton, auctionHouseChoice,
                houseStatus, item1, item2, item3, bid,
                timerLabel, disconnectFromAH, bankStatusActual, items,
                houseStatusActual, item1Cost, item2Cost, item3Cost, bidStatus);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        /**
         * Updates the GUI with the new information being received
         */
        class UpdateTimer extends AnimationTimer {
            private long lastUpdate = 0;
            int wasWinning = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 1_000_000_000) {
                    lastUpdate = now;
                    agent = getNextAgentNotion();
                    auctionHouse = getNextAHNotion();

                    agentToBank.addMessageToQueue("2");
                    if (agentToAH != null) {
                        agentToAH.addMessageToQueue("4");
                    }

                    bankAccount.setText("Account number: "
                            + agent.getAccountNum());
                    funds.setText("Funds: " + agent.getFunds());
                    locked.setText("Locked Funds: "
                            + agent.getBlockedFunds());
                    bankStatusActual.setText("Connected.");
                    bankStatusActual.setTextFill(Color.GREEN);

                    // Enable/disable exit button
                    exit.setDisable(agent.getBlockedFunds() > 0);

                    // Updates the dropdown menu for auction house connections
                    ArrayList<AuctionInfo> infos = agent.getKnownAuctions();
                    ArrayList<String> converter = new ArrayList<>();
                    for (AuctionInfo info : infos) {
                        converter.add(info.getName());
                    }
                    for (String s : converter) {
                        if (!auctionHouseCBOptions.contains(s)) {
                            auctionHouseCBOptions.add(s);
                        }
                    }
                    auctionHouseCBOptions.removeIf(s ->
                            !converter.contains(s));

                    if (auctionHouse != null) {
                        lastAH = auctionHouse;

                        // Set item names and costs in labels
                        item1.setText("Item 1: " +
                                auctionHouse.getItemName(0));
                        item2.setText("Item 2: " +
                                auctionHouse.getItemName(1));
                        item3.setText("Item 3: " +
                                auctionHouse.getItemName(2));
                        item1Cost.setText("Cost: " +
                                auctionHouse.getItemPrice(0));
                        item2Cost.setText("Cost: " +
                                auctionHouse.getItemPrice(1));
                        item3Cost.setText("Cost: " +
                                auctionHouse.getItemPrice(2));

                        // Updates drop down menu for items
                        if (itemsCBOptions.size() != 3){
                            itemsCBOptions.clear();
                            itemsCBOptions.add(0,
                                    auctionHouse.getItemName(0));
                            itemsCBOptions.add(1,
                                    auctionHouse.getItemName(1));
                            itemsCBOptions.add(2,
                                    auctionHouse.getItemName(2));
                        }
                        if ((itemsCBOptions.size() == 3)){
                            if (!itemsCBOptions.get(0).equals
                                    (auctionHouse.getItemName(0))){
                                itemsCBOptions.remove(0);
                                itemsCBOptions.add(0,
                                        auctionHouse.getItemName(0));
                            }
                            if (!itemsCBOptions.get(1).equals
                                    (auctionHouse.getItemName(1))){
                                itemsCBOptions.remove(1);
                                itemsCBOptions.add(1,
                                        auctionHouse.getItemName(1));
                            }
                            if (!itemsCBOptions.get(2).equals
                                    (auctionHouse.getItemName(2))){
                                itemsCBOptions.remove(2);
                                itemsCBOptions.add(2,
                                        auctionHouse.getItemName(2));
                            }
                        }

                        AuctionItem winningItem = null; // You are winning this
                        int itemIndex = -1; // The index of the current bid
                        AuctionItem[] items = auctionHouse.getItems();
                        for (int i = 0; i < items.length; i++) {
                            AuctionItem item = items[i];
                            int winnerID = item.getCurrentWinner();
                            if (winnerID != 0) {
                                itemIndex = i;
                                if (winnerID == agent.getAccountNum()) {
                                    winningItem = item;
                                }
                                break;
                            }
                        }

                        if (winningItem != null) {
                            // You are currently winning an item
                            wasWinning = 4;
                            bid.setDisable(true);
                            if (counter > 0){
                                timerLabel.setText("Time remaining on " +
                                        "current auction: " + counter +
                                        " seconds.");
                                counter--;
                                bidStatus.setText("Bid status: You are " +
                                        "currently the highest bid on "
                                        + winningItem.getName() + ".");
                            }
                            else {
                                itemsCBOptions.remove(winningItem.getName());
                                //Sets the agent's blocked funds to zero
                                agentToBank.addMessageToQueue("3 0");
                                //Takes the auction house item
                                agentToAH.addMessageToQueue("2 " + itemIndex);
                                //Enable the bid button
                                bid.setDisable(false);
                                timerLabel.setText("Congrats, you won a " +
                                        winningItem.getName() + "!");
                                bidStatus.setText("Bid status: No " +
                                        "active bids");
                            }
                        } else if (wasWinning > 0) {
                            wasWinning--;
                            // Unblock your funds
                            agentToBank.addMessageToQueue("4");
                            // Enable the bid button
                            bid.setDisable(false);

                            timerLabel.setText("Time remaining on " +
                                    "current auction: " + counter +
                                    " seconds.");
                            counter = 28;
                            bidStatus.setText("Bid status: You are no " +
                                    "longer the highest bid on item "
                                    + (itemIndex + 1));


                        } else if (itemIndex != -1) {
                            // Some others are bidding
                            if (counter > 0){
                                timerLabel.setText("Time remaining on " +
                                        "current auction: " + counter +
                                        " seconds.");
                                counter--;
                                bidStatus.setText("Bid status: You have not " +
                                        "bid on item " + (itemIndex + 1));

                            }
                            else {
                                bidStatus.setText("Bid status: No " +
                                        "active bids");
                            }
                        } else {
                            timerLabel.setText("Time remaining on bid: ");
                            bidStatus.setText("Bid status: No " +
                                    "active bids");
                        }
                    }
                }
            }
        }

        UpdateTimer timer = new UpdateTimer();
        timer.start();

    }
}