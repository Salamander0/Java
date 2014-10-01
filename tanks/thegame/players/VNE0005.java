package thegame.players;

import java.awt.Point;

import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;

public class VNE0005 implements IPlayer{
	
    public Command planNextMove(Info info) {
    	
    	  if (info.getEnemies().size() != 0){
    		  
    	   InfoDetail d = info.getEnemies().get(0);

    	   Point ENEMY = new Point(d.getX(), d.getY());
    	   Point ME = new Point(info.getX(), info.getY());
    	   
    	   int angle;
    	   
    	   if(d.getY() > info.getY()) angle = 180 - (int) Math.abs(Math.toDegrees(Math.atan2(ME.y - ENEMY.y, ME.x - ENEMY.x)));
    	   else angle = 360 - (int) Math.abs(Math.toDegrees(Math.atan2(ENEMY.y - ME.y, ENEMY.x - ME.x)));

    	   int aimbot =  angle - info.getDirection();
    	   
    	   if (info.getDirection() >= angle - 5 && info.getDirection() <= angle + 5)
    	    return new Command(CommandType.NONE, CommandType.NONE, 0, CommandType.SHOOT);
    	   		else
    	    return new Command(CommandType.NONE, CommandType.TURN_RIGHT, aimbot, CommandType.NONE);
    	  }
    	  return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.NONE);
    	 }

    public String getName() {
        return "Lukas";
    }
    
}
