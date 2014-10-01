/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thegame.players;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;
import thegame.model.Tank;

/**
 *
 * @author Lubomír
 */
public class Strigo33 implements IPlayer {
    
    private Info info;
    private Tank me = null;
    private ArrayList<Integer> allDistances = new ArrayList<Integer>();
    private double granateTime;
    private int distance;
    private Tank nearestEnemy;
    private int deltaX;
    private int deltaY;
    private int enemySpeed;
    private Tank firstMesurement = new Tank(null, -1, -1, -1);
    private int targetRtation;
    
    
    private ArrayList<Integer> myDirection = new ArrayList<Integer>();
    private ArrayList<Integer> granateDistance = new ArrayList<Integer>();
    private Tank nemesis;
    
    private boolean prepare = false;
    private boolean danger = false;
   
    private int splash[] = {0, -1, 1};
    private int itersplash = -1;
    private int calc = 3;
    private Timer t;
    private ActionListener listen;
    private boolean dont = false;
   
    
    @Override
    public String getName() {
        return "Strigo33";
    }
    
    public void printInfo() {
        System.out.println("---------------------");
        for(InfoDetail i : info.getGrenades()) {            
            System.out.printf("x: %d, y: %d, direction: %d\n", i.getX(), i.getY(), i.getDirection());
        }
    }
    
    @Override
    public Command planNextMove(Info info) {
        this.info = info;
        
        me = new Tank(null, info.getX(), info.getY(), info.getDirection());
        nearestEnemy();
        senceDanger();
        granateDistance();
        granateTime();
        enemySpeed();
        hang();
        aimEnemy();
        Command c = commandLogic();
        myDirection.clear();
        allDistances.clear();
        granateDistance.clear();
        danger = false;
        return c;
    }
    
    public void senceDanger() {
        aimMyTank();
        for(int i=0; i<myDirection.size(); i++) {
            int temp = info.getEnemies().get(i).getDirection();
            if(temp <= myDirection.get(i) + 10) {
                if(temp >= myDirection.get(i) % 360 - 10) {
                    danger = true;
                    int x = info.getEnemies().get(i).getX();
                    int y = info.getEnemies().get(i).getY();
                    int dir = info.getEnemies().get(i).getDirection();
                    nemesis = new Tank(null, x, y, dir);
                }
            }
        }
    }
    
    public void enemySpeed() {
        Tank secondMeasurement = nearestEnemy;
        deltaX = firstMesurement.getX() - secondMeasurement.getX();
        deltaY = firstMesurement.getY() - secondMeasurement.getY();
        int track = (int)Math.sqrt((deltaX*deltaX) + (deltaY*deltaY));
        double time = 1000/800;
        enemySpeed = (int)(track / time);
        if(enemySpeed > 2 ) enemySpeed = 4;
        else if(enemySpeed > 0) enemySpeed = 2;
        else enemySpeed = 0;
        firstMesurement = secondMeasurement;
    }
    
    
    
    public void nearestEnemy() {
        Tank enemy = new Tank(null, 0, 0, 0);
        int minDistance = (int)Math.abs(Math.sqrt((GameData.GAME_WIDTH * GameData.GAME_WIDTH) + (GameData.GAME_HEIGTH * GameData.GAME_HEIGTH)));
        for(InfoDetail i : info.getEnemies()) {
            int xDist = me.getX() - i.getX();
            int yDist = me.getY() - i.getY();
            int dist = (int)Math.abs(Math.sqrt((xDist*xDist) + (yDist*yDist)));
            allDistances.add(dist);
            if(dist < minDistance) {
                minDistance = dist;
                enemy = new Tank(null, i.getX(), i.getY(), i.getDirection());
            }
        }
        this.distance = minDistance;
        this.nearestEnemy = enemy;
    }
    
    public void aimEnemy() {
        int x = Math.abs(me.getX() - nearestEnemy.getX());
        int y = Math.abs(me.getY() - nearestEnemy.getY());
        double result = Math.toDegrees(Math.atan((double)y / (double)x));
        if(nearestEnemy.getX() <= me.getX() && nearestEnemy.getY() <= me.getY()) {
            result = 180 + result;            
        }
        else if(nearestEnemy.getX() <= me.getX() && nearestEnemy.getY() >= me.getY()){
            result = 180 - result;
        }
        else if(nearestEnemy.getX() >= me.getX() && nearestEnemy.getY() <= me.getY()){
            result = 360 - result ;
        }
        result += 0.5;
        targetRtation = (int)(me.getDirection() - result);
    }
    
