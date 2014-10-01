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
import thegame.model.ModelObject;

/**
 *
 * @author beh01
 */
public class VYB0028 implements IPlayer{
     private  int x, y, direction;
     private int grenade_dir = 0;
     private int enX, enY;
     private int pom_uhel = 0;
     private int pom_uhel1 = 0;
     private int shootMove = 0;
     private int shootcount = 0;
     private int continueDefMove = 0;
     private int continueEneMove = 0;
     private boolean danger = false;
     private Command lastMove;
     
     private List<InfoDetail> enemies = new ArrayList<InfoDetail>();
     private List<InfoDetail> grenades = new ArrayList<InfoDetail>();
    @Override
    public Command planNextMove(Info info) {
         direction=info.getDirection();
         x=info.getX();//moje pozice
         y=info.getY();//moje pozice
         Command com = null;
         grenades=info.getGrenades();
         enemies=info.getEnemies();
         Random rand = new Random();
         int random = rand.nextInt(100);
         
         if(info.getEnemies().isEmpty()) return new Command(CommandType.NONE, CommandType.NONE,0, CommandType.NONE);
         com = GrenadeExecute();
         if(com != null) {
             lastMove = com;
             shootcount = 0;
             return com;
         }
         
         com = EnemyExecute();
         if(com != null && danger == false) {
             lastMove = com;
             shootcount = 0;
             return com;
         }
         
         if(info.isCanShoot()) {
         if(!info.getEnemies().isEmpty()) {
            int angle = getAngleNearEnemy();
            if(shootcount == 4) {
                if(enY < y && enX > x) angle -= 10;
                else if(enY < y && enX < x) angle += 10;
                else if(enY > y && enX < x) angle -= 10;
                else angle += 10;
            }
            if(shootcount == 6) {
                if(enY < y && enX > x) angle -= 8;
                else if(enY < y && enX < x) angle += 8;
                else if(enY > y && enX < x) angle -= 8;
                else angle += 8;
            }
            if(shootcount == 8) {
                if(enY < y && enX > x) angle -= 5;
                else if(enY < y && enX < x) angle += 5;
                else if(enY > y && enX < x) angle -= 5;
                else angle += 5;
                shootcount = 0;
            }
            shootcount++;
            return new Command(null, CommandType.TURN_LEFT,angle, CommandType.SHOOT);
            
            //return Shoot();
         }
         else com = new Command(CommandType.NONE, CommandType.NONE,0, CommandType.NONE);
         }
         else com = new Command(CommandType.MOVE_FORWARD, CommandType.NONE,pom_uhel, CommandType.NONE);
         
         return com;
    }
    
