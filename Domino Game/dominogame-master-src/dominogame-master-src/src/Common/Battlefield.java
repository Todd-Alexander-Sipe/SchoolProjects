/*
Written by Todd Sipe February of 2021 for CS351
The battlefield is the common play area
 */

package Common;

import java.util.List;
import java.util.ArrayList;

public class Battlefield {

    private String top = "";
    private String bot = "";
    private boolean rightSwitch = false;
    private boolean leftSwitch = false;

    private final List<Bone> FIELD;

    //This creates an empty list of bones that we use as the battlefield
    public Battlefield(){
        FIELD = new ArrayList<Bone>();
    }

    //This allows us to print the battlefield in direct order if needed
    public String toString(){
        StringBuilder ret = new StringBuilder();
        if (FIELD.size() == 0){
            ret.append("(empty)");
        }
        else {
            for (Bone bone : FIELD) {
                ret.append(bone);
            }
        }
        return ret.toString();
    }

    //This allows us to play a bone on the left side of the battlefield. It also keeps track
    //of the staggered printing of the battlefield that was required of us
    public boolean playLeft(Bone b){
        if (FIELD.size() == 0){
            FIELD.add(0, b);
            top = top + b;
            return true;
        }
        else {
            if (b.side2() == FIELD.get(0).side1() || b.side2() == 0 || FIELD.get(0).side1() == 0){
                FIELD.add(0, b);
                if (leftSwitch){
                    top = b + top;
                }
                else{
                    bot = b + bot;
                }
                leftSwitch = !leftSwitch;
                return true;
            }
            else {
                System.out.println("This Domino does not fit here!");
                return false;
            }
        }
    }

    //This allows us to play a bone on the right side of the battlefield. It also keeps track
    //of the staggered printing of the battlefield that was required of us
    public boolean playRight(Bone b){
        if (FIELD.size() == 0){
            FIELD.add(b);
            top = top + b;
            return true;
        }
        else {
            if (b.side1() == FIELD.get(FIELD.size() - 1).side2() || b.side1() == 0 ||
                    FIELD.get(FIELD.size() - 1).side2() == 0){
                FIELD.add(b);
                if (rightSwitch){
                    top = top + b;
                }
                else {
                    bot = bot + b;
                }
                rightSwitch = !rightSwitch;
                return true;
            }
            else {
                System.out.println("This Domino does not fit here!");
                return false;
            }
        }
    }

    //This tells us how many bones are on the battlefield
    public int size(){
        return FIELD.size();
    }

    //This gets the left-most bone on the battlefield
    public int getLeft(){
        return FIELD.get(0).side1();
    }

    //This gets the right-most bone on the battlefield
    public int getRight(){
        return FIELD.get(FIELD.size() - 1).side2();
    }

    //This prints our battlefield in a way that staggers the bones and keeps them in proper order
    public void printField(){
        if (leftSwitch){
            System.out.println("   " + top);
            System.out.println(bot);
        }
        else{
            System.out.println(top);
            System.out.println("   " + bot);
        }
    }
}
