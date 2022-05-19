/*
Written by Todd Sipe February of 2021 for CS351
DominoGame is a game we can play with rules specified by the directions given to the class.
Player will take actions during a turn, and then when the turn is over the cpu will do
the same.
*/

package ConsoleGame;

import Common.*;
import java.util.Scanner;

public class DominoGame {

    static Boneyard by = new Boneyard();
    static Battlefield bf = new Battlefield();
    static Player hum0n = new Player(by);
    static Player c0mp = new Player(by);

    public static void main(String[] args){

        System.out.println("Welcome! Let's play some Dominoes!");

        //playGame is a method that contains a while true for our game to be played
        playGame();
    }

    //c0mpTurn method is how the cpu player decides how to play which domino where
    public static void c0mpTurn(){
        System.out.println("C0mp is carefully considering the next course of action...");
        boolean passTurn = false;
        for (int i = 0; i < c0mp.size(); i++){
            if (c0mp.getBone(i).side2() == bf.getLeft() || c0mp.getBone(i).side2() == 0 || bf.getLeft() == 0){
                c0mp.playLeft(i, bf);
                System.out.println("C0mp played left side.\n");
                c0mpCheckWin(c0mp, by);
                passTurn = true;
                break;
            }
            else if (c0mp.getBone(i).side1() == bf.getRight() || c0mp.getBone(i).side1() == 0 || bf.getRight() == 0){
                c0mp.playRight(i, bf);
                System.out.println("C0mp played right side.\n");
                c0mpCheckWin(c0mp, by);
                passTurn = true;
                break;
            }
            else {
                c0mp.getBone(i).flip();
                if (c0mp.getBone(i).side2() == bf.getLeft() || c0mp.getBone(i).side2() == 0 || bf.getLeft() == 0){
                    c0mp.playLeft(i, bf);
                    System.out.println("C0mp played left side after flipping.\n");
                    c0mpCheckWin(c0mp, by);
                    passTurn = true;
                    break;
                }
                else if (c0mp.getBone(i).side1() == bf.getRight() || c0mp.getBone(i).side1() == 0 ||
                        bf.getRight() == 0){
                    c0mp.playRight(i, bf);
                    System.out.println("C0mp played right side after flipping.\n");
                    c0mpCheckWin(c0mp, by);
                    passTurn = true;
                    break;
                }
            }
        }
        if (!passTurn){
            c0mp.take(by);
            System.out.println("C0mp had to take a Bone from the Boneyard!");
            c0mpTurn();
        }
    }

    //While this while loop is going, the game is being played
    public static void playGame(){
        while (true){
            System.out.println("Size of the Boneyard: " + by.size());
            System.out.println("Size of C0mp Hand: " + c0mp.size());
            if (bf.size() != 0){
                bf.printField();
            }
            System.out.print("Hum0n ");
            hum0n.printHand();
            System.out.println("What would you like to do for your turn?" +
                    "\n[p] Play a domino" +
                    "\n[t] Take a domino from the Boneyard" +
                    "\n[q] Quit the game");
            Scanner sc = new Scanner(System.in);
            switch (sc.next()){
                case "p":
                    if (hum0n.isEmpty()){
                        System.out.println("Your hand is empty! Try taking a bone from the Boneyard.");
                        break;
                    }
                    System.out.println("Which domino? (0 is far left, incrementing as you go right)");
                    int selection;
                    try{
                        selection = sc.nextInt();
                    }
                    catch (Exception e){
                        System.out.println("********************************************************");
                        System.out.println("Please enter an integer for the question 'Which domino?'");
                        System.out.println("********************************************************");
                        break;
                    }
                    System.out.println("Flip this domino? (y/n)");
                    if (sc.next().equals("y")){
                        hum0n.flip(selection);
                    }
                    System.out.println("Play to left or right side? (l/r)");
                    int currentField = bf.size();
                    switch (sc.next()){
                        case "l":
                            hum0n.playLeft(selection, bf);
                            bf.printField();
                            checkWin(hum0n, by);
                            if (currentField != bf.size()){
                                c0mpTurn();
                            }
                            break;
                        case "r":
                            hum0n.playRight(selection, bf);
                            bf.printField();
                            checkWin(hum0n, by);
                            if (currentField != bf.size()){
                                c0mpTurn();
                            }
                            break;
                    }
                    break;
                case "t":
                    if (by.isEmpty()){
                        System.out.println("The Boneyard is empty!");
                        break;
                    }
                    hum0n.take(by);
                    break;
                case "q":
                    System.exit(0);
                default:
                    break;
            }
        }
    }

    //This method checks if the player has won
    public static void checkWin(Player p, Boneyard by){
        if (p.size() == 0 && by.size() == 0){
            System.out.println("You win the game!!");
            System.exit(0);
        }
    }

    //This method checks if the cpu has won
    public static void c0mpCheckWin(Player p, Boneyard by){
        if (p.size() == 0 && by.size() == 0){
            System.out.println("The C0mp won the game!! You lose. =[=[=[");
            System.exit(0);
        }
    }

}