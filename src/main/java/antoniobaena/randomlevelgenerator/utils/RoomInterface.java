/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antoniobaena.randomlevelgenerator.utils;

/**
 * Allows users to define custom rooms from outside this package.
 * @author antonio
 */
public interface RoomInterface {
    
    public static Room createCustomRoom(String customLabel, int minDoors, int maxDoors){
        if(minDoors > maxDoors) throw new IllegalArgumentException("minDoors can't be greater than maxDoors");
        if(customLabel.isEmpty() || customLabel == null) throw new IllegalArgumentException("You must give a label to this room");
        return new Room(customLabel, minDoors, maxDoors);
    }
}
