/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.model;

/**
 *
 * @author beh01
 */
public class Command {

    public Command(CommandType move, CommandType rotate, int angle,  CommandType shoot) {
        this.move = move;
        this.rotate = rotate;
        this.shoot = shoot;
        this.angle =angle;
    }

    public CommandType getMove() {
        return move;
    }

    public CommandType getRotate() {
        return rotate;
    }

    public CommandType getShoot() {
        return shoot;
    }

    public int getAngle() {
        return angle;
    }

    private CommandType move, rotate, shoot;
    private int angle;
}
