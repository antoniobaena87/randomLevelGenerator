/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antoniobaena.randomlevelgenerator.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author antonio
 */
class DoorsCollection {
    
    private HashMap<String, Float> doors = new HashMap<>();
    private float[] probabilities = new float[]{100,100,100,100};
    private String[] directions = new String[4];
    
    /**
     * 
     * @param map HashMap containing String with all four directions, and float with the chance of being placed for each door
     * For example, <"N", 0.5> means the north door has a 50% chance of being placed while <"E", 0.25> means the east door has
     * a 25% chance of being placed.
     * The probability is useful when getting a random door, as a bias. If the user wants a less cluttered level, one of the doors
     * must have a significant higher probability of being placed. For example, if the user sets thickness to zero, the level should
     * appear as a straight line. Thus, one of the directions will have a 100% probability of being placed.
     * The arrays are sorted in ascending order.
     */
    public DoorsCollection(HashMap<String, Float> map){
        doors = map;  // doors isn't used in the class -> remove
        // Sort arrays in ascending order
        for(String key:map.keySet()){
            //System.out.println("Checking " + key);
            for(int i = 0; i < probabilities.length; i++){
                //System.out.println("Checking if " + key + " probability (" + map.get(key) + ") < " + probabilities[i] + " for position " + i + " in probabilities array");
                if(map.get(key) <= probabilities[i]){
                    //System.out.println("True... moving data to the right");
                    for(int j = probabilities.length - 1; j > i; j--){
                       //System.out.println("Moving probabilities[" + (j-1) + "] = " + probabilities[j-1] + " to probabilities[" + j + "] = " + probabilities[j]);
                        //System.out.println("And moving directions[" + (j-1) + "] = " + directions[j-1] + " to directions[" + j + "] = " + directions[j]);
                        probabilities[j] = probabilities[j-1];
                        directions[j] = directions[j-1];
                        //System.out.println("The resulting arrays after this are: ");
                        //System.out.println(Arrays.toString(directions));
                        //System.out.println(Arrays.toString(probabilities));
                        //System.out.println("");
                    }
                    //System.out.println("Setting probabilities[" + i + "] = " + map.get(key));
                    //System.out.println("And directions[" + i + "] = " + key);
                    probabilities[i] = map.get(key);
                    directions[i] = key;
                    break;
                }
            }
        }
    }
    
    /**
     * 
     * @param possibleDoors List with the possible doors that can be placed in a particular room.
     * @return A door, randomly chosen and taking into account the probability bias.
     * null if no other door should be placed.
     */
    protected String getRandomDoor(ArrayList<String> possibleDoors){
        
        if(possibleDoors.isEmpty()) throw new IllegalArgumentException("possibleDoors list is empty");
        
        float upperLimit = 100;
        float[] limits = new float[possibleDoors.size()];
        String[] dirs = new String[possibleDoors.size()];
        
        int j = 0;
        for(int i = 0; i < directions.length; i++){
            if(!possibleDoors.contains(directions[i])) upperLimit -= probabilities[i];
            else{
                dirs[j] = directions[i];
                limits[j++] = probabilities[i];
            }
        }
        
        // Calculate inferior limits for each possibility
        for(int i = limits.length - 1; i >= 0; i--){
            if(i == limits.length - 1) limits[i] = upperLimit - limits[i];
            else limits[i] = limits[i+1] - limits[i];
        }
        
        // Get random door
        float random = getRandom(upperLimit);
        for(int i = limits.length - 1; i >= 0; i--){
            if(i == limits.length - 1){
                if(upperLimit > random && random > limits[i]) return dirs[i];
            }else{
                if(limits[i+1] > random && random >= limits[i]) return dirs[i];
            }
        }
        
        return null;
    }
    
    private float getRandom(float upperLimit){
        return (float) (Math.random() * upperLimit);
    }
    
    /**
     * 
     * @return the door with the least probability of all
     */
    protected String getDoorLeastProb(ArrayList<String> possibleDoors){
        ArrayList<String> list = new ArrayList<>();
        float prob = -1;
        for(int i = 0; i < probabilities.length; i++){
            if(possibleDoors.contains(directions[i])){
                prob = probabilities[i];
                break;
            }
        }
        if(prob == -1) throw new IllegalStateException("No door found");
        list.add(directions[0]);
        for(int i = 1; i < probabilities.length; i++){
            if(probabilities[i] == prob) list.add(directions[i]);
        }

        return list.get((int)(Math.random() * list.size()));
    }
    
    @Override
    public String toString() {
        String toPrint = new String();
        for(int i = 0; i < probabilities.length; i++){
            toPrint += directions[i] + ": " + probabilities[i] + ", ";
        }
        return toPrint;
    }
    
}
