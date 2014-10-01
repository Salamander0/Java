/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.Random;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;
import thegame.info.Info;

/**
 *
 * @author Dalibor
 */
public class ZAK0049 implements IPlayer{

    private static int gr_det_dist=200;
          
    @Override
    public Command planNextMove(Info info) {
        Random r = new Random();
        //Rozpoznani okraju pole
        if (info.getX()>(GameData.GAME_WIDTH-GameData.TANK_SIZE/2)) {
           if(info.getDirection()>=0 && info.getDirection()<120)
                return new Command(CommandType.NONE, CommandType.TURN_RIGHT, r.nextInt(30)+1, CommandType.NONE);
           if (info.getDirection()>220 && info.getDirection()<360)
               return new Command(CommandType.NONE, CommandType.TURN_LEFT, r.nextInt(30)+1, CommandType.NONE);
        }
        if (info.getX()<(0+GameData.TANK_SIZE/2)) {
           if(info.getDirection()>=180 && info.getDirection()<320)
            return new Command(CommandType.NONE, CommandType.TURN_RIGHT, r.nextInt(30)+1, CommandType.NONE);
           if(info.getDirection()>40 && info.getDirection()<180)
            return new Command(CommandType.NONE, CommandType.TURN_LEFT, r.nextInt(30)+1, CommandType.NONE);
        }
        if (info.getY()<(0+GameData.TANK_SIZE/2)) {
           if((info.getDirection()>=270 && info.getDirection()<360) || (info.getDirection()>=0 && info.getDirection()<50))
              return new Command(CommandType.NONE, CommandType.TURN_RIGHT, r.nextInt(30)+1, CommandType.NONE);
           if(info.getDirection()>120 && info.getDirection()<270)
               return new Command(CommandType.NONE, CommandType.TURN_LEFT, r.nextInt(30)+1, CommandType.NONE);
        }
        if (info.getY()>(GameData.GAME_HEIGTH-GameData.TANK_SIZE/2)) {
           if(info.getDirection()>=90 && info.getDirection()<240)
              return new Command(CommandType.NONE, CommandType.TURN_RIGHT, r.nextInt(30)+1, CommandType.NONE);
           if((info.getDirection()>=0 && info.getDirection()<90) || (info.getDirection()>300 && info.getDirection()<360))
              return new Command(CommandType.NONE, CommandType.TURN_LEFT, r.nextInt(30)+1, CommandType.NONE);
        }
        
        /*
        //Vyhnuti granatum
        double gr_dist=-1;
        int gr_number=-1;
        for (int i=0;i<info.getGrenades().size();i++) {
            double x_dist=Math.abs(info.getGrenades().get(i).getX()-info.getX());
            double y_dist=Math.abs(info.getGrenades().get(i).getY()-info.getY());
            double distance=Math.sqrt(Math.pow(x_dist,2)+Math.pow(y_dist,2));
            if (distance<gr_det_dist) {
                gr_dist=distance;
                gr_number=i;
                break;
            }
        }
        if (gr_dist!=-1) {
            CommandType move = CommandType.NONE;
            CommandType turn = CommandType.NONE;
            int angle=0;
            
            double x_dist=Math.abs(info.getGrenades().get(gr_number).getX()-info.getX());
            double y_dist=Math.abs(info.getGrenades().get(gr_number).getY()-info.getY());
            int ang=info.getDirection()-info.getGrenades().get(gr_number).getDirection();
            if (x_dist<0 && ang>=0) {
                ang=180-ang;
            }
            else {
                if (ang<0) {
                    if (x_dist<0) {
                        ang=Math.abs(ang)+180;
                    }
                    else {
                        ang=ang+360;
                    }
                }
            }     
            
            double ang2 = Math.PI * info.getGrenades().get(gr_number).getDirection() / 180.0;
            int next_x=info.getGrenades().get(gr_number).getX()+(int) (Math.round(GameData.GRENADE_SPEED * Math.cos(angle)));
            int next_y=info.getGrenades().get(gr_number).getY()+(int) (Math.round(GameData.GRENADE_SPEED * Math.sin(angle)));
            x_dist=Math.abs(next_x-info.getX());
            y_dist=Math.abs(next_y-info.getY());
            double distance=Math.sqrt(Math.pow(x_dist,2)+Math.pow(y_dist,2));
            boolean dist_dif;
            dist_dif = distance<gr_dist ? true : false;
            if (dist_dif==true) {
                if (ang>=0 && ang<90) {
                    move= CommandType.MOVE_FORWARD;
                    turn= CommandType.TURN_RIGHT;
                    angle=15;
                }
                if (ang>=90 && ang<180) {
                    move= CommandType.MOVE_FORWARD;
                    turn= CommandType.TURN_LEFT;
                    angle=15;
                }
                if (ang>=180 && ang<270) {
                    move= CommandType.MOVE_FORWARD;
                    turn= CommandType.TURN_LEFT;
                    angle=15;
                }
                if (ang>=270 && ang<360) {
                    move= CommandType.MOVE_FORWARD;
                    turn= CommandType.TURN_RIGHT;
                    angle=15;
                }
                return new Command(move, turn, angle, CommandType.NONE);
            } 
        }
        */
        
        //Hledani nejblizsiho tanku
        double min_dist=-1;
        int enemy_no=-1;
        for (int i=0; i<info.getEnemies().size(); i++) {
            double x_dist=Math.abs(info.getEnemies().get(i).getX()-info.getX());
            double y_dist=Math.abs(info.getEnemies().get(i).getY()-info.getY());
            double distance=Math.sqrt(Math.pow(x_dist,2)+Math.pow(y_dist,2));
            
            if (min_dist==-1) {
                min_dist=distance;
                enemy_no=i;
            }
                else {
                  if (distance<min_dist) {
                      min_dist=distance;
                      enemy_no=i;
                  }
            }
        }
        
        double y_dist=info.getEnemies().get(enemy_no).getY()-info.getY();
        double x_dist=info.getEnemies().get(enemy_no).getX()-info.getX();
        double ang=Math.toDegrees(Math.asin(y_dist/min_dist));
        
        if (x_dist<0 && ang>=0) {
            ang=180-ang;
        }
        else {
          if (ang<0) {
            if (x_dist<0) {
                ang=Math.abs(ang)+180;
            }
            else {
                ang=ang+360;
            }
          }
        }
        
        CommandType move=CommandType.MOVE_FORWARD;
        CommandType turn;
        int angle;
        CommandType shoot=CommandType.NONE;
        
        if (info.getDirection()-ang<-180) {
            ang=ang-360;
        }
        else {
            if (info.getDirection()-ang>180) {
                ang=ang+360;
            }
        }
        angle=(int)(Math.round(info.getDirection()-ang));

        if (angle>=0) {
            turn = CommandType.TURN_LEFT;
            if (angle>10)
                angle=15;
            else {
                shoot= CommandType.SHOOT;
                if (angle<5)
                    angle=0;
            }
        }
        else {
            turn = CommandType.TURN_RIGHT;
            if (angle<-10)
                angle=15;
            else {
                shoot= CommandType.SHOOT;
                if (angle>-5)
                    angle=0;
            }                
        }
        
        if (min_dist<50)
            move= CommandType.MOVE_BACK;
        
        return new Command(move, turn, angle, shoot);                
      }

    @Override
    public String getName() {
        return "ZAK0049";
    }
}
