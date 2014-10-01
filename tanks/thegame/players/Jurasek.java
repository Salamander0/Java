/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.Random;
import thegame.info.Info;
import thegame.model.Command;
import thegame.model.CommandType;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import thegame.model.GameData;
import java.lang.Math;

/**
 *
 * @author silent
 */
public class Jurasek implements IPlayer{
    GameData data;
//    private int i = 0;
    int xmoje,ymoje =0;
    int xcizi,ycizi = 0;
//    double c =0;
//    double v = 0;
//    double m =0;
//    int y = 0;
//    double z = 0;
//    int k =0;
//    String j;
    double h = 0;
     @Override
     public Command planNextMove(Info info) {

          if(info.getEnemies().iterator().hasNext())
          {
              xmoje = info.getX();
              ymoje = info.getY();
            
              xcizi = info.getEnemies().iterator().next().getX();
              ycizi = info.getEnemies().iterator().next().getY();
              

            //c = Math.abs((xmoje*ymoje)-(xcizi*ycizi));
//            m = c/360;
//            // OREZE DES. CAST
//            int pamet = (int)((m + 0.005) * 100.0);
//            m = ((double)pamet)/100.0;
//            v = Math.floor(c/360);
//            z = m-v;
//            j = Double.toString(z);
//            j=j.substring(2,4);
//            k = Integer.parseInt(j);
            
            
               h = Math.toDegrees(Math.atan2(ycizi-ymoje, xcizi - xmoje));
               int cel =(int)h;
            
             
                return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, 360 + info.getDirection()- cel, CommandType.SHOOT);
                
          }
          else
              return new Command(CommandType.NONE, CommandType.NONE, 0, CommandType.NONE);
        
   
            
    }

    @Override
    public String getName() {
        return "Jurasek";
    }
    
}
