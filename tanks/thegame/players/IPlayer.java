/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import thegame.info.Info;
import thegame.model.Command;

/**
 *
 * @author beh01
 */
public interface IPlayer {
    public Command planNextMove(Info info);
    public String getName();
}
