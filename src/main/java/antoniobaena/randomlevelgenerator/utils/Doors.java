/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antoniobaena.randomlevelgenerator.utils;

/**
 * Class containing some util methods for handling doors.
 * @author antonio
 */
class Doors {
    static final String north = "N";
    static final String south = "S";
    static final String east = "E";
    static final String west = "W";
    
    static boolean isValidDoor(String door){
        if(door.equals(Doors.north) || door.equals(Doors.south) || door.equals(Doors.east) || door.equals(Doors.west)) return true;
        return false;
    }
    
    static String getOppositeDoor(String door){
        if(door.equals(Doors.north)) return Doors.south;
        else if(door.equals(Doors.south)) return Doors.north;
        else if(door.equals(Doors.east)) return Doors.west;
        else if(door.equals(Doors.west)) return Doors.east;
        
        return "Error";
    }
}