    public void hang() {
        deltaX *= granateTime;
        deltaY *= granateTime;
        
        int newX = nearestEnemy.getX() - deltaX;
        int newY = nearestEnemy.getY() - deltaY;
        Tank newCoordinates = new Tank(null, newX, newY, nearestEnemy.getDirection());
        nearestEnemy = newCoordinates;
        int xDist = me.getX() - newCoordinates.getX();
        int yDist = me.getY() - newCoordinates.getY();
        distance = (int)Math.abs(Math.sqrt((xDist*xDist) + (yDist*yDist)));
    }
    
    public void granateDistance() {
        for(InfoDetail i : info.getGrenades()){
            int xDist = me.getX() - i.getX();
            int yDist = me.getY() - i.getY();
            int dist = (int)Math.abs(Math.sqrt((xDist*xDist) + (yDist*yDist)));
            granateDistance.add(dist);
        }
    }
    
    public Command commandLogic() {
        for(int i : granateDistance) {
            if(i <= 70) {
                return new Command(CommandType.NONE, CommandType.TURN_LEFT, 5, Acommand3());
            }
        }
        if(danger) {
            return emergencyLogic();
        }
        
        if(info.getEnemies().isEmpty()) {
            return new Command(CommandType.NONE, CommandType.NONE, 5, CommandType.NONE);
        }
        
        if(me.getX() < 150 || me.getX() > GameData.GAME_WIDTH - 150)
        {
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, (me.getDirection() - 180) % 360, Acommand3());
        }
        
        if(me.getX() < 150 || me.getX() > GameData.GAME_WIDTH - 150)
        {
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, (me.getDirection() - 180) % 360, Acommand3());
        }
        
        if(distance <= 100) {
            return new Command(CommandType.MOVE_BACK, CommandType.TURN_LEFT, targetRtation, Acommand3());
        }
        return agresiveLogic();
    }
    
    public void aimMyTank() {
        for(InfoDetail i : info.getEnemies()) {
            int n = 0;
            double x = Math.abs(i.getX() - me.getX());
            
            int result = (int)(Math.toDegrees(Math.acos(x / allDistances.get(n))));
            if(me.getX() <= i.getX() && me.getY() <= i.getY()) {
                result = 180 + result;
            }
            else if(me.getX() < i.getX() && me.getY() > i.getY()){
                result = 180 - result;
            }
            else if(me.getX() >= i.getX() && me.getY() <= i.getY()){
                result = 360 - result;
            }
            myDirection.add(result);
            n++;
        }
    }
    
    public Command emergencyLogic() {
        
        if(Math.abs(nemesis.getDirection() - me.getDirection()) <= 220 && Math.abs(nemesis.getDirection() - me.getDirection()) > 140) {
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, 5, Acommand3());
        }
        if(nemesis.getX() > me.getX()) {
            return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 5, Acommand3());
        }
        return agresiveLogic();
    }
    
    
    
    public Command agresiveLogic() {
        if(me.canShoot()) {
            if(distance > 1500) {
                return new Command(Acommand1(), CommandType.TURN_LEFT, targetRtation, Acommand3());
            }
            else {
                if(distance > 1200)calc = 1;
                itersplash = (itersplash + 1) % 3;
                Command c =  new Command(Acommand1(), CommandType.TURN_LEFT, targetRtation + splash[itersplash] * calc, CommandType.SHOOT);
                calc = 3;                
                return c;
            }
        }
        return new Command(Acommand1(), CommandType.TURN_LEFT, targetRtation, Acommand3());        
    }
    
    public CommandType Acommand1() {
        return CommandType.MOVE_FORWARD;
    }
    
    
    public CommandType Acommand3() {
       if(prepare) {
           prepare = false;
           return CommandType.SHOOT;
       }
       return CommandType.NONE;               
   }
    
   public void granateTime() {
        granateTime = ((double)distance / GameData.GRENADE_SPEED);
   }
    
}
    
    
    
