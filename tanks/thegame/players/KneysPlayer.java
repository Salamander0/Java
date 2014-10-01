/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thegame.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;

/**
 *
 * @author david
 */
public class KneysPlayer implements IPlayer{
    private final int edgeDodgeDegree = 160;
    
    public int calculate(int kvadrant, int uhel){
        if (kvadrant == 2) {
            uhel = 180 - uhel;
        } else if (kvadrant == 3) {
            uhel = 180 + uhel;
        } else if (kvadrant == 4) {
            uhel = 360 - uhel;
        }
        
        return uhel;
    }
    
    @Override
    public Command planNextMove(Info info) {
        Command rCommand = null;
        if ((rCommand = dodgeEdges(info)) != null)
            return rCommand; 

        for (InfoDetail enemy : info.getEnemies())
        {                       
            int xVzdalenost = Math.abs(enemy.getX() - info.getX()) + (new Random().nextInt((int) (GameData.TANK_SIZE * 0.95)) - (int)(GameData.TANK_SIZE *  0.95 / 2));
            int yVzdalenost = Math.abs(enemy.getY() - info.getY()) + (new Random().nextInt((int) (GameData.TANK_SIZE * 0.95)) - (int)(GameData.TANK_SIZE *  0.95 / 2));
            int prepona = (int) Math.sqrt(Math.pow(xVzdalenost, 2) + Math.pow(yVzdalenost, 2));
            
            int kvadrant;
            
            if (enemy.getX() - info.getX() > 0)
                if (enemy.getY() - info.getY() > 0)
                    kvadrant = 4;
                else
                    kvadrant = 1;
            else
                if (enemy.getY() - info.getY() > 0)
                    kvadrant = 3;
                else
                    kvadrant = 2;
            
            int uhel = (int) Math.toDegrees(Math.acos((float)xVzdalenost / prepona));
            
  
            rCommand = new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, calculate(kvadrant, uhel) + info.getDirection(), CommandType.SHOOT);
        }
       
        /*
        for (InfoDetail grenade : info.getGrenades())
        {  
            int xVzdalenost = Math.abs(grenade.getX() - info.getX());
            int yVzdalenost = Math.abs(grenade.getY() - info.getY());
            int prepona = (int) Math.sqrt(Math.pow(xVzdalenost, 2) + Math.pow(yVzdalenost, 2));
            
            //int kvadrant;
            int uhel;
            
            if (grenade.getX() - info.getX() > 0)
                if (grenade.getY() - info.getY() > 0) {
                    //kvadrant = 4;
                    uhel = (int) Math.toDegrees(Math.acos((float)xVzdalenost / prepona));
                } else {
                    //kvadrant = 1;
                    uhel = (int) Math.toDegrees(Math.acos((float)yVzdalenost / prepona));
                }
            else {
                if (grenade.getY() - info.getY() > 0){
                    //kvadrant = 3;
                    uhel = (int) Math.toDegrees(Math.acos((float)xVzdalenost / prepona));
                } else {
                    //kvadrant = 2;
                    uhel = (int) Math.toDegrees(Math.acos((float)yVzdalenost / prepona));
                }
            }
            
            
            //int uhel = (int) Math.toDegrees(Math.acos((float)xVzdalenost / prepona));
            
            if (grenade.getDirection() == uhel)
                System.out.println("aarght");
            
            System.out.println(uhel);
            System.out.println(grenade.getDirection());
        }*/       
            
        
        if(rCommand == null)
            rCommand = new Command(CommandType.NONE, CommandType.NONE, 0, CommandType.NONE);
            
        return rCommand;
    }
    
    private Command dodgeEdges(Info info){
        Command rCommand = null;
        
        if (info.getX() + GameData.FORWARD_SPEED + GameData.TANK_SIZE / 2 >= GameData.GAME_WIDTH) {
            rCommand = new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, edgeDodgeDegree, CommandType.NONE);
        } else if (info.getX() - GameData.FORWARD_SPEED - GameData.TANK_SIZE / 2 <= 0) {
            rCommand = new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, edgeDodgeDegree, CommandType.NONE);
        } else if (info.getY() + GameData.FORWARD_SPEED + GameData.TANK_SIZE / 2 >= GameData.GAME_HEIGTH) {
            rCommand = new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, edgeDodgeDegree, CommandType.NONE);
        } else if (info.getY() - GameData.FORWARD_SPEED - GameData.TANK_SIZE / 2 <= 0) {
            rCommand = new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, edgeDodgeDegree, CommandType.NONE);
        }
        
        return rCommand;
    }
    
    @Override
    public String getName() {
        return "Kneys KNE0026";
    }
    
}
