/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;
import thegame.model.Tank;

/**
 *
 * @author Tom
 * 
 * my own player to operate with tank
 * 
 */


public class TomPlayer implements IPlayer {
    private Info data;
    private List<Integer> distances;
    private int x, y, rotation = -1;
    private boolean victory = false;
    private int shootDistance = 350;
    
    public TomPlayer() {
        distances = new ArrayList<Integer>();
    }
    
    @Override
    public Command planNextMove(Info info) {
        // command types: NONE, MOVE_FORWARD, MOVE_BACK, TURN_LEFT, TURN_RIGHT, SHOOT
        // command order: CommandType move, CommandType rotate, int angle,  CommandType shoot
        // move forward/back, turn left/right, turn about mow many degrees, shoot order 
        
        Random r = new Random();
        
        // clean old
        distances.clear();
        this.x = this.y = this.rotation = -1;
        
        // fill new
        this.data = info;
        this.x = info.getX();
        this.y = info.getY();
        this.rotation = info.getDirection();
        
        int targetAngle = -1;
        int changeAngle = -1;
        int changeAngleVictory = -1;
        int targetDistance;
        
        int[] targetInfo;
        int[] approachingGrenades;
       
        
    /* -------------- any grenades approaching? -------------- */
        approachingGrenades = grenadesApproaching();
        if (approachingGrenades[0] != -1 && approachingGrenades[1] != -1) {
            targetAngle = approachingGrenades[0];
            if (this.rotation != targetAngle) {
//                System.out.println(targetAngle + ", " + this.rotation);
                if (canMove(targetAngle) == 1) {
                    targetAngle += 180;
                    if (targetAngle >= 360) targetAngle -= 360;
                }
                if (this.rotation > targetAngle) {
                    changeAngle = this.rotation - targetAngle;   
                } else if (this.rotation < targetAngle) {
                    changeAngle = this.rotation - targetAngle;
                }
            } else {
                changeAngle = 0;
            }
            if (canMove(targetAngle) == 1) {
                return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, changeAngle, CommandType.NONE);
            } else {
                
                return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, changeAngle, CommandType.NONE);
            }
            
        }
                
                
    /* -------------- end of evasive actions -------------- */    
        
        // is end of game? <=> no enemies
        if (this.victory == true) {
            changeAngleVictory = 0;
            while (canMove(this.rotation + changeAngleVictory) == 0) {
                changeAngleVictory += 90;
            }
            
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, changeAngleVictory, CommandType.NONE);
        }
        
        
                
    /* -------------- we are going to shoot -------------- */
        
        targetInfo = this.getTarget();
        targetAngle = targetInfo[0];
        targetDistance = targetInfo[1];
        
        if (targetAngle == -1) {
            this.victory = true;
            return new Command(CommandType.NONE, CommandType.NONE, 0, CommandType.NONE);
        }
        
        if (this.rotation != targetAngle) {
            if (this.rotation > targetAngle) {
                changeAngle = this.rotation - targetAngle;
            } else if (this.rotation < targetAngle) {
                changeAngle = this.rotation - targetAngle;
            }
        } else {
            changeAngle = 0;
        }
    /* -------------- end of shooting -------------- */
        int oppositeDirrection = targetAngle + 180;
        if (oppositeDirrection >= 360) oppositeDirrection -= 360;
        if (targetAngle >= 360) targetAngle -= 360;
        else if (targetAngle < 0) targetAngle += 360;
        
        
//        System.out.println("currentAngle = " + targetAngle + ", angle: " + canMove(targetAngle) + ", opposite: " + canMove(oppositeDirrection));
        
