/*
Written by Todd Sipe February of 2021 for CS351
Hand is a collection of bones held by a player.
 */

package Common;

import java.util.List;
import java.util.ArrayList;

public class Hand {
    private final List<Bone> HAND;

    //This creates a list of bones for a hand
    public Hand(){
        HAND = new ArrayList<Bone>();
    }

    //This allows us to easily print a hand
    public String toString(){
        StringBuilder ret = new StringBuilder("Hand: ");
        for (Bone bone : HAND) {
            ret.append(bone).append(" ");
        }
        return ret.toString();
    }

    //This allows us to add a bone to our hand
    public void add(Bone b){
        HAND.add(b);
    }

    //This creates a hand of 7 bones
    public void createHand(Boneyard p){
        for (int i =0; i < 7; i++){
            HAND.add(p.take());
        }
    }

    //This plays a bone to the left side of the battlefield
    public boolean playLeft(int b, Battlefield bf){
        boolean check = bf.playLeft(HAND.get(b));
        if (check){
            HAND.remove(b);
            return true;
        }
        return false;
    }

    //This plays a bone to the right side of the battlefield
    public boolean playRight(int b, Battlefield bf){
        boolean check = bf.playRight(HAND.get(b));
        if (check){
            HAND.remove(b);
            return true;
        }
        return false;
    }

    public void take(Boneyard by){
        HAND.add(by.take());
    }

    //This flips a bone
    public void flip(int b){
        HAND.get(b).flip();
    }

    //This gives us the size of a hand
    public int size(){
        return HAND.size();
    }

    //This gets a bone from a hand
    public Bone getBone(int b){
        return HAND.get(b);
    }

    //This checks a hand to see if it is empty
    public boolean isEmpty(){
        return HAND.isEmpty();
    }

}
