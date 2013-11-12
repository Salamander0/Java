/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.Random;
import thegame.info.Info;
import thegame.model.Command;
import thegame.model.CommandType;

/**
 *
 * @author beh01
 */
public class HopelessPlayer implements IPlayer {

    @Override
    public Command planNextMove(Info info) {
        Random r = new Random();
        return new Command(CommandType.NONE, CommandType.TURN_LEFT, r.nextInt(10)+1, CommandType.SHOOT);
    }

    @Override
    public String getName() {
        return "Hopeless";
    }
    
}
