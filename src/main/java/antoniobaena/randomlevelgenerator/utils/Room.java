/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antoniobaena.randomlevelgenerator.utils;

import java.util.ArrayList;

/**
 *
 * @author antonio
 */
public class Room implements RoomInterface{
    private int x;
    private int y;
    private String label;
    private ArrayList<String> doors = new ArrayList<>();
    
    private int minDoors;
    private int maxDoors;
    
    protected Room(int x, int y, String enterPoint, String label){
        this.x = x;
        this.y = y;
        if(enterPoint != null) this.doors.add(enterPoint);
        this.label = label;
    }
    
    protected Room(String Label, int minDoors, int maxDoors){
        this.minDoors = minDoors;
        this.maxDoors = maxDoors;
        this.label = label;
    }
    
    protected void addDoor(String door){
        if(doors.contains(door)) throw new IllegalStateException("The room [" + x + ", " + y + "] already has the door " + door);
        if(Doors.isValidDoor(door)) doors.add(door);
        else throw new IllegalArgumentException("Door " + door + " isn't a valid door.");
    }
    
    protected ArrayList<String> removeAlreadySetDoors(ArrayList<String> doorOptions){
        if(doors.size() == 0) return doorOptions;
        for(String door:doors){
            if(doorOptions.contains(door)) doorOptions.remove(door);
        }
        
        return doorOptions;
    }
    
    protected String[] getDoors(){
        return doors.toArray(new String[doors.size()]);
    }
    
    protected boolean hasDoor(String door){
        return doors.contains(door);
    }
    
    protected int getX(){
        return x;
    }
    
    protected int getY(){
        return y;
    }
    
    protected String getLabel(){
        return label;
    }
    
    protected String getStringRepresentation(boolean showCoordinates, boolean showFullLabel){
        String room = new String();
        if(showCoordinates) room += "["+ y + "," + x + "]";
        for(String door:new String[]{"N","E","S","W"}){
            if(doors.contains(door)) room += door;
        }
        String showLabel = showFullLabel ? label : label.substring(0, 1);
        room += "." + showLabel;
        
        return room;
    }
}
