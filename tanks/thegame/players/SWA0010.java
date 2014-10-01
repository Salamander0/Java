/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.Random;
import thegame.info.Info;
import thegame.model.Command;
import thegame.model.CommandType;
//import java.util.ArrayList;
//import java.util.List;
//import thegame.info.InfoDetail;
import thegame.model.GameData;
/**
 *
 * @author swa0010
 */
public class SWA0010 implements IPlayer{
    @Override
    public Command planNextMove(Info info) {
        //List<InfoDetail> seznamGranatu = info.getGrenades();
                    
        //udaje meho tanku
        int tanX = info.getX();
        int tanY = info.getY();
        int tanDir = info.getDirection();
        
        //projizdim seznam granatu
        /*for(InfoDetail item : seznamGranatu){
            //udaje granatu
            int grX = item.getX();
            int grY = item.getY();
            int grDir = item.getDirection();
            
            //vypocet pozice strely za 5 pohybu
            double angle = Math.PI * grDir / 180.0;
            int x = (int) (Math.round(GameData.GRENADE_SPEED * Math.cos(angle)));
            int y = (int) (Math.round(GameData.GRENADE_SPEED * Math.sin(angle)));
            grX += (x*5);
            grY += (y*5);
       
            //if((((grX -5) < tanX) &&(( 5+ grX)> tanX)) && (((grY -5) < tanY) &&(( 5+ grY)> tanY)))
        }*/
        Random r = new Random();
        if ((((tanX +30 ) < GameData.GAME_WIDTH) && ((tanY +30 ) < GameData.GAME_HEIGTH))&&(((tanX -30 ) > 0) && ((tanY -30 ) > 0))){
                System.out.println(tanX);
                int otoc = r.nextInt(40)+1;
                
                if(otoc <5){
                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, r.nextInt(30)+19, CommandType.SHOOT);
                }
                if((otoc >5)&&(otoc<11) ){
                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, 15, CommandType.SHOOT);
                }
                else
                    return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.SHOOT);
        }
        return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, r.nextInt(30)+19, CommandType.SHOOT);
    }

    @Override
    public String getName() {
        return "SWA0010";
    }
}
