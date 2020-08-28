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
class Grid implements GridInterface{
    
    private Room[][] grid;
    private int totalRooms;
    private float intertwined;
    private ExitsChooser exitsChooser;
    
    private ArrayList<Room> specialRooms;
    private ArrayList<Room> roomsToPlace = new ArrayList<>();
    private ArrayList<Room> placedRooms = new ArrayList<>();
    
    /**
     * 
     * @param minRooms
     * @param maxRooms
     * @param specialRooms
     * @param intertwined 0 to 1 The higher, the more rooms that will be connected to each other.
     */
    protected Grid(int minRooms, int maxRooms, ArrayList<Room> specialRooms, float intertwined, float thickness){
        this.intertwined = intertwined;
        this.specialRooms = specialRooms;
        totalRooms = calculateTotalRooms(minRooms, maxRooms);
        grid = new Room[totalRooms * 2][totalRooms * 2];
        exitsChooser = ExitsChooser.construct(this, thickness, intertwined);
    }
    
    private int calculateTotalRooms(int minRooms, int maxRooms){
        // Check for inconsistencies
        if(minRooms > maxRooms) throw new IllegalArgumentException("minRooms can't be higher than maxRooms");
        if(maxRooms < specialRooms.size() + 2) throw new IllegalArgumentException("maxRoom can't be lower than all the asked specialRooms plus the starting and ending points");
        
        return (int)((Math.random() * ((maxRooms - minRooms) + 1)) + minRooms);
    }
    
    protected int getNumAvailableRooms(){
        // Don't use roomsToPlace.size() here because those rooms already are inside placedRooms
        return totalRooms - placedRooms.size();
    }
    
    protected int getTotalRooms(){
        return totalRooms;
    }
    
    @Override
    public Room getRoom(int x, int y){
        return grid[y][x];
    }
    
    /**
     * 
     * @return 1 if roomsToPlace list is empty
     */
    protected int getMinDoors(){
        if(roomsToPlace.isEmpty()) return 1;
        return 0;
    }
    
    protected void setStartingRoom(ExitsChooser exitsChooser){
        int x = totalRooms;
        int y = x;
        grid[y][x] = new Room(x, y, null, "Starting");
        
        placedRooms.add(grid[y][x]);
        ArrayList<String> addedDoors = exitsChooser.setDoors(x, y, this.getNumAvailableRooms(), getGridSize(), getMinDoors());
        for(String door:addedDoors){
            addNewRoom(x, y, Doors.getOppositeDoor(door));
        }
    }
    
    private int getGridSize(){
        return grid.length;
    }
    
    /**
     * Rooms are added when the room that is currently being placed sets a new door. This is because
     * it is at that precise moment when we know for sure we need to place a new room so that door has somewhere to get us.
     * For example, if the room grid[5][3] is being placed, and a door to the north is set, that means there must be a new room
     * in the location grid[5][2] if there isn't any yet. If there is already a room there and we want to connect the two, then add a door.
     * 
     * @param x The x coordinate of the room that placed a door to this new room to add
     * @param y The y coordinate of the room that placed a door to this new room to add
     * @param comingFrom The direction from where this rooms is being created. If this room is created from the west door of the next room, then comingFrom == "E"
     */
    protected void addNewRoom(int x, int y, String comingFrom){
        if(comingFrom.equals(Doors.north)) x += 1;
        else if(comingFrom.equals(Doors.south)) x -= 1;
        else if(comingFrom.equals(Doors.east)) y -= 1;
        else if(comingFrom.equals(Doors.west)) y += 1;
        
        if(grid[y][x] != null) throw new IllegalStateException("The room [" + x + "," + y + "] already contains a room.");
        
        grid[y][x] = new Room(x, y, comingFrom, setLabel());
        placedRooms.add(grid[y][x]);
        roomsToPlace.add(grid[y][x]);
        // Add in both ArrayList because I need to know where a room is placed, even 
        // if we haven't yet set all its doors
    }
    
    public Room getRandomRoomToPlace(){
        int random = (int) (Math.random() * roomsToPlace.size());
        Room room = roomsToPlace.get(random);
        roomsToPlace.remove(room);
        return room;
    }
    
    private String setLabel(){
        String label = new String();
        label = "Normal";
        return label;
    }
    
    /**
     * Remove empty rows and columns from grid.
     */
    protected void trimLevel(){
        int verticalPositions = 0;
        int horizontalPositions = 0;
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid.length; j++){
                if(grid[j][i] != null){
                    // Row j contains a room
                    break;
                }
                // Row j doesn't contain a room
            }
        }
    }
    
    protected void printLevel(){
        System.out.println("Total rooms: " + totalRooms);
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid.length; j++){
                String label = grid[j][i] == null ? "" : grid[j][i].getStringRepresentation(false, false);
                System.out.print(label + "\t");
            }
            System.out.print("\n");
        }
    }
    
    public String[] getLevel(){
        String[] level = new String[totalRooms];
        int index = 0;
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid.length; j++){
                if(grid[j][i] != null){
                    level[index++] = grid[j][i].getStringRepresentation(true, true);
                }
            }
        }
        
        return level;
    }
}
