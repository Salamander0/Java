/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import thegame.info.Info;
import thegame.model.Command;
import thegame.model.CommandType;

/**
 *
 * @author beh01
 */
public class StupidPlayer implements IPlayer{

    @Override
    public Command planNextMove(Info info) {
        return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.SHOOT);
    }

    @Override
    public String getName() {
        return "Stupid";
    }
    
}
