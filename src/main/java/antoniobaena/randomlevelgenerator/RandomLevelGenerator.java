/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antoniobaena.randomlevelgenerator;

import antoniobaena.randomlevelgenerator.utils.LevelGeneratorManager;
import antoniobaena.randomlevelgenerator.utils.Manager;
import antoniobaena.randomlevelgenerator.utils.Room;
import antoniobaena.randomlevelgenerator.utils.RoomInterface;
import java.util.ArrayList;

/**
 *
 * @author antonio
 */
public class RandomLevelGenerator {
    
    LevelGeneratorManager manager;
    
    /**
     * 
     * @param minRooms
     * @param maxRooms
     * @param specialRooms Array of Room objects containing user defined rooms. A user can define new rooms by constructing them with the following
     * method from RoomInterface: Room room = RoomInterface.createCustomRoom(String label);
     * @param intertwined From 0 to 100, defines how likely different paths are to be interconnected (100 max interconnection)
     * @param thickness From 0 to 100, defines how spread the level is (0 would mean a single corridor in a single direction)
     * 
     * *Note that intertwined and thickness work together. A value of intertwined = 100 means nothing if thickness = 0.
     * 
     * @see RoomInterface
     */
    public RandomLevelGenerator(int minRooms, int maxRooms, ArrayList<Room> specialRooms, float intertwined, float thickness){
        if(specialRooms == null) specialRooms = new ArrayList<>();
        
        manager = new Manager(minRooms, maxRooms, specialRooms, intertwined, thickness);
        manager.generateGrid();
        manager.setStartingRoom();
        
        while(manager.canSetRoom()){
           manager.setARoom();
        }
        
        String[] level = manager.getLevel();
        System.out.println("Printing level...");
        for(String room:level) System.out.println(room);
    }
}