    /*
     * Funkce na střelbu a oddálení od nepřítele
     */
    private Command Shoot() {
        Command com = null;
        
       //if(isEnemyNear()) {
        
            int angle = getAngleNearEnemy();
            com = new Command(CommandType.NONE, CommandType.TURN_LEFT,angle, CommandType.SHOOT);
        /*}
        else {
            int angle = NearestEnemy();
            switch(shootMove){
                case 0: {
                com = new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT,angle, CommandType.SHOOT);
                shootMove++;
                } break;
                    
                case 1: {
                com = new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT,angle+5, CommandType.SHOOT);
                shootMove++;
                } break;
                    
                case 2: {
                com = new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT,angle-5, CommandType.SHOOT);
                shootMove = 0;
                } break;
            }
        }*/
        
        return com;
    }
    private int NearestEnemy(){
        int vzdalenost = 3000;
        int angle, min_x = 0,min_y = 0;
        /**
          * Zjištění nejližšího nepřítele
          */
         for (int i=0;i<enemies.size();i++)
         { 
            // Výpočet vzdalenost X,Y souradnice
            int pomx = Math.abs(x - enemies.get(i).getX());
            int pomy = Math.abs(y - enemies.get(i).getY());
            
            // Výpočet uhlopříčky
            int pom_vzdalenost = (int) Math.sqrt(Math.pow(pomx, 2)+Math.pow(pomy, 2));
            
            if(pom_vzdalenost < vzdalenost){
                vzdalenost = pom_vzdalenost;
                min_x=Math.abs(enemies.get(i).getX()-x);
                min_y=Math.abs(enemies.get(i).getY()-y);
                enX = enemies.get(i).getX();
                enY = enemies.get(i).getY();
            }
         }
         /*************************************/
        
        if(enX >=x && enY >=y){
            // KVADRANT 4
             double pom1 = (double) min_x/min_y;
             angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
             angle += 270 + direction;
         }
         else if(enX >=x && enY <=y){
              // KVADRANT 1
             double pom1 = (double) min_y/min_x;
             angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
             angle+=direction; 
         }
         else if(enX <=x && enY <=y){
             // KVADRANT 2
             double pom1 = (double) min_x/min_y;
             angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
             angle+=90;angle+=direction;
         }
         else {
             // KVADRANT 3
             double pom1 = (double) min_y/min_x;
             angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
             angle += 180 + direction;
         }
        
        return angle;
    }
    private Command EnemyExecute(){
        Command com = null;
        
        if(continueEneMove > 0) {
             com = new Command(CommandType.MOVE_FORWARD,CommandType.NONE,pom_uhel,CommandType.NONE);
             continueEneMove--;
         }
        else {
             if(isEnemyNear()){
                 if(enX <= x && enY<= y){
                    pom_uhel = 360 - direction;
                    pom_uhel += 45;
                 
                    if(x < 30) getCorrectAngle(pom_uhel, 1);
                    if(x > GameData.GAME_WIDTH - 30) getCorrectAngle(pom_uhel, 2);
                    if(y < 30) getCorrectAngle(pom_uhel, 3);
                    if(y > GameData.GAME_HEIGTH - 30) getCorrectAngle(pom_uhel, 4);
                    
                    com = new Command(CommandType.MOVE_FORWARD,CommandType.TURN_RIGHT,pom_uhel,CommandType.NONE);            
            
                    //defmove = true;
                    continueEneMove = 5; 
                 }
                 if(enX > x && enY<= y){
                    pom_uhel = 360 - direction;
                    pom_uhel += 135;
                 
                    if(x < 30) getCorrectAngle(pom_uhel, 1);
                    if(x > GameData.GAME_WIDTH - 30) getCorrectAngle(pom_uhel, 2);
                    if(y < 30) getCorrectAngle(pom_uhel, 3);
                    if(y > GameData.GAME_HEIGTH - 30) getCorrectAngle(pom_uhel, 4);
                    
                    com = new Command(CommandType.MOVE_FORWARD,CommandType.TURN_RIGHT,pom_uhel,CommandType.NONE);            
            
                    //defmove = true;
                    continueEneMove = 5;
                 }
                 if(enX > x && enY > y){
                    pom_uhel = 360 - direction;
                    pom_uhel += 225;
                 
                    if(x < 30) getCorrectAngle(pom_uhel, 1);
                    if(x > GameData.GAME_WIDTH - 30) getCorrectAngle(pom_uhel, 2);
                    if(y < 30) getCorrectAngle(pom_uhel, 3);
                    if(y > GameData.GAME_HEIGTH - 30) getCorrectAngle(pom_uhel, 4);
                    
                    com = new Command(CommandType.MOVE_FORWARD,CommandType.TURN_RIGHT,pom_uhel,CommandType.NONE);            
            
                    //defmove = true;
                    continueEneMove = 5;
                 }
                 if(enX <= x && enY > y){
                    pom_uhel = 360 - direction;
                    pom_uhel += 315;
                 
                    if(x < 30) getCorrectAngle(pom_uhel, 1);
                    if(x > GameData.GAME_WIDTH - 30) getCorrectAngle(pom_uhel, 2);
                    if(y < 30) getCorrectAngle(pom_uhel, 3);
                    if(y > GameData.GAME_HEIGTH - 30) getCorrectAngle(pom_uhel, 4);
                    
                    com = new Command(CommandType.MOVE_FORWARD,CommandType.TURN_RIGHT,pom_uhel,CommandType.NONE);            
            
                    //defmove = true;
                    continueEneMove = 5;
                 }
             }
         }
        return com;
    }
    private boolean isEnemyNear(){
        int vzdalenost = 200;
        boolean isEnemy = false;
        
        for(int i = 0; i < enemies.size(); i++){
            int pomx = Math.abs(x - enemies.get(i).getX());
            int pomy = Math.abs(y - enemies.get(i).getY());
            // Výpočet uhlopříčky
            int pom_vzdalenost = (int) Math.sqrt(Math.pow(pomx, 2)+Math.pow(pomy, 2));
            
            if(pom_vzdalenost < vzdalenost){
                isEnemy = true;
                vzdalenost = pom_vzdalenost;
                enX = enemies.get(i).getX();
                enY = enemies.get(i).getY();
            }
        }
        
        if(isEnemy) return true;
        else return false;
    }
    private int getAngleNearEnemy(){
        int vzdalenost = 2000;
        int angle, min_x = 0,min_y = 0;
        /**
          * Zjištění nejližšího nepřítele
          */
         for (int i=0;i<enemies.size();i++)
         { 
            // Výpočet vzdalenost X,Y souradnice
            int pomx = Math.abs(x - enemies.get(i).getX());
            int pomy = Math.abs(y - enemies.get(i).getY());
            
            // Výpočet uhlopříčky
            int pom_vzdalenost = (int) Math.sqrt(Math.pow(pomx, 2)+Math.pow(pomy, 2));
            
            if(pom_vzdalenost < vzdalenost){
                vzdalenost = pom_vzdalenost;
                min_x=Math.abs(enemies.get(i).getX()-x);
                min_y=Math.abs(enemies.get(i).getY()-y);
                enX = enemies.get(i).getX();
                enY = enemies.get(i).getY();
            }
         }
         /*************************************/
        
        if(enX >=x && enY >=y){
            // KVADRANT 4
             double pom1 = (double) min_x/min_y;
             angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
             angle += 270 + direction;
         }
         else if(enX >=x && enY <=y){
              // KVADRANT 1
             double pom1 = (double) min_y/min_x;
             angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
             angle+=direction; 
         }
         else if(enX <=x && enY <=y){
             // KVADRANT 2
             double pom1 = (double) min_x/min_y;
             angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
             angle+=90;angle+=direction;
         }
         else {
             // KVADRANT 3
             double pom1 = (double) min_y/min_x;
             angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
             angle += 180 + direction;
         }
        
        return angle;
    }
    
