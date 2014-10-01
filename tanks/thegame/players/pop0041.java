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

/**
 *
 * @author beh01
 */
//http://www.mathopenref.com/coorddist.html
public class pop0041 implements IPlayer{
	
    @Override
   public Command planNextMove(Info info) {
  // TheGameView.
  if (info.getEnemies().size() > 0) {
     InfoDetail ui = info.getEnemies().get(0);
     double myp = info.getX()+info.getY();
     double range = ui.getX()+ui.getY();
     double max_prvni = Math.abs(myp - range);
     System.out.print(myp+" "+max_prvni);
     int tmp = 0;
     
     for(int i = 0;i<info.getEnemies().size();i++){
         ui = info.getEnemies().get(i);
         range = ui.getX()+ui.getY();
         double max = Math.abs(myp - range);
         if (max_prvni > max){
              max_prvni = max;
              tmp = i;
         }
     
     }
     info.getEnemies().get(tmp);
     
    

   Point Enemy = new Point(ui.getX(), ui.getY());
   
   Point Me = new Point(info.getX(), info.getY());
   
   int uhel;
   if(ui.getY() > info.getY())
    uhel = 180 - (int) Math.abs(Math.toDegrees(Math.atan2(Me.y - Enemy.y, Me.x - Enemy.x)));
   else
    uhel = 360 - (int) Math.abs(Math.toDegrees(Math.atan2(Enemy.y - Me.y, Enemy.x - Me.x)));

   if (info.getDirection() >= uhel - 5 && info.getDirection() <= uhel + 5)
    return new Command(CommandType.NONE, CommandType.NONE, 0,
      CommandType.SHOOT);
   else
    return new Command(CommandType.NONE, CommandType.TURN_RIGHT, 9,
      CommandType.NONE);
  }
  return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0,
    CommandType.NONE);
 }
    @Override
    public String getName() {
        return "pop0041";
    }
    
}
