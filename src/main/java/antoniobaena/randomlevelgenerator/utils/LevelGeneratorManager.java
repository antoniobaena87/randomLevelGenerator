/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antoniobaena.randomlevelgenerator.utils;

import antoniobaena.randomlevelgenerator.RandomLevelGenerator;
import java.util.ArrayList;

/**
 *
 * @author antonio
 */
public interface LevelGeneratorManager {
    
    public void generateGrid();
    
    public void setStartingRoom();
    
    public void setARoom();
    
    public boolean canSetRoom();
    
    /**
     * The level is returned to the user as a String[]. Each value contains the basic information of a level in the following format:<p>
     * [positionx, positiony]PossibleDoors.RoomLabel<p>
     * 
     * For instance:<p>
     * [21,28]ES.Normal -> Room located at position x: 21 and y: 28 (top-left corner to bottom-right oriented)
     * and it has two doors, one at north position and another at east position. This is a Normal room.<p>
     * [10,15]NEW.Treasure -> Room located at position x: 10 and y: 15 and it has three doors: North, Easth and West.
     * This a Treasure room, which would be a user defined label.<p>
     * 
     * The predefined labels are N: Normal; and S: Starting point. Users can pass custom labeled rooms via RandomLevelGenerator.
     * 
     * @return The level as an array of rooms in String format.
     * @see RandomLevelGenerator
     */
    public String[] getLevel();
}
