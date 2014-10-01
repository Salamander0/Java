/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.List;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;

/**
 *
 * @author Majka
 */
public class Bagzila implements IPlayer {

    static boolean nasel = true;
    public Command Kill(Info info){
        List<InfoDetail> enemies = info.getEnemies();
    
        int near = 0;
        int ex=0;
        int ey=0;
        
        int mx = info.getX();
        int my = info.getY();
        
        for(int i=0;i<enemies.size();i++){
           
            double dis = Math.pow((enemies.get(i).getX()-info.getX()),2)-Math.pow((enemies.get(i).getY()-info.getY()),2);
            int ii = (int) Math.sqrt(dis);
            
            if(near == 0){
                near = ii;
                ex = enemies.get(i).getX();
                ey = enemies.get(i).getY();
            }
            
            else if(ii < near){
                near = ii;
                ex = enemies.get(i).getX();
                ey = enemies.get(i).getY();
            }
        }
        
        
        //System.out.println("Zjistuji" + mx +"  "+ my +"enemy: " + ex + " " + ey);
        
        double vysledny_uhel=0;
        
        if(ex<mx && ey < my){
            
              //System.out.println("Jdem po nem?");
      
            
            int b = mx - ex;
            int a = my - ey;
            
            
            
            double c = Math.sqrt(a*a + b*b);
            
            double helper = (double) a/c;
             //System.out.println("A:" + a + "B:" +b + "C: "+c + "helper" + helper);
             
            double uhel = Math.asin(helper);
            uhel = Math.toDegrees(uhel);
            double muj_uhel = info.getDirection();
            
            //System.out.println("Jeho uhel" + uhel);
            
            if(muj_uhel<180){
                vysledny_uhel=180-muj_uhel + uhel;
                
            }else{
                vysledny_uhel=muj_uhel - 180 + uhel;                
            }
               //System.out.println("Vysledny uhel" + vysledny_uhel);
            
          if(nasel==true){  
             //nasel=false;
             //System.out.println("otocil jsem se");
              return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, (int)vysledny_uhel, CommandType.SHOOT);
             
          }else{
                 return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, (int)0, CommandType.SHOOT);
          
          }
            
        }
        
        
        return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, (int)vysledny_uhel, CommandType.NONE);
        
    }
        
        
        

    
    
    public Command goCenter(Info info){
       
        if(info.getX() > 0 && info.getX() < 30){
            //System.out.println("1 " + info.getDirection());
            if(info.getDirection() < 280 && info.getDirection() > 10)
                return new Command(CommandType.NONE, CommandType.TURN_LEFT, 10, CommandType.NONE);
        }
        if(info.getY() > 0 && info.getY() < 30){
            //System.out.println("2");
            if(info.getDirection() < 100 && info.getDirection() > 80);
            else
                return new Command(CommandType.NONE, CommandType.TURN_RIGHT, 10, CommandType.NONE);
        }
        if(info.getX() < GameData.GAME_WIDTH && info.getX() > GameData.GAME_WIDTH-30){
            //System.out.println("3");
            if(info.getDirection() < 190 && info.getDirection() > 170);
            else
                return new Command(CommandType.NONE, CommandType.TURN_RIGHT, 10, CommandType.NONE);
        }
        if(info.getY() < GameData.GAME_HEIGTH && info.getY() > GameData.GAME_HEIGTH-30){
            //System.out.println("4");
            if(info.getDirection() < 280 && info.getDirection() > 260);
            else
                return new Command(CommandType.NONE, CommandType.TURN_LEFT, 10, CommandType.NONE);
        }
        
        
        
         return Kill(info);
        
   //      return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.NONE);
        
        
    }
    
    
    @Override
    public Command planNextMove(Info info) {
        int x = info.getX();
        int y = info.getY();
        int direction = info.getDirection();
        
       // System.out.println("x: " + x + " y: " + y);
        //List<InfoDetail> enemies = info.getEnemies();
        //System.out.println(enemies.get(1).getX() + " " + enemies.get(1).getY());
        return this.goCenter(info);
    }

    @Override
    public String getName() {
        return "Bagzila";
    }
    
}
