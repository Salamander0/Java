/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;

/**
 *
 * @author beh01
 */
//http://www.mathopenref.com/coorddist.html
public class BRE0084 implements IPlayer{
    @Override
    public Command planNextMove(Info info) {
  // TheGameView.
        
        
  if (!info.getEnemies().isEmpty()) {
      int index = 0;
      int range = 10000;
      InfoDetail enemy = info.getEnemies().get(0);
      for (int i = 0; i < info.getEnemies().size(); i++) {
          enemy = info.getEnemies().get(i);
          
          if(range > Math.abs((enemy.getY() - info.getY())) + Math.abs((enemy.getX() - info.getX()))){
              range = Math.abs((enemy.getY() - info.getY())) + Math.abs((enemy.getX() - info.getX()));
              index = i;
              
          }
              
      }
      
      enemy = info.getEnemies().get(index);
   
   
      int x1 = enemy.getX(),
              y1 = enemy.getY(),
              x2 = info.getX(),
              y2 = info.getY();

   int angle;
   if(enemy.getY() > info.getY())
    angle = 180 - (int) Math.abs(Math.toDegrees(Math.atan2(y2 - y1, x2 - x1)));
   else
    angle = 360 - (int) Math.abs(Math.toDegrees(Math.atan2(y2 - y1, x1 - x2)));
   
   int smer = angle - info.getDirection();
   
   if (info.getDirection() == angle)
    return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.SHOOT);
   else
    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, smer, CommandType.NONE);
  }

  return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.NONE);
 }

    @Override
    public String getName() {
        return "BRE0084";
    }
    
}
