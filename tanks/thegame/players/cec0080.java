/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.Timer;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;

/**
 *
 * @author Reemon
 */
public class cec0080 implements IPlayer, ActionListener {
    private List<InfoDetail> enemies;
    private List<InfoDetail> bullets;
    private List<InfoDetail> bullets_in_radius;
    
    private Point position;
    private Point2D.Float direction_vector;
    private int radius;
    private int vision;
    
    private InfoDetail closest_tank;
    
    int aim_ahead;
    int aim_distance;
    int max_see_ahead;
    
    private Random randomizer;
    private boolean evading = false;
    private boolean adjust_evading = false;
    private InfoDetail evade_from = null;
    private int angle = 0;
    private Timer timer;

    public cec0080() {
        this.randomizer = new Random(System.currentTimeMillis());
        this.timer = new Timer(300, this);
        this.bullets_in_radius = new ArrayList<InfoDetail>();
        this.position = new Point();
        this.direction_vector = new Point2D.Float();
        this.closest_tank = null;
        this.radius = 400;
        this.vision = 10;
        
        //Na vzdalenost 670 strilet pred tank o 130
        this.aim_ahead = 135;
        this.aim_distance = 650;
    }
    
    @Override
    public Command planNextMove(Info info) {
        try{
            this.enemies = info.getEnemies();
            this.bullets = info.getGrenades();

            this.position.x = info.getX();
            this.position.y = info.getY();
            this.direction_vector = this.getVectorFromAngle(info.getDirection());
            
            if(this.position.x - GameData.TANK_SIZE/2 < 50 || this.position.y - GameData.TANK_SIZE/2 < 50
                    || this.position.x + GameData.TANK_SIZE/2 > GameData.GAME_WIDTH - 50 || this.position.y + GameData.TANK_SIZE/2 > GameData.GAME_HEIGTH - 50) {
                this.direction_vector.x *= -1;
                this.direction_vector.y *= -1;
                angle = 360 - this.getAngleFromVector(this.direction_vector);
                return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, angle, CommandType.NONE);
            }
            
            if(this.evading) {
                this.timer.start();
                if(this.adjust_evading){
                    this.adjust_evading = false;
                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, angle, CommandType.NONE);
                }
                
                return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, angle, CommandType.NONE);
            }
            
            this.findDangerousBullets();
            
            if(!this.bullets_in_radius.isEmpty()) {
                this.avoidBullet();
            } else {
                this.closest_tank = this.findClosestTank();
                if(this.closest_tank != null) {
                    Point2D.Float dir_vec = this.pursuitVector(this.closest_tank);
                    angle = 360 + this.getAngleFromVector(direction_vector) - this.getAngleFromVector(dir_vec);
                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, angle, CommandType.SHOOT);
                }
            }
        }catch(Exception e) {
            System.err.println(e.getMessage());
        }
               
        return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.NONE);
    }

    @Override
    public String getName() {
        return "CEC0080";
    }
    
    //Distances between objects 
    private float magnitude(int x, int y) {
        return (float)(Math.sqrt(x * x  + y * y));
    }
    
    private float magnitude(float x, float y) {
        return (float)(Math.sqrt(x * x  + y * y));
    }
    
    private float distanceFromMe(int x, int y) {
        return this.distance(this.position.x, this.position.y, x, y);
    }
    
    private float distance(int x1, int y1, int x2, int y2) {
        return this.magnitude(x1 - x2, y1 - y2);
    }
    
    //Find closest objects
    private InfoDetail findClosestTank() {
        return this.findClosestObject(this.enemies);
    }
    
    private void findDangerousBullets() {
        this.bullets_in_radius.clear();
        for(InfoDetail bullet : this.bullets) {
            float distance = this.distanceFromMe(bullet.getX(), bullet.getY());
            if(distance < this.radius) {
                Point2D.Float dir = this.getVectorFromAngle(bullet.getDirection());
                dir.x = (dir.x*distance) + bullet.getX();
                dir.y = (dir.y*distance) + bullet.getY();
                if(isPointInTank((int)dir.x, (int)dir.y)) {
                    
                    this.bullets_in_radius.add(bullet);
                }
            }
        }
    }
    
    private InfoDetail findClosestObject(List<InfoDetail> objects) {
        double closest_distance = 0.0;
        double object_distance = 0.0;
        InfoDetail closest = null;
        for(InfoDetail object : objects) {
            object_distance = this.distanceFromMe(object.getX(), object.getY()); 
            if(closest == null) {
                closest = object;
                closest_distance = object_distance;              
            } else {
                if(closest_distance > object_distance) {
                    closest = object;
                    closest_distance = object_distance;
                }
            }          
        }
        
        /*if(this.aim_ahead != 0) {
            this.aim_ahead = (this.aim_ahead * (int)closest_distance) / this.aim_distance;
            this.aim_distance = (int)closest_distance;
        }*/
        
        return closest;
    }
    
    private boolean isPointInTank(int x, int y) {
        return (this.position.x - GameData.TANK_SIZE/2 - this.vision < x && this.position.x + GameData.TANK_SIZE/2 + this.vision > x &&
                this.position.y - GameData.TANK_SIZE/2 - this.vision < y && this.position.y + GameData.TANK_SIZE/2 + this.vision > y);
    }
    
    //Convert dir vector to angle and opposite
    private Point2D.Float normalize(Point2D.Float vec) {
        float len = (float)(Math.sqrt((vec.x * vec.x) + (vec.y * vec.y)));
        if(len != 0.f) {
            vec.x /= len;
            vec.y /= len;
        }
        return vec;
    }
    
    private Point2D.Float pursuitVector(InfoDetail object) {
        Point2D.Float vec = this.getVectorFromAngle(object.getDirection());
        int objx = object.getX(); /*+ Math.round(vec.x * this.aim_ahead);*/
        int objy = object.getY(); /*+ Math.round(vec.y * this.aim_ahead);*/
        return this.normalize(new Point2D.Float(objx - this.position.x, objy - this.position.y));
    }
    
    private int getAngleFromVector(Point2D.Float vec) { 
        return Math.round((float)(Math.atan2(vec.y, vec.x)*180/Math.PI));
    }
    
    private Point2D.Float getVectorFromAngle(int angle) {
        float x = (float)(Math.cos(angle*Math.PI/180));
        float y = (float)(Math.sin(angle*Math.PI/180));
        return new Point2D.Float(x,y);
    } 

    private void avoidBullet() {       
        InfoDetail bullet = this.findClosestObject(this.bullets_in_radius);
        this.evading = true;
        if(bullet != null) {
            if(this.evade_from == null || bullet.getDirection() != this.evade_from.getDirection()) {
                int rdirx = (this.randomizer.nextBoolean()) ? 1 : -1;
                int rdiry = (rdirx == 1) ? -1 : 1;
                
                Point2D.Float evade_vec = this.getVectorFromAngle(bullet.getDirection());
                
                float temp = evade_vec.x;
                evade_vec.x = evade_vec.y*rdirx;
                evade_vec.y = temp*rdiry;
                this.evade_from = bullet;
                this.adjust_evading = true;
                angle = 360 + this.getAngleFromVector(direction_vector) - this.getAngleFromVector(evade_vec);
            }
        }   
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        this.findDangerousBullets();
        
        if(this.bullets_in_radius.isEmpty()) {
            this.timer.stop();
            this.evading = false;
        } else {
            this.avoidBullet();
        }
    }
}
