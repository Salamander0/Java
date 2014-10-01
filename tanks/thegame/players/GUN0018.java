package thegame.players;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.ModelPlayer;

public class GUN0018 implements IPlayer{
	private List<InfoDetail> enemies;
	
	@Override
    public Command planNextMove(Info info) {
		double dist=999999999, tempd;
		int tempe = 0, check = 0;
		InfoDetail TMP;
		
		enemies = new ArrayList<InfoDetail>();
		enemies = info.getEnemies();
		
		if (info.getEnemies().size() > 0) {
		for (int i=0; i<enemies.size(); i++) {	
				TMP = enemies.get(i);
				tempd = Math.sqrt(TMP.getX()^2+TMP.getY()^2);
				if (tempd == 0) {}
				else if (tempd < dist) {
					dist = tempd;
					tempe = i;
				}
			}						
		}
		else {check = 1;}
		
		TMP = enemies.get(tempe);	
		
		if (info.getEnemies().size() > 0 && check == 0) {
			int angle;
			if(TMP.getY() > info.getY()){angle = 180 - (int) Math.abs(Math.toDegrees(Math.atan2(info.getY() - TMP.getY(), (info.getX() - TMP.getX()))));}
			else {angle = 360 - (int) Math.abs(Math.toDegrees(Math.atan2(TMP.getY() - info.getY(), TMP.getX() - info.getX())));}
			
			if (info.getDirection() >= angle -3 && info.getDirection() <= angle +3) {
				return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.SHOOT);
			}
			else{
				if(angle < info.getDirection()) {
					return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, 11,
						CommandType.SHOOT);
				}
				else{
					return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, 11, CommandType.SHOOT);
				}
			}
		}
		else if (check == 1) {return new Command(CommandType.NONE, CommandType.NONE, 10, CommandType.NONE);}
		else {return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, 10, CommandType.SHOOT);}				  					  		
	}
				

    @Override
    public String getName() {
        return "GUN0018";
    }
    

}