    /**
     * 
     * Funkce na vyhýání se granátu 
     */
    private Command GrenadeExecute(){
        Command com = null;
        
         if(continueDefMove > 0) {
             
             if(isEnemyNear()) {
                pom_uhel = 360 - direction;
                pom_uhel += grenade_dir + 65;
                // Korekce uhlu aby nevyjel z hrací plochy
                if(x < 30) getCorrectAngle(pom_uhel, 1);
                if(x > GameData.GAME_WIDTH - 30) getCorrectAngle(pom_uhel, 2);
                if(y < 30) getCorrectAngle(pom_uhel, 3);
                if(y > GameData.GAME_HEIGTH - 30) getCorrectAngle(pom_uhel,4 );
                com = new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT,pom_uhel,CommandType.NONE);
             }
             else {
             // Korekce uhlu aby nevyjel z hrací plochy
             if(x < 30) getCorrectAngle(pom_uhel, 1);
             if(x > GameData.GAME_WIDTH - 30) getCorrectAngle(pom_uhel, 2);
             if(y < 30) getCorrectAngle(pom_uhel, 3);
             if(y > GameData.GAME_HEIGTH - 30) getCorrectAngle(pom_uhel,4 );
             com = new Command(CommandType.MOVE_FORWARD,CommandType.NONE,pom_uhel,CommandType.NONE);
             }
             if(continueDefMove > 0) continueDefMove--;
         }
         else
            if(isDanger()){
                if(isEnemyNear()) {
                    pom_uhel = 360 - direction;
                    pom_uhel += grenade_dir + 65;
                }
                /*else {
                    pom_uhel = 360 - direction;
                    pom_uhel += grenade_dir + 90;
                }*/
                if(x < 30) getCorrectAngle(pom_uhel, 1);
                if(x > GameData.GAME_WIDTH - 30) getCorrectAngle(pom_uhel, 2);
                if(y < 30) getCorrectAngle(pom_uhel, 3);
                if(y > GameData.GAME_HEIGTH - 30) getCorrectAngle(pom_uhel, 4);
                
                com = new Command(CommandType.MOVE_FORWARD,CommandType.TURN_RIGHT,pom_uhel,CommandType.NONE);  
                
             continueDefMove = 7;
         }
         return com;
    }
    
    private boolean isDanger(){
       int vzdalenost = 300;
       boolean isGrenade = false;
       int grenade_x, grenade_y;
        
         for (int i=0;i<grenades.size();i++)
         { 
            // Výpočet vzdalenost X,Y souradnice
            int pomx = Math.abs(x - grenades.get(i).getX());
            int pomy = Math.abs(y - grenades.get(i).getY());
            // Výpočet uhlopříčky
            int pom_vzdalenost = (int) Math.sqrt(Math.pow(pomx, 2)+Math.pow(pomy, 2));
            
            if(pom_vzdalenost < 300){
                if(isGranadeOnMyWay(grenades.get(i).getX(), grenades.get(i).getY(), grenades.get(i).getDirection())){
                    if(pom_vzdalenost < vzdalenost) {
                        isGrenade = true;
                        vzdalenost = pom_vzdalenost;
                        grenade_x = grenades.get(i).getX();
                        grenade_y = grenades.get(i).getY();
                        grenade_dir = grenades.get(i).getDirection();
                    }
                }
            }
         }
        if(!isGrenade) return false;
        else return true;
         
    }
    private boolean isGranadeOnMyWay(int grenade_x, int grenade_y, int grenade_dir){
        int min_x = Math.abs(grenade_x-x);
        int min_y = Math.abs(grenade_y-y);
        int enX = grenade_x;
        int enY = grenade_y;
        int angle;
        
        if(x >= enX && y >= enY){
            // KVADRANT 1
            double pom1 = (double) min_y/min_x;
            angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
            //System.out.println("Kvadrant 1 "+angle);
        }
        else if(x >= enX && y <= enY){
            // KVADRANT 4
            double pom1 = (double) min_x/min_y;
            angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
            angle+= 270;
            //System.out.println("Kvadrant 4 "+angle);
        }
        else if(x <= enX && y <= enY){
            // KVADRANT 3
            double pom1 = (double) min_y/min_x;
            angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
            angle+= 180;
            //System.out.println("Kvadrant 3 "+angle);
        }
        else {
            // KVADRANT 2
            double pom1 = (double) min_x/min_y;
            angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
            angle += 90;
            //System.out.println("Kvadrant 2 "+angle);
        }
        
       /*if(min_x < 20 || min_y < 20) {
            if(angle != 180)
            danger = true;
            return true;
        }
        if(min_x < 30 || min_y < 30) {
            danger = true;
            return true;
        }*/
        
        if(grenade_dir >= angle - 10 && grenade_dir <= angle + 10) {
            danger = false;
            return true;
        }
        else {
            danger = false;
            return false;
        }
    }
    private int getCorrectAngle(int ang, int correct){
        int pom = 0;
       
        int angle = CorrectDirection(ang+direction);
        if(correct == 1) {
            if(angle >= 90 && angle <= 180){
                pom = 360 - direction + 85;
                pom_uhel1 = pom_uhel;
                pom_uhel = pom; 
            }
            else if(angle > 180 && angle <= 275) {
                pom = 360 - direction + 275;
                pom_uhel1 = pom_uhel;
                pom_uhel = pom; 
            }
                
        }
        if(correct == 2) {
            if(angle >= 0 && angle <= 90){
                pom = 360 - direction + 95;
                pom_uhel1 = pom_uhel;
                pom_uhel = pom; 
            }
            else if(angle <= 360 && angle >= 270) {
                pom = 360 - direction + 275;
                pom_uhel1 = pom_uhel;
                pom_uhel = pom; 
            }
                
        }
        if(correct == 3) {
            if(angle <= 360 && angle >= 270){
                pom = 360 - direction + 5;
                pom_uhel1 = pom_uhel;
                pom_uhel = pom; 
            }
            else if(angle < 270 && angle > 180){
                pom = 360 - direction + 175;
                pom_uhel1 = pom_uhel;
                pom_uhel = pom; 
            }
                
        }
        if(correct == 4) {
            if(angle >= 0 && angle <= 90){
                pom = 360 - direction + 355;
                pom_uhel1 = pom_uhel;
                pom_uhel = pom; 
            }
            else if(angle > 90 && angle <= 180){
                pom = 360 - direction + 185;
                pom_uhel1 = pom_uhel;
                pom_uhel = pom; 
            }
                
        }
        /*if(defmove) {
            pom = 360 - direction + 5;
            continueCorrection = 2;
            pom_uhel1 = pom_uhel;
            pom_uhel = pom;
        }
        else {
        pom = angle + 5;
        pom_uhel1 = pom_uhel;
        pom_uhel = pom;
        }*/
        return pom;
    }
    private int CorrectDirection(int dir){
        boolean pom = true;
        int direct = dir;
        
        while(pom) {
            if(direct >= 360) {
                direct -=360;
            }
            else pom = false;
        }
        return direct;
    }
    @Override
    public String getName() {
        return "VYB0028";
    }
    
}