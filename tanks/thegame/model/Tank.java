/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.model;

import thegame.players.IPlayer;

/**
 *
 * @author beh01
 */
public class Tank extends ModelObject {

    private ModelPlayer player;
    private int shootTimer;

    public Tank(ModelPlayer player, int x, int y, int direction) {
        super(x, y, direction);
        this.player = player;
    }

    public ModelPlayer getPlayer() {
        return player;
    }
    public boolean canShoot() {
        return shootTimer==0;
    }
    public Grenade doCommand(Command command) {
        Grenade grenade = null;

        if (command.getRotate() == CommandType.TURN_LEFT) {
            addToDirection(-command.getAngle());
        }
        if (command.getRotate() == CommandType.TURN_RIGHT) {
            addToDirection(command.getAngle());
        }

        if (command.getShoot() == CommandType.SHOOT) {
            if (shootTimer == 0) {
                grenade = new Grenade(this, getX(), getY(), getDirection());
                grenade.move();
                grenade.move();
                player.addShoot();
                shootTimer=GameData.SHOOT_TIMER;
            }
        }

        if (command.getMove() == CommandType.MOVE_FORWARD) {
            moveForward();
        }
        if (command.getMove() == CommandType.MOVE_BACK) {
            moveBack();
        }
        if (shootTimer > 0) {
            shootTimer--;
        }
        return grenade;
    }

    public double getRotation() {
        return Math.PI * getDirection() / 180.0;
    }

    private void moveForward() {

        int x = (int) (Math.round(GameData.FORWARD_SPEED * Math.cos(getRotation())));
        int y = (int) (Math.round(GameData.FORWARD_SPEED * Math.sin(getRotation())));
        //System.out.println(""+x+", "+y+" - "+rotation+"="+getRotation());
        move(x, y);

    }

    private void moveBack() {
        double angle = getRotation() + Math.PI;
        int x = (int) (Math.round(GameData.BACWARD_SPEED * Math.cos(angle)));
        int y = (int) (Math.round(GameData.BACWARD_SPEED * Math.sin(angle)));
        move(x, y);
    }
}
