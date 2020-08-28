/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antoniobaena.randomlevelgenerator.utils;

import antoniobaena.randomlevelgenerator.utils.Grid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Singleton class
 * @author antonio
 */
class ExitsChooser {
    
    private float thickness;
    private float intertwined;
    private GridInterface grid;
    private DoorsCollection doorsCollection;
    private ArrayList<String> directions = new ArrayList<>(Arrays.asList(Doors.north, Doors.south, Doors.east, Doors.west));
    
    private static ExitsChooser exitsChooser;
    
    private ExitsChooser(Grid grid, float thickness, float intertwined){
        this.thickness = thickness;
        this.intertwined = intertwined;
        this.grid = grid;
        setRatios();
    }
    
    protected static ExitsChooser construct(Grid grid, float thickness, float intertwined){
        if(exitsChooser == null){
            exitsChooser = new ExitsChooser(grid, thickness, intertwined);
        }
        return exitsChooser;
    }
    
    private void setRatios(){
        // Randomly choose a direction that will serve as our reference to calculate everything
        String mainDirection = directions.get((int) (Math.random() * 4));
        // Based on mainDirection, decide the secondary direction
        String secondaryDirection = new String();
        if(mainDirection.equals(Doors.north) || mainDirection.equals(Doors.south)){
            secondaryDirection = directions.get((int) (Math.random() * ((3 - 2) + 1) + 2));
        }else{
            secondaryDirection = directions.get((int) (Math.random() * 2));
        }
        directions.remove(mainDirection);
        directions.remove(secondaryDirection);
        // Set ratios accordingly
        HashMap<String, Float> exits = new HashMap<>();
        //exits.put(mainDirection, 100 - thickness);
        System.out.println("main: " + mainDirection);
        System.out.println("secondary: " + secondaryDirection);
        exits.put(mainDirection, (-0.75f * thickness + 100f));
        exits.put(secondaryDirection, ((100 - (-0.75f * thickness + 100)) / 2));
        for(String remainingDirection:directions) exits.put(remainingDirection, (100 - (exits.get(mainDirection) + exits.get(secondaryDirection))) / 2);
        doorsCollection = new DoorsCollection(exits);
    }
    
    /**
     * Given a coordinate within the rooms grid, this method sets the exits that room will have.
     * @param room
     * @param numAvailableRooms The number of rooms that we can still add to the grid.
     */
    protected ArrayList<String> setDoors(int x, int y, int numAvailableRooms, int gridSize, int minDoors){
        ArrayList<String> addedDoors = new ArrayList<>();
        
        Room currentRoom = grid.getRoom(x, y);
        ArrayList<String> possibleDoors = setPossibleDoors(currentRoom, minDoors, gridSize, intertwined);
        
        System.out.println("Setting doors for room [" + x  + ", " + y + "]");
        System.out.print("Options: ");
        for(String s:possibleDoors) System.out.print(s + " ");
        System.out.println("");
        
        while(numAvailableRooms < possibleDoors.size()){
            // trim options
            System.out.println("Removing door: " + doorsCollection.getDoorLeastProb(possibleDoors));
            possibleDoors.remove(doorsCollection.getDoorLeastProb(possibleDoors));
        }
        if(!possibleDoors.isEmpty()){
            // Set number of doors
            int numDoors = setNumDoors(minDoors, possibleDoors.size(), thickness);
            
            for(int i = 0; i < numDoors; i++){
                System.out.println("Selecting door for room [" + x + ", " + y + "]... Ratios: " + doorsCollection.toString() + ". Thickness: " 
                        + thickness + ". Intertwined: " + intertwined + ". numDoors: " + numDoors);
                System.out.print("Options: ");
                for(String s:possibleDoors) System.out.print(s + " ");
                System.out.println("");
               
                String door = doorsCollection.getRandomDoor(possibleDoors);
                if(door == null && minDoors == 1) throw new IllegalStateException("No doors chosen and one door was needed.");
                if(door == null){
                    System.out.println("****No door selected****\n");
                    break;
                }
                System.out.println("Selected door: " + door + "\n");
                possibleDoors.remove(door);
                currentRoom.addDoor(door);
                // check if some of those doors intertwine with an already placed door
                boolean intertwined = false;
                switch(door){
                    case Doors.north:
                        if(grid.getRoom(x-1, y) != null){
                            System.out.println("Intertwined from " + Doors.getOppositeDoor(door));
                            grid.getRoom(x-1, y).addDoor(Doors.getOppositeDoor(door));
                            intertwined = true;
                        }
                        break;
                    case Doors.south:
                        if(grid.getRoom(x+1, y) != null){
                            System.out.println("Intertwined from " + Doors.getOppositeDoor(door));
                            grid.getRoom(x+1, y).addDoor(Doors.getOppositeDoor(door));
                            intertwined = true;
                        }
                        break;
                    case Doors.east:
                        if(grid.getRoom(x, y+1) != null){
                            System.out.println("Intertwined from " + Doors.getOppositeDoor(door));
                            grid.getRoom(x, y+1).addDoor(Doors.getOppositeDoor(door));
                            intertwined = true;
                        }
                        break;
                    case Doors.west:
                        if(grid.getRoom(x, y-1) != null){
                            System.out.println("Intertwined from " + Doors.getOppositeDoor(door));
                            grid.getRoom(x, y-1).addDoor(Doors.getOppositeDoor(door));
                            intertwined = true;
                        }
                        break;
                }
                if(!intertwined){
                    addedDoors.add(door);
                    minDoors--;  // if there was a need for a door, that need is no more
                }
            }
        }
        return addedDoors;
    }
    
