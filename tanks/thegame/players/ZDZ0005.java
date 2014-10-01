/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.awt.Point;

import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;

/**
 *
 * @author beh01
 */
// http://www.mathopenref.com/coorddist.html
public class ZDZ0005 implements IPlayer {

	@Override
	public Command planNextMove(Info info) {
		// TheGameView.

		Point C = new Point(info.getX(), info.getY());
		double min = GameData.GAME_WIDTH+GameData.GAME_HEIGTH;
		int enemy = 0;
		for (int i = 0; i < info.getEnemies().size(); i++) {
			InfoDetail d = info.getEnemies().get(i);

			Point A = new Point(d.getX(), d.getY());

			double length = Math.sqrt(Math.pow(C.x - A.x, 2) + Math.pow(C.y - A.y, 2));

			if(length < min){
				min = length;
				enemy = i;
			}
		}

		if (info.getEnemies().size() > 0) {
			InfoDetail d = info.getEnemies().get(enemy);

			Point A = new Point(d.getX(), d.getY());

			int angle;
			if(d.getY() > info.getY())
				angle = 180 - (int) Math.abs(Math.toDegrees(Math.atan2(C.y - A.y, C.x - A.x)));
			else
				angle = 360 - (int) Math.abs(Math.toDegrees(Math.atan2(A.y - C.y, A.x - C.x)));

			//int rotation = angle - info.getDirection();

			if (info.getDirection() >= angle -5 && info.getDirection() <= angle +5)
				return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0,
						CommandType.SHOOT);
			else{
				if(angle < info.getDirection()){
					return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, 11,
						CommandType.NONE);
				}else
				{
					return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, 11,
							CommandType.NONE);
				}
			}
		}else{
			return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, 10,
					CommandType.SHOOT);
		}
	}

	@Override
	public String getName() {
		return "ZDZ0005";
	}

}
