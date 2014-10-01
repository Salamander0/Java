/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.Iterator;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;

/**
 *
 * @author Patrik
 */
public class Dificult implements IPlayer {
    private int finish = 0;
    private int Angle = 0;
    private int type = 0;
    String name = "Dificult";
    private InfoDetail nextKill = null;

    @Override
    public Command planNextMove(Info info) {
        setCommand(info);
        return new Command(moveForward(), moveRotate(), Angle, Shoot());
        //return new Command(CommandType.NONE, CommandType.NONE, 0, CommandType.NONE);
    }
    
    @Override
    public String getName() {
        return name;
    }
    private boolean left;
    private boolean shoot;

    private void setCommand(Info info) {
        Angle = 0;
        FindNextTank(info);
        int angle = RotateToKill(info);
        int distance = Distance(info);

        if (angle > info.getDirection()) {
            Angle = angle - info.getDirection();
            if (distance > 200) {
                type = 1;
            } else {
                type = 0;
            }
            left = false;
        } else if (angle == info.getDirection()) {
            shoot = true;

        } else {
            Angle = info.getDirection() - angle;
            left = true;
        }
        if (distance > 500) {
            shoot = false;
        } else {
            shoot = true;
        }
        if(finish == 1)
        {
            shoot = false;
            Angle = 90;
            type = 0;
            name = "WINNER";
        }
    }
    private int number = -1;

    private CommandType Shoot() {
        if (shoot) {
            return CommandType.SHOOT;
        } else {
            return null;
        }

    }

    private void FindNextTank(Info info) {
        number =-1;
        float minDistance = 5000;
        float Distance = 0;
        Iterator iterator;
        iterator = info.getEnemies().iterator();
        while (iterator.hasNext()) {
            iterator.next();
            number++;
        }
        if(number == -1)
        {
            finish = 1;
        }
        for (; number >= 0; number--) {
            InfoDetail tank = info.getEnemies().get(number);
            int x = Math.abs(info.getX() - tank.getX());
            int y = Math.abs(info.getY() - tank.getY());
            Distance = (float) Math.sqrt((x * x) + (y * y));
            if (Distance < minDistance) {
                minDistance = Distance;
                nextKill = tank;
            }
        }
    }

    private int RotateToKill(Info info) {
        double x = Math.abs(info.getX() - nextKill.getX());
        double y = Math.abs(info.getY() - nextKill.getY());
        double distance = Math.sqrt((x * x) + (y * y));
        double angle;
        angle = Math.asin(x / distance);
        if (info.getY() > nextKill.getY()) {
            if (info.getX() > nextKill.getX()) {
                return 180 + (90 - (int) Math.toDegrees(angle));
            } else {
                return 270 + (int) Math.toDegrees(angle);
            }
        } else {
            if (info.getX() > nextKill.getX()) {
                return 90 + (int) Math.toDegrees(angle);
            } else {
                return 90 - (int) Math.toDegrees(angle);
            }
        }
    }

    private int Distance(Info info) {
        int x = Math.abs(info.getX() - nextKill.getX());
        int y = Math.abs(info.getY() - nextKill.getY());
        return (int) Math.sqrt((x * x) + (y * y));
    }

    private CommandType moveForward() {
        if (type == 1) {
            return CommandType.MOVE_FORWARD;
        } else if (type == (-1)) {
            return CommandType.MOVE_BACK;
        } else {
            return CommandType.NONE;
        }
    }

    private CommandType moveRotate() {
        if (left) {
            return CommandType.TURN_LEFT;
        } else {
            return CommandType.TURN_RIGHT;
        }
    }
}
