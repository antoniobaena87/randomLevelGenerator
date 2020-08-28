/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antoniobaena.randomlevelgenerator.utils;

/**
 *
 * @author antonio
 */
public interface GridInterface {
    
    Room getRoom(int x, int y);
    
    Room getRandomRoomToPlace();
    
    String[] getLevel();
}
