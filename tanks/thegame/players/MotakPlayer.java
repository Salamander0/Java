/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.awt.geom.Ellipse2D;
//import java.awt.geom.Line2D;
import java.util.Random;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.GameData;
import thegame.model.Command;
import thegame.model.CommandType;


/**
 *
 * @author MOT0039
 */
public class MotakPlayer implements IPlayer{
    private Ellipse2D warning_zone;
    private Ellipse2D danger_zone;
    private boolean FLAG_WARNING = false;
    private boolean FLAG_DANGER = false;
    
    @Override
    public Command planNextMove(Info info){
        int warning_radius = 300;
        int danger_radius = 100;
        
        InfoDetail next_target;
        warning_zone = new Ellipse2D.Double((double)(info.getX() - warning_radius/2), (double)(info.getY() - warning_radius/2), warning_radius, warning_radius);
        danger_zone = new Ellipse2D.Double((double)(info.getX() - danger_radius/2), (double)(info.getY() - danger_radius/2), danger_radius, danger_radius);
        
        
        Random r = new Random();
        
        if(info.getX() < 50 || info.getX() > GameData.GAME_WIDTH - 50){
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, 90, CommandType.NONE);
        }
        if(info.getY() < 50 || info.getY() > GameData.GAME_HEIGTH - 50){
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, 90, CommandType.NONE);
        }
        
        InfoDetail immediate_danger = null;
        
        for(InfoDetail d : info.getGrenades()){
            if(warning_zone.contains((double)d.getX(),(double) d.getY())){
                if(danger_zone.contains((double)d.getX(), (double)d.getY())){
                    FLAG_DANGER = true;
                }
                else{
                    FLAG_DANGER = false;
                }
            FLAG_WARNING = true;
            int deg = 90 - DegreeOfVectors(info.getX(), info.getY(), info.getDirection(),d);
            return new Command(CommandType.MOVE_FORWARD,  CommandType.TURN_LEFT,deg, CommandType.NONE);
            }
            else{
                FLAG_WARNING = false;
            }
            
           /* if(immediate_danger == null){
                immediate_danger = d;
            }
            
            if(Math.abs(info.getX() - immediate_danger.getX()) > Math.abs(info.getX() - d.getX()) && Math.abs(info.getY() - immediate_danger.getY()) > Math.abs(info.getY() - d.getY())){
                immediate_danger = d;
            }
            
            Line2D help = new Line2D.Double(immediate_danger.getX(), immediate_danger.getY(), immediate_danger.getX() + Math.cos(Math.toRadians(immediate_danger.getDirection())) * (warning_radius + 50) ,immediate_danger.getY() + Math.sin(Math.toRadians(immediate_danger.getDirection()))* (warning_radius + 50));
            if(help.intersects(warning_zone.getBounds2D())){
                int deg = 90 - DegreeOfVectors(info.getX(), info.getY(), info.getDirection(),d);
                    return new Command(CommandType.MOVE_FORWARD,  CommandType.TURN_LEFT,deg, CommandType.NONE);
            }*/
            

        }
        

        if(info.isCanShoot() && info.getEnemies().size() > 0){
            next_target = info.getEnemies().get(r.nextInt(info.getEnemies().size()));

            int real_degree = DegreeOfVectors(info.getX(), info.getY(), info.getDirection(), next_target);
            return new Command(CommandType.NONE, CommandType.TURN_RIGHT, real_degree, CommandType.SHOOT);    
        }
        
        return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.NONE);
    }
    
    @Override
    public String getName() {
        return "FilipMotak";
    }
    
    public int DegreeOfVectors(int x, int y, int dir, InfoDetail b){
            int delta_x = b.getX() - x;
            int delta_y = b.getY() - y;
            double degree = Math.atan2(delta_y , delta_x) * 180 / Math.PI;
            int real_degree = (int)degree - dir;  
            return real_degree;
    }
    
    public Ellipse2D getWarning(){
        return warning_zone;
    }
    
    public Ellipse2D getDanger(){
        return danger_zone;
    }
}
