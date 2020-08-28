/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antoniobaena.randomlevelgenerator;

import antoniobaena.randomlevelgenerator.utils.Room;
import antoniobaena.randomlevelgenerator.utils.RoomInterface;
import java.util.ArrayList;


/**
 *
 * @author antonio
 */
public class Main {
    public static void main(String[] args){
        ArrayList<Room> specialRooms = new ArrayList<>();
        specialRooms.add(RoomInterface.createCustomRoom("Treasure", 1, 1));
        new RandomLevelGenerator(30, 40, specialRooms, 30, 60);
    }
}
