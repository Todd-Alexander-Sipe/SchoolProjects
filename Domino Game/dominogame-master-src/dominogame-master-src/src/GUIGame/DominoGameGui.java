/*
Written by Todd Sipe February of 2021 for CS351
DominoGameGui is a game we can play with rules specified by the directions given to the class.
Player will take actions during a turn, and then when the turn is over the cpu will do
the same.
*/

package GUIGame;

import Common.Battlefield;
import Common.Boneyard;
import Common.Hand;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DominoGameGui extends Application {

    public static void main(String[] args) {launch(args);}

    static Hand hum0n = new Hand();
    static Hand c0mp = new Hand();
    static Battlefield bf = new Battlefield();
    static Boneyard by = new Boneyard();
    static HBox centerHBoxTop = new HBox();
    static HBox centerHBoxBot = new HBox();
    static VBox centerVBox = new VBox();
    private static boolean rightSwitch = false;
    private static boolean leftSwitch = false;
    private static final Label BONEYARD_LABEL_NUM = new Label();
    private static final Label C0MP_HAND_LABEL_NUM = new Label();

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Dominoes");

        hum0n.createHand(by);
        c0mp.createHand(by);
        BONEYARD_LABEL_NUM.setText("" + by.size());
        C0MP_HAND_LABEL_NUM.setText("" + c0mp.size());

        Label c0mpHandLabel = new Label("CPU Hand:");
        Label boneyardLabel = new Label("Boneyard:");
        Label infoText = new Label("Select which domino (0, 1, ...)");
        TextField boneSelect = new TextField();
        Button playRightBtn = new Button("Play Right");
        Button playLeftBtn = new Button("Play Left");
        Button flipBtn = new Button("Flip Bone");
        Button takeBtn = new Button("Take One From Boneyard");

        HBox topHBox = new HBox(30);
        HBox botHBox = new HBox(5);
        VBox leftVBox = new VBox(5);
        ScrollPane scrollPlayerHand = new ScrollPane(botHBox);
        centerVBox.getChildren().addAll(centerHBoxTop, centerHBoxBot);
        ScrollPane scrollField = new ScrollPane(centerVBox);

        boneyardLabel.setFont(Font.font(24));
        BONEYARD_LABEL_NUM.setFont(Font.font(24));
        c0mpHandLabel.setFont(Font.font(24));
        C0MP_HAND_LABEL_NUM.setFont(Font.font(24));

        topHBox.setAlignment(Pos.CENTER);
        topHBox.getChildren().addAll(c0mpHandLabel, C0MP_HAND_LABEL_NUM, boneyardLabel, BONEYARD_LABEL_NUM);
        botHBox.setPrefHeight(80);
        botHBox.setAlignment(Pos.CENTER);
        leftVBox.getChildren().addAll(playRightBtn, playLeftBtn, flipBtn, infoText, boneSelect, takeBtn);
        scrollField.setFitToHeight(true);

        //series of buttons that provide the basic functionality of the game
        //would have been nice to parse out the logic, but i was pressured under time crunch
        playRightBtn.setOnAction(e -> {
            if (!botHBox.getChildren().isEmpty()){
                int select = Integer.parseInt(boneSelect.getText());
                if(hum0n.playRight(select, bf)){
                    checkWin(hum0n, by);
                    if (bf.size() == 1){
                        makeInvisible(centerHBoxBot);
                        centerHBoxTop.getChildren().add(botHBox.getChildren().get(select));
                    }
                    else{
                        if (rightSwitch){
                            centerHBoxTop.getChildren().add(botHBox.getChildren().get(select));
                        }
                        else {
                            centerHBoxBot.getChildren().add(botHBox.getChildren().get(select));
                        }
                        rightSwitch = !rightSwitch;
                    }
                    c0mpTurn();
                }
            }
        });
        playLeftBtn.setOnAction(e -> {
            if (!botHBox.getChildren().isEmpty()){
                int select = Integer.parseInt(boneSelect.getText());
                if(hum0n.playLeft(select, bf)){
                    checkWin(hum0n, by);
                    if (bf.size() == 1){
                        makeInvisible(centerHBoxBot);
                        centerHBoxTop.getChildren().add(botHBox.getChildren().get(select));
                    }
                    else{
                        if (leftSwitch){
                            centerHBoxTop.getChildren().remove(0);
                            centerHBoxTop.getChildren().add(0, botHBox.getChildren().get(select));
                            makeInvisible(centerHBoxBot);
                        }
                        else{
                            centerHBoxBot.getChildren().remove(0);
                            centerHBoxBot.getChildren().add(0, botHBox.getChildren().get(select));
                            makeInvisible(centerHBoxTop);
                        }
                        leftSwitch = !leftSwitch;
                    }
                    c0mpTurn();
                }
            }
        });
        takeBtn.setOnAction(e -> {
            if (!by.isEmpty()){
                hum0n.take(by);
                new MakeVisual(botHBox, hum0n.size() - 1, hum0n.getBone(hum0n.size() - 1).side1(),
                        hum0n.getBone(hum0n.size() - 1).side2());
                BONEYARD_LABEL_NUM.setText("" + by.size());
            }
        });
        flipBtn.setOnAction(e -> {
            if (!botHBox.getChildren().isEmpty()){
                int select = Integer.parseInt(boneSelect.getText());
                hum0n.flip(select);
                new MakeVisual(botHBox, select, hum0n.getBone(select).side1(),
                        hum0n.getBone(select).side2());
                botHBox.getChildren().remove(select + 1);
            }
        });

        //create visual of initial player hand
        for (int i = 0; i < hum0n.size(); i++){
            new MakeVisual(botHBox, i, hum0n.getBone(i).side1(), hum0n.getBone(i).side2());
        }

        BorderPane root = new BorderPane();
        root.setTop(topHBox);
        root.setCenter(scrollField);
        root.setBottom(scrollPlayerHand);
        root.setLeft(leftVBox);

        Scene scene = new Scene(root, 650, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //c0mpTurn is a method that runs the cpu turn
    public static void c0mpTurn(){
        boolean passTurn = false;
        for (int i = 0; i < c0mp.size(); i++){
            if (c0mp.getBone(i).side2() == bf.getLeft() || c0mp.getBone(i).side2() == 0 || bf.getLeft() == 0){
                if (leftSwitch){
                    centerHBoxTop.getChildren().remove(0);
                    new MakeVisual(centerHBoxTop, 0, c0mp.getBone(i).side1(), c0mp.getBone(i).side2());
                    makeInvisible(centerHBoxBot);
                }
                else{
                    centerHBoxBot.getChildren().remove(0);
                    new MakeVisual(centerHBoxBot, 0, c0mp.getBone(i).side1(), c0mp.getBone(i).side2());
                    makeInvisible(centerHBoxTop);
                }
                leftSwitch = !leftSwitch;
                c0mp.playLeft(i, bf);
                C0MP_HAND_LABEL_NUM.setText("" + c0mp.size());
                c0mpCheckWin(c0mp, by);
                passTurn = true;
                break;
            }
            else if (c0mp.getBone(i).side1() == bf.getRight() || c0mp.getBone(i).side1() == 0 || bf.getRight() == 0){
                if (rightSwitch){
                    new MakeVisual(centerHBoxTop, centerHBoxTop.getChildren().size(),
                            c0mp.getBone(i).side1(), c0mp.getBone(i).side2());
                }
                else {
                    new MakeVisual(centerHBoxBot, centerHBoxBot.getChildren().size(),
                            c0mp.getBone(i).side1(), c0mp.getBone(i).side2());
                }
                rightSwitch = !rightSwitch;
                c0mp.playRight(i, bf);
                C0MP_HAND_LABEL_NUM.setText("" + c0mp.size());
                c0mpCheckWin(c0mp, by);
                passTurn = true;
                break;
            }
            else {
                c0mp.getBone(i).flip();
                if (c0mp.getBone(i).side2() == bf.getLeft() || c0mp.getBone(i).side2() == 0 || bf.getLeft() == 0){
                    if (leftSwitch){
                        centerHBoxTop.getChildren().remove(0);
                        new MakeVisual(centerHBoxTop, 0, c0mp.getBone(i).side1(), c0mp.getBone(i).side2());
                        makeInvisible(centerHBoxBot);
                    }
                    else{
                        centerHBoxBot.getChildren().remove(0);
                        new MakeVisual(centerHBoxBot, 0, c0mp.getBone(i).side1(), c0mp.getBone(i).side2());
                        makeInvisible(centerHBoxTop);
                    }
                    leftSwitch = !leftSwitch;
                    c0mp.playLeft(i, bf);
                    C0MP_HAND_LABEL_NUM.setText("" + c0mp.size());
                    c0mpCheckWin(c0mp, by);
                    passTurn = true;
                    break;
                }
                else if (c0mp.getBone(i).side1() == bf.getRight() || c0mp.getBone(i).side1() == 0 ||
                        bf.getRight() == 0){
                    if (rightSwitch){
                        new MakeVisual(centerHBoxTop, centerHBoxTop.getChildren().size(),
                                c0mp.getBone(i).side1(), c0mp.getBone(i).side2());
                    }
                    else {
                        new MakeVisual(centerHBoxBot, centerHBoxBot.getChildren().size(),
                                c0mp.getBone(i).side1(), c0mp.getBone(i).side2());
                    }
                    rightSwitch = !rightSwitch;
                    c0mp.playRight(i, bf);
                    C0MP_HAND_LABEL_NUM.setText("" + c0mp.size());
                    c0mpCheckWin(c0mp, by);
                    passTurn = true;
                    break;
                }
            }
        }
        if (!passTurn){
            c0mp.take(by);
            BONEYARD_LABEL_NUM.setText("" + by.size());
            c0mpTurn();
        }
    }

    //small method to create an invisible box to place before a row of bones on field
    private static void makeInvisible(HBox hb){
        Rectangle r = new Rectangle(40, 40, Color.TRANSPARENT);
        hb.getChildren().add(0, r);
    }

    //This method checks if the player has won
    public static void checkWin(Hand p, Boneyard by){
        if (p.size() == 0 && by.size() == 0){
            System.out.println("You win the game!!");
            System.exit(0);
        }
    }

    //This method checks if the cpu has won
    public static void c0mpCheckWin(Hand p, Boneyard by){
        if (p.size() == 0 && by.size() == 0){
            System.out.println("The C0mp won the game!! You lose. =[=[=[");
            System.exit(0);
        }
    }

}
