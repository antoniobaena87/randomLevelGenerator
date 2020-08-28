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
public class Manager implements LevelGeneratorManager{
    
    Grid grid;
    ExitsChooser exitsChooser;
    
    int minRooms, maxRooms;
    float intertwined, thickness;
    ArrayList<Room> specialRooms;
    
    public Manager(int minRooms, int maxRooms, ArrayList<Room> specialRooms, float intertwined, float thickness){
        if(intertwined < 0 || intertwined > 100 || thickness < 0 || thickness > 100) throw new IllegalArgumentException("intertwined and thickness range is [0, 100]");
        this.minRooms = minRooms;
        this.maxRooms = maxRooms;
        this.specialRooms = specialRooms == null ? new ArrayList<>() : specialRooms;
        this.intertwined = intertwined;
        this.thickness = thickness;
    }

    @Override
    public void generateGrid() {
        grid = new Grid(minRooms, maxRooms, specialRooms, intertwined, thickness);
        exitsChooser = ExitsChooser.construct(grid, thickness, intertwined);
    }

    @Override
    @SuppressWarnings("empty-statement")
    public void setStartingRoom() {
        grid.setStartingRoom(exitsChooser);
    }

    @Override
    public void setARoom() {
        Room roomToPlace = grid.getRandomRoomToPlace();
        ArrayList<String> addedDoors = exitsChooser.setDoors(roomToPlace.getX(), roomToPlace.getY(), grid.getNumAvailableRooms(), grid.getTotalRooms(), grid.getMinDoors());
        for(String door:addedDoors) grid.addNewRoom(roomToPlace.getX(), roomToPlace.getY(), Doors.getOppositeDoor(door));
    }

    @Override
    public boolean canSetRoom() {
        if(grid.getNumAvailableRooms() > 0) return true;
        return false;
    }

    @Override
    public String[] getLevel() {
        grid.printLevel();
        return grid.getLevel();
    }
    
}
