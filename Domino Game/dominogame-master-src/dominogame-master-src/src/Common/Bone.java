/*
Written by Todd Sipe February of 2021 for CS351
This is an object that the game is played around.
 */

package Common;

public class Bone {
    private int side1;
    private int side2;

    public Bone(int boneSide1, int boneSide2){
        side1 = boneSide1;
        side2 = boneSide2;
    }

    //This gets side 1 of a bone
    public int side1(){
        return side1;
    }

    //This gets side 2 of a bone
    public int side2(){
        return side2;
    }

    //This allows us to easily print a bone
    public String toString(){
        return "[" + side1 + "  " + side2 + "]";
    }

    //This flips the sides of a bone
    public void flip(){
        int temp;
        temp = side1;
        side1 = side2;
        side2 = temp;
    }
}