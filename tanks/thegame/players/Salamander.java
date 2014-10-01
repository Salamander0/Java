package thegame.players;

import java.util.*;
import java.util.List;
import java.awt.*;

import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;


public class Salamander implements IPlayer{

	private int TANK_SIZE = thegame.model.GameData.TANK_SIZE/2;
	
    private String displayname = "Crazy_horse";
    private Point position;
    private int nextangle = 0;
    private boolean shooting = false;
    private boolean evading = false;
    private int turn = 0;
    
	
    @Override
    public Command planNextMove(Info info) {
    	this.position.x = info.getX();
        this.position.y = info.getY();
        NextStep(info);
    	return new Command(CommandType.MOVE_FORWARD, Turning(), nextangle, Shooting());
    }
    
    @Override
    public String getName() {
        return displayname;
    }
    
    public Salamander(){
    	 this.position = new Point();
    }
    
    private void NextStep(Info info){
    	this.turn = 0;
    	if((Targeted(info) == true) && (evading == false)){
    		evading = true;
    	}
    	else{
    		evading = false;
    		FindTarget(info);
    	}
    	if (nextangle > info.getDirection()){
    		nextangle = nextangle - info.getDirection();
    		this.turn = 1;
    	}else if (nextangle < info.getDirection()){
    		nextangle = info.getDirection() - nextangle;
    		this.turn = -1;
    	}
    }
    
    private CommandType Shooting(){
    	if(this.shooting == true)
    		return CommandType.SHOOT;
    	else return CommandType.NONE;	
    }
    
    private CommandType Turning(){
    	if(this.turn == -1){
    		return CommandType.TURN_LEFT;
    	}
    	else if (this.turn == 1){
    		return CommandType.TURN_RIGHT;
    	}
    	else return CommandType.NONE;
    }
    
    
    private double magnitude(int ix, int iy) {
    	double x = Math.abs(ix);
    	double y = Math.abs(iy);
        return (double)(Math.sqrt(x * x  + y * y));
    }
    
    private double distanceFromMe(int x, int y) {
        return this.distance(this.position.x, this.position.y, x, y);
    }
    
    private double distance(int x1, int y1, int x2, int y2) {
        return this.magnitude(x1 - x2, y1 - y2);
    }
    
    private int angleToHit(int x, int y, int offset){  
    	int px = position.x + offset - x;
    	int py = position.y + offset - y;
    	double dst = distance(position.x+offset, position.y+offset, px, py);
		double angle = Math.asin(Math.abs(px) / dst);
    	
    	if(position.y > y){
    		if(position.x > x){return 270+(int)Math.toDegrees(angle); } //270+
    		else{return 360-(int)Math.toDegrees(angle);} //360-
    	}else{
    		if(position.x > x){return 180-(int)Math.toDegrees(angle);} //180-
    		else{return 90+(int)Math.toDegrees(angle);} //90+
    	}
    }
    
    private void FindTarget(Info info){
    	double Distance = 5000;
		int angletoclosest = 0;
		int i = -1;
		
		Iterator enemies;	
		enemies = info.getEnemies().iterator();
		
		if(info.getEnemies().size() != 0){
			
			while(enemies.hasNext()){
				enemies.next();
				i++;
				InfoDetail tank = info.getEnemies().get(i);
				
				int targetX = tank.getX();
				int targetY = tank.getY();
				int angle = angleToHit(targetX,targetY, 0);
				
							
				if(Distance > distanceFromMe(targetX, targetY)){
					Distance = distanceFromMe(targetX, targetY);
					angletoclosest = angle;
				}
			}
			if(Distance < 700){
				this.shooting = true;
				this.nextangle = angletoclosest;
			}
			else{
				this.nextangle = angletoclosest;
				this.shooting = false;
			}
		}	
    }
    
    private boolean Targeted(Info info){
		double Distance = 5000;
		int closest_target = -1;
		boolean targeted = false;
		int i = -1;
		
		Iterator grenades;
		grenades = info.getGrenades().iterator();
		
		if(info.getGrenades().size() != 0){
			while(grenades.hasNext()) {
				grenades.next();
				i++;
				
				InfoDetail grenade = info.getGrenades().get(i);
				
				int angle = grenade.getDirection();
				int targetX = grenade.getX();
				int targetY = grenade.getY();
			
				if(distanceFromMe(targetX, targetY)<700){
					int min = Math.abs(360-angleToHit(targetX,targetY, -TANK_SIZE));
					int max = Math.abs(360-angleToHit(targetX,targetY, TANK_SIZE));
				
					if(max > angle && min < angle){
						targeted = true;
						if(Distance > distanceFromMe(targetX, targetY)){
							Distance = distanceFromMe(targetX, targetY);
							closest_target = i;
						}
					}
				}
			}
			if(targeted){
				InfoDetail threat = info.getGrenades().get(closest_target);
				int angle_of_grenade = threat.getDirection();
				if(angle_of_grenade >= 180)
					{this.nextangle = angle_of_grenade - 90;}
				else 
					{this.nextangle = angle_of_grenade + 90;}
				return true;
			}
		}
		return false;
    }
}
