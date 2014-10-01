/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.Iterator;
import java.util.Random;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;
import thegame.model.ModelObject;
import thegame.model.Tank;

/**
 *
 * @author Dominik.S
 */
public class Sypula implements IPlayer {

    private InfoDetail tankToKill = null;
    private InfoDetail Granade = null;
    private int id = -1;
    private int foward_back = 1;
    private boolean left = true;   //left
    private int Angle = 0;
    private boolean Shoot = false;

    @Override
    public Command planNextMove(Info info) {
        Random r = new Random();
        setCommand(info);
        return new Command(foeard_back(), left_right(), Angle, Shoot());
    }

    private CommandType foeard_back() {
        if (foward_back == 1) {
            return CommandType.MOVE_FORWARD;
        } else if (foward_back == -1) {
            return CommandType.MOVE_BACK;
        } else {
            return CommandType.NONE;
        }
    }

    private CommandType left_right() {
        if (left) {
            return CommandType.TURN_LEFT;
        } else {
            return CommandType.TURN_RIGHT;
        }
    }

    private CommandType Shoot() {
        if (Shoot) {
            return CommandType.SHOOT;
        } else {
            return CommandType.NONE;
        }
    }

    @Override
    public String getName() {
        return "Sypula";
    }

    private void setCommand(Info info) {
        if (!shootOnMe(info)) {
            findTankToKIll(info);
            int angle = getAngleToTank(info);
            if (angle > info.getDirection()) {
                Angle = angle - info.getDirection();
                left = false;
            } else if (angle == info.getDirection()) {
                Angle = 0;
            } else {
                Angle = info.getDirection() - angle;
                left = true;
            }
            if (getDistanceTToTank(info) < 200) {
                Shoot = true;
                foward_back = 0;
            } else {
                Shoot = true;
                foward_back = 1;
            }


            //System.out.print("");
        }
    }

    private void findTankToKIll(Info info) {
        float minDistance = 5000;
        float tmpDistance;
        int id = 0;
        /*if (this.id < 0) {*/
        Iterator iter;
        iter = info.getEnemies().iterator();
        while (iter.hasNext()) {
            iter.next();
            id++;
        }
        id--;
        for (; id >= 0; id--) {
            InfoDetail tank = info.getEnemies().get(id);
            int x = Math.abs(info.getX() - tank.getX());
            int y = Math.abs(info.getY() - tank.getY());
            tmpDistance = (float) Math.sqrt((x * x) + (y * y));
            if (tmpDistance < minDistance) {
                minDistance = tmpDistance;
                tankToKill = tank;
                this.id = id;
            }
        }

    }

    private int getAngleToTank(Info info) {
        double x = Math.abs(info.getX() - tankToKill.getX());
        double y = Math.abs(info.getY() - tankToKill.getY());
        double distance = Math.sqrt((x * x) + (y * y));
        double angle = 0;
        angle = Math.asin(x / distance);
        if (info.getY() > tankToKill.getY()) {
            if (info.getX() > tankToKill.getX()) {
                return 180 + (90 - (int) Math.toDegrees(angle));
            } else {
                return 270 + (int) Math.toDegrees(angle);
            }
        } else {
            if (info.getX() > tankToKill.getX()) {
                return 90 + (int) Math.toDegrees(angle);
            } else {
                return 90 - (int) Math.toDegrees(angle);
            }

        }
    }

    private int getDistanceTToTank(Info info) {
        int x = Math.abs(info.getX() - tankToKill.getX());
        int y = Math.abs(info.getY() - tankToKill.getY());
        return (int) Math.sqrt((x * x) + (y * y));
    }

    private boolean shootOnMe(Info info) {
        boolean find = false;
        float minDistance = 5000;
        float tmpDistance;
        int id = -1;
        /*if (this.id < 0) {*/
        Iterator iter;
        iter = info.getGrenades().iterator();
        while (iter.hasNext()) {
            iter.next();
            id++;
        }
        for (; id >= 0; id--) {
            //InfoDetail granade = info.getGrenades().get(id);
            int angleMin = getAngleToGranade(info, -GameData.TANK_SIZE / 2, id);
            int angle = getAngleToGranade(info, 0, id);
            int angleMax = getAngleToGranade(info, GameData.TANK_SIZE / 2, id);
            //System.out.println("MIN: "+angleMin+"  MAX: "+angleMax+"  optimal: "+angle);
            int dirGranade = info.getGrenades().get(id).getDirection();
            if ((angleMax > angleMin && angleMax > dirGranade && angleMin < dirGranade)
                    || (angleMax < angleMin && angleMax < dirGranade && angleMin > dirGranade)) {
                //System.out.println("POZOR");
                find = true;
                InfoDetail granade = info.getGrenades().get(id);
                int x = Math.abs(info.getX() - granade.getX());
                int y = Math.abs(info.getY() - granade.getY());
                tmpDistance = (float) Math.sqrt((x * x) + (y * y));
                if (tmpDistance < minDistance) {
                    minDistance = tmpDistance;
                    Granade = granade;
                }

            }
        }
        if (find) {
            int anglePom = Granade.getDirection() - 90;
            if (info.getDirection() > anglePom) {
                Angle = info.getDirection() - anglePom;
                left = true;
                foward_back = 1;
            } else if (info.getDirection() < anglePom) {
                Angle = info.getDirection() + anglePom;
                left = false;
                foward_back = 1;
            } else {
                Angle = 0;
                foward_back = 1;
            }
            return true;
        } else {
            return false;
        }


    }

    private int getAngleToGranade(Info info, int offset, int idGranade) {
        double x = Math.abs((info.getX() + offset) - info.getGrenades().get(idGranade).getX());
        double y = Math.abs((info.getY() + offset) - info.getGrenades().get(idGranade).getY());
        double distance = Math.sqrt((x * x) + (y * y));
        double angle = 0;

        angle = Math.asin(x / distance);
        int pom = (int) Math.toDegrees(angle);
        if (info.getGrenades().get(idGranade).getY() > info.getY() + offset) {
            if (info.getGrenades().get(idGranade).getX() > info.getX() + offset) {
                return 180 + (90 - (int) Math.toDegrees(angle));
            } else {
                return 270 + (int) Math.toDegrees(angle);
            }
        } else {
            if (info.getGrenades().get(idGranade).getX() > info.getX() + offset) {
                return 90 + (int) Math.toDegrees(angle);
            } else {
                return 90 - (int) Math.toDegrees(angle);
            }

        }
    }
}
