package thegame.players;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jirka
 */
public class MyAI implements IPlayer {
    private class BetterInfoDetail{
        private boolean isMoving;
        private boolean exists;
        private int x;
        private int y;
        private double direction;
        private boolean back;

        public BetterInfoDetail(InfoDetail info) {
            x = info.getX();
            y = info.getY();
            direction = info.getDirection();
            this.isMoving = false;
            this.exists = true;
            back = false;
        } 
    }
    
    private int _x, _y;
    private double _direction;
    private List<BetterInfoDetail> _enemies;
    private List<InfoDetail> _grenades;
    private boolean _first;
    
    
    
    
    public MyAI(){
        _enemies = new ArrayList<BetterInfoDetail>();
        _first = true;
    }
    
    @Override
    public Command planNextMove(Info info) {
        _x = info.getX();
        _y = info.getY();
        _direction = info.getDirection() / 180.0 * Math.PI;
        for(BetterInfoDetail bid : _enemies)
            bid.exists = false;
        for(InfoDetail id :info.getEnemies()){
            boolean found = false;
            for(BetterInfoDetail bid : _enemies) {
                Point tank1 = new Point(id.getX(), id.getY());
                Point tank2 = new Point(bid.x, bid.y);
                if(tank1.distance(tank2) == 0){
                    bid.exists = true;
                    bid.isMoving = false;
                    bid.direction = id.getDirection() / 180.0 * Math.PI;
                    bid.back = false;
                    found = true;
                }
                else if(Math.round(tank1.distance(tank2)) == GameData.FORWARD_SPEED){
                    bid.back = false;
                    bid.exists = true;
                    bid.isMoving = true;
                    bid.direction = id.getDirection() / 180.0 * Math.PI;
                    bid.x = id.getX();
                    bid.y = id.getY();
                    found = true;
                }
                else if(Math.round(tank1.distance(tank2)) == GameData.BACWARD_SPEED){
                    bid.back = true;
                    bid.exists = true;
                    bid.isMoving = true;
                    bid.direction = id.getDirection() / 180.0 * Math.PI;
                    bid.x = id.getX();
                    bid.y = id.getY();
                    found = true;
                }
            }
            if(!found) {
                _enemies.add(new BetterInfoDetail(id));
            }
                
        }
        for(int i = 0; i < _enemies.size(); i++)
            if(!_enemies.get(i).exists)
                _enemies.remove(i);
        _grenades = info.getGrenades();
        
        //
        // UNIK
        //
        for(InfoDetail id : _grenades){
            double direction = id.getDirection() / 180 * Math.PI;
            double x = id.getX() + Math.round(Math.cos(direction) * 750);
            double y = id.getY() + Math.round(Math.sin(direction) * 750);
            if(Line2D.ptSegDist(id.getX(), id.getY(), x, y, _x, _y) < GameData.TANK_SIZE / 2 + 2){
               /* escapeDirection += direction - Math.PI; 
                danger = true;*/
                return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, radToDeg(_direction - direction), CommandType.NONE);
            }
        }
       /*if(danger){
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, radToDeg(_direction - escapeDirection), CommandType.NONE);
        }*/
        //
        // UTOK
        //
        if(info.isCanShoot() && !_enemies.isEmpty())
        {
            Point target = new Point();
            double targetDirection = 0.0;
            boolean moving = false;
            boolean back = false;
            int nearDist = Integer.MAX_VALUE;
            for (BetterInfoDetail id : _enemies){
                if(new Point(id.x, id.y).distance(new Point(_x, _y)) < nearDist){
                    nearDist = (int)(new Point(id.x, id.y).distance(new Point(_x, _y)));
                    target = new Point(id.x, id.y);
                    moving = id.isMoving;
                    back = id.back;
                    targetDirection = id.direction;
                }
            }
            
            if(moving){
                int multiplier = (int)(Math.round(target.distance(new Point(_x, _y)) / GameData.GRENADE_SPEED ));

                target.x += Math.round(Math.cos(targetDirection) * ((back)? GameData.BACWARD_SPEED : GameData.FORWARD_SPEED)* multiplier);
                target.y += Math.round(Math.sin(targetDirection) * ((back)? GameData.BACWARD_SPEED : GameData.FORWARD_SPEED)* multiplier);
            }
                    
            double deltaY = _y - target.y;
            double deltaX = _x - target.x;
            double angle =  Math.atan(deltaY/deltaX);
            int dir = -1;
            if(deltaX < 0)
                dir = 1;
            else
              angle = Math.PI - angle;  
            
            if(deltaX == 0)
            {
                angle = Math.PI / 2;
                if(deltaY < 0)
                    angle *= -1;
            }
            if(!_first)
                return new Command(CommandType.NONE, CommandType.TURN_LEFT, radToDeg( _direction - angle * dir), CommandType.SHOOT);
            else
                _first = false;
        }
        else if(new Point(_x, _y).distance(new Point(GameData.GAME_WIDTH / 2, GameData.GAME_HEIGTH / 2)) > 20){
            Point target = new Point(GameData.GAME_WIDTH / 2, GameData.GAME_HEIGTH / 2);
            double deltaY = _y - target.y;
            double deltaX = _x - target.x;
            double angle =  Math.atan(deltaY/deltaX);
            int dir = -1;
            if(deltaX < 0)
                dir = 1;
            else
              angle = Math.PI - angle;  
            
            if(deltaX == 0)
            {
                angle = Math.PI / 2;
                if(deltaY < 0)
                    angle *= -1;
            }
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, radToDeg(_direction - angle * dir), CommandType.NONE);
        }
        
        return new Command(CommandType.NONE, CommandType.TURN_LEFT, 0, CommandType.NONE);

    }
    
    private int radToDeg(double degValue){
        return (int)(degValue / Math.PI * 180);
    }

    @Override
    public String getName() {
        return "jut0006";
    }
    
}
