/*
Written by Todd Sipe February of 2021 for CS351
The boneyard is a collection of all the dominoes, similar to a deck of cards
 */

package Common;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Boneyard {

    private List<Bone> bones;

    //This creates the initial set of 28 bones, double blank up through double 6
    public Boneyard(){
        bones = new ArrayList<Bone>();
        for (int i = 0; i < 7; i++){
            for (int j = i; j < 7; j++){
                Bone b = new Bone(i, j);
                bones.add(b);
            }
        }
        Collections.shuffle(bones);
    }

    //This checks to see if we have an empty boneyard
    public boolean isEmpty(){
        return bones.isEmpty();
    }

    //This tells us how many bones are in the boneyard
    public int size(){
        return bones.size();
    }

    //This allows us to take a bone from the boneyard
    public Bone take(){
        Bone b = bones.get(0);
        bones.remove(0);
        return b;
    }

    //This allows for easy printing of our boneyard
    public String toString(){
        String ret = "The Boneyard (shuffled):\n";
        for (int i = 0; i < bones.size(); i++){
            ret = ret + bones.get(i) + "\n";
        }
        return ret;
    }
}