//        (targetDistance < 100) ? CommandType.SHOOT : CommandType.NONE)
        
        if (canMove(targetAngle) == 1) {
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, changeAngle, ((targetDistance <= shootDistance) ? CommandType.SHOOT : CommandType.NONE));
        } else if (canMove(oppositeDirrection) == 1) {
            return new Command(CommandType.MOVE_BACK, CommandType.TURN_LEFT, changeAngle, ((targetDistance <= shootDistance) ? CommandType.SHOOT : CommandType.NONE));
        } else {
            return new Command(CommandType.NONE, CommandType.TURN_LEFT, changeAngle, ((targetDistance <= shootDistance) ? CommandType.SHOOT : CommandType.NONE));
        }
        
    }

    
    @Override
    public String getName() {
        return "Tom";
    }
    
    private int[] grenadesApproaching() {      
        InfoDetail chosen = null;
        int distX, distY;
        double dX, dY;
        double angle = -1;
        double collisionCourse = -1;
        double distance, minDistance;
        int[] ret = new int[2]; // evasive angle, distance
        double deviation; // odchylka smeru strely od kolizniho kurzu
        double deviationTolerance; // tolerance, when grenade is dangerous
        double evasiveDirection; // uhybny uhel - kolmy ke koliznimu kurzu
        
        distance = minDistance = 10000;
        ret[0] = -1;
        ret[1] = -1;
        
        if (this.data != null) {
            for (InfoDetail o : (this.data.getGrenades())) {
                if(o != null) {
                    chosen = o;
                    distX = Math.abs(this.x - o.getX());
                    distY = Math.abs(this.y - o.getY());
                    distance = Math.sqrt((distX*distX) + (distY*distY));
                    
                    // if any threat grenade was found, ignore farther ones
                    if (ret[1] != -1 && distance > ret[1]) {
                        continue;
                    }
                    angle = Math.toDegrees(Math.atan((double)distY/distX));
                    
                    // find out what angle should grenade have to dirrect on me
                    if (chosen.getX() > this.x) { // target is to the right => 1st or 4th quadrant
                        if (chosen.getY() > this.y) { // 4th quadrant
//                            angle = 0 + angle;
                            collisionCourse = 270 - (180-90-angle);
                        } else { // 1st quadrant
//                            angle = 360 - angle;
                            collisionCourse = 90 + (180-90-angle);
                        }
                        
                    } else { // target is to the left => 2nd or 3rd quadrant
                        if (chosen.getY() > this.y) { // 3rd quadrant
//                            angle = 180 - angle;
                            collisionCourse = 270 + (180-90-angle);
                        } else { // 2nd quadrant
//                            angle = 180 + angle;
                            collisionCourse = 90 - (180-90-angle);
                        }
                    }
                    
                    deviation = Math.abs(chosen.getDirection() - collisionCourse);
                    
                    if (distance < 100) {
                        deviationTolerance = 15;
                    } else if (distance < 200) {
                        deviationTolerance = 10;
                    } else if (distance < 300) {
                        deviationTolerance = 8;
                    } else {
                        deviationTolerance = 6;
                    }                  
                    
                    if (deviation <= deviationTolerance) { // is grenade a threat?
                        // not correct function
                        /*
                        if ((chosen.getDirection() - collisionCourse) > 0) {
                            evasiveDirection = angle + 90;
                        } else {
                            evasiveDirection = angle - 90;
                        }
                        */
                        evasiveDirection = angle - 90;
                        
                        if (evasiveDirection >= 360) evasiveDirection -= 360;
                        else if (evasiveDirection < 0) evasiveDirection += 360;
                        
                        ret[0] = (int)evasiveDirection;
                        ret[1] = (int)distance;
                    } 
                }
            }
        } // this.data is null
        return ret;
    }
    
    private int[] getTarget() {
        // find the best target based on distance and current rotation of my tank
        // return rotation to hit the target
        
        InfoDetail chosen = null;
        int distX, distY;
        double dX, dY;
        double angle;
        double distance, minDistance;
        int closest;
        int[] ret = new int[2]; // targetAngle, distance
        
        distance = minDistance = 10000;
        
        
        // fill distances list 
        if (this.data != null) {
            for (InfoDetail o : (this.data.getEnemies())) {
                if(o != null) {
                    distX = Math.abs(this.x - o.getX());
                    distY = Math.abs(this.y - o.getY());
                    distance = Math.sqrt((distX*distX) + (distY*distY));
                    if (distance < minDistance) {
                        minDistance = distance;
                        chosen = o;
                    }
                }
            }
            if (chosen == null) {
                ret[0] = -1;
                ret[1] = -1;
                return ret;
            }
            // get angle
            //System.out.println("My tank on: " + this.x + " : " + this.y + " ");
            distX = Math.abs(this.x - chosen.getX());
            distY = Math.abs(this.y - chosen.getY());
            
            distance = Math.sqrt((distX*distX) + (distY*distY));

            angle = Math.toDegrees(Math.atan((double)distY/distX));
//            System.out.println("Distance: " + distX + " / " + distY + ", target angle = " + angle);

            if (chosen.getX() > this.x) { // target is to the right => 1st or 4th quadrant
                if (chosen.getY() > this.y) { // 4th quadrant
                    
                    angle = 0 + angle;
                    
                    /*
                    if ((chosen.getDirection() > 300 && chosen.getDirection() < 360) || (chosen.getDirection() > 0 && chosen.getDirection() < 60)) {
                        if (distance < 100) angle -= 4;
                        else if (distance > 100) angle -= 8;
                        else angle -= 12;
                    }
                    if ((chosen.getDirection() > 120 && chosen.getDirection() < 240)) {
                        if (distance < 100) angle += 4;
                        else if (distance > 100) angle += 8;
                        else angle += 12;
                    }
                    */
                    
                } else { // 1st quadrant
                             
                    angle = 360 - angle;
                    
                }
            } else { // target is to the left => 2nd or 3rd quadrant
                if (chosen.getY() > this.y) { // 3rd quadrant
                    angle = 180 - angle;
                                        
                } else { // 2nd quadrant
                    angle = 180 + angle;
                }
            }

            ret[0] = (int)angle;
            ret[1] = (int)distance;
            return ret;
        } // this.data is null
//        System.out.println("data is null!");
        ret[0] = -1;
        ret[1] = -1;
        return ret;
    }
    
    private int[] whereMove() {
        int wid, hei;
        int topLimit, bottomLimit, rightLimit, leftLimit;
        int tollerance = 150;
        int[] ret = {1,1,1,1}; // can move up, down, left, right
        
        wid = GameData.GAME_WIDTH;
        hei = GameData.GAME_HEIGTH;
        
        topLimit = tollerance;
        bottomLimit = hei - tollerance;
        rightLimit = wid - tollerance;
        leftLimit = tollerance;
        
        
        
        if (this.y < topLimit) {
            ret[0] = 0;
        }
        if (this.y > bottomLimit) {
            ret[1] = 0;
        }
        
        if (this.x < leftLimit) {
            ret[2] = 0;
        }
        if (this.x > rightLimit) {
            ret[3] = 0;
        }
        
        return ret;
    }
    
    private int canMove(int angle) {
        int[] where = whereMove(); // can move up, down, left, right
        
        
//        int wid, hei;
//        int topLimit, bottomLimit, rightLimit, leftLimit;
//        int tollerance = 150;
        
//        int[] ret = {1,1,1,1}; // can move up, down, left, right
//        
//        wid = GameData.GAME_WIDTH;
//        hei = GameData.GAME_HEIGTH;
//        
//        topLimit = tollerance;
//        bottomLimit = hei - tollerance;
//        rightLimit = wid - tollerance;
//        leftLimit = tollerance;
//        
        
//        if (this.y < topLimit-50 && angle > 180 && angle <= 359) {
//            return 0;
//        }
//        if (this.y > bottomLimit+50 && angle < 180 && angle > 0) {
//            return 0;
//        }
//        if (this.x < leftLimit-50 && angle > 90 && angle < 270) {
//            return 0;
//        }
//        if (this.x > rightLimit+50 && (angle > 270 && angle <= 360) || (angle >= 0 && angle < 90)) {
//            return 0;
//        }
        
        
        if (angle >= 225 && angle <= 315) { // heading up
            return where[0];
        }
        if (angle >= 45 && angle <= 135) { // heading bottom
            return where[1];
        }
        if ((angle > 315 && angle <= 360) || (angle >= 0 && angle < 45)) { // heading right
            return where[3];
        }
        if (angle > 135 && angle < 225) { // heading left
            return where[2];
        }
        
        return 0;
    }
    //private InfoDetail approachingGrenades() {}
    
}