    /**
     * 
     * @param minDoors
     * @param possibleDoorsSize used to know the maximum number of doors this room can have
     * @param thickness
     * @return 
     */
    private static int setNumDoors(int minDoors, int possibleDoorsSize, float thickness){
        System.out.println("Setting numDoors...");
        int numDoors = 0;
        int maxDoors = (int) (0.03f * thickness + 1);
        if(maxDoors > possibleDoorsSize) maxDoors = possibleDoorsSize;
        System.out.println("minDoors: " + minDoors + ". maxDoors: " + maxDoors);
        //if(maxDoors < minDoors) numDoors = minDoors;
        numDoors = (int) (Math.random() * ((maxDoors - minDoors) + 1) + minDoors);
        System.out.println("numDoors: " + numDoors);
        
        return numDoors;
    }
    
    private ArrayList<String> setPossibleDoors(Room currentRoom, int minDoors, int gridSize, float intertwined){
        int x = currentRoom.getX();
        int y = currentRoom.getY();
        ArrayList<String> possibleDoors = new ArrayList<>();
        
        Room northRoom = x == 0 ? null : grid.getRoom(x-1, y);
        Room eastRoom = y == gridSize ? null : grid.getRoom(x, y+1);
        Room southRoom = x == gridSize ? null : grid.getRoom(x+1, y);
        Room westRoom = y == 0 ? null : grid.getRoom(x, y-1);
        
        if(x != 0 && !currentRoom.hasDoor(Doors.north)){
            int random = (int)(Math.random() * 101);
            if(northRoom != null && random < intertwined){
                System.out.println("Added possible interwining in room [" + northRoom.getX() + "," + northRoom.getY() + "]");
                possibleDoors.add(Doors.north);
            }else if(northRoom == null){
                possibleDoors.add(Doors.north);
            }
        }
        if(y != gridSize && !currentRoom.hasDoor(Doors.east)){
            int random = (int)(Math.random() * 101);
            if(eastRoom != null && random < intertwined){
                System.out.println("Added possible interwining in room [" + eastRoom.getX() + "," + eastRoom.getY() + "]");
                possibleDoors.add(Doors.east);
            }else if(eastRoom == null){
                possibleDoors.add(Doors.east);
            }
        }
        if(x != gridSize && !currentRoom.hasDoor(Doors.south)){
            int random = (int)(Math.random() * 101);
            if(southRoom != null && random < intertwined){
                System.out.println("Added possible interwining in room [" + southRoom.getX() + "," + southRoom.getY() + "]");
                possibleDoors.add(Doors.south);
            }else if(southRoom == null){
                possibleDoors.add(Doors.south);
            }
        }
        if(westRoom == null && y != 0 && !currentRoom.hasDoor(Doors.west)){
            int random = (int)(Math.random() * 101);
            if(westRoom != null && random < intertwined){
                System.out.println("Added possible interwining in room [" + westRoom.getX() + "," + westRoom.getY() + "]");
                possibleDoors.add(Doors.west);
            }else if(westRoom == null){
                possibleDoors.add(Doors.west);
            }
        }
        if(possibleDoors.isEmpty() && minDoors == 1) throw new IllegalStateException("No doors to place but at least one door needs to be placed");
        
        return possibleDoors;
    }
}
