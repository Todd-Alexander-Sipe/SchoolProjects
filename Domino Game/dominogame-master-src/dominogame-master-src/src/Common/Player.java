/*
Written by Todd Sipe February of 2021 for CS351
Player is an object that holds a hand and manipulates this hand accordingly
 */

package Common;

public class Player {

    Hand h = new Hand();

    //This creates a hand for the player, retrieving 7 bones from the boneyard
    public Player(Boneyard by){
        h.createHand(by);
    }

    //This prints out the hand of a player
    public void printHand(){
        System.out.println(h);
    }

    //This plays a bone to the left side of the battlefield
    public void playLeft(int b, Battlefield bf){
        h.playLeft(b, bf);
    }

    //This plays a bone to the right side of the battlefield
    public void playRight(int b, Battlefield bf){ h.playRight(b, bf); }

    //This takes a bone from the boneyard
    public void take(Boneyard by){ h.add(by.take()); }

    //This flips a bone around
    public void flip(int b){
        h.flip(b);
    }

    //This gives us the size of a hand for a player
    public int size(){
        return h.size();
    }

    //This gets a bone from the hand of a player
    public Bone getBone (int b){
        return h.getBone(b);
    }

    //This is a method to check if the hand of a player is empty
    public boolean isEmpty(){
        return h.isEmpty();
    }

}
