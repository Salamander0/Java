/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.awt.event.KeyEvent;
import java.util.Random;
import thegame.info.Info;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;

/**
 *
 * @author Smeki
 */
public class sme0061 implements IPlayer {
   private int step = 0;
   

    
   //funkce na ziskani smeru k souperi
   private int getAngle(int my_x, int my_y, int my_direction, int e_x, int e_y){
       double angle = 0;
       
       //utoc jen na nepratele dostatecne blizko - distance px
       if((e_x-my_x)*(e_x-my_x) + (e_y-my_y)*(e_y-my_y) < 800*800){
            //-----------VYPOCET UHLU
            if(my_y > e_y && my_x < e_x || my_y < e_y && my_x > e_x){
            angle += (Math.atan(Math.abs(((double)(my_x-e_x))/((double)(my_y-e_y)))))*(180.00 / Math.PI);
            }else{
            angle += 90.00 - (Math.atan(Math.abs(((double)(my_x-e_x))/((double)(my_y-e_y)))))*(180.00 / Math.PI);
            }
            //System.out.println((int)angle);
            //-----------URCENI KVADRANTU SOUPERE VZHLEDEM K TANKU
            if(my_y > e_y && my_x > e_x){
                //Souper je v 2.kvadrantu
                //System.out.println("Souper je v 2.kvadrantu");
                angle +=180;
            }else if(my_y > e_y && my_x < e_x){
                //Souper je v 1.kvadrantu
                //System.out.println("Souper je v 1.kvadrantu");
                angle +=270;
            }else if(my_y < e_y && my_x < e_x){
                //Souper je v 4.kvadrantu
                //System.out.println("Souper je v 4.kvadrantu");
                angle +=0;
            }else if(my_y < e_y && my_x > e_x){
                //Souper je v 3.kvadrantu
                //System.out.println("Souper je v 3.kvadrantu");
                angle +=90;
            }else if(my_y == e_y && my_x > e_x){
                //Souper ma stejnou Y a je v 2./3. kvadrantu
                //System.out.println("Souper ma stejnou Y a je v 1./4. kvadrantu");
                angle +=0;
            }else if(my_y == e_y && my_x < e_x){
                //Souper ma stejnou Y a je v 1./4. kvadrantu
                //System.out.println("Souper ma stejnou Y a je v 2./3. kvadrantu");
                angle +=180;
            }else if(my_y > e_y && my_x == e_x){
                //Souper ma stejnou Y a je v 1./4. kvadrantu
                //System.out.println("Souper ma stejnou X a je v 3./4. kvadrantu");
                angle +=270;
            }else if(my_y < e_y && my_x == e_x){
                //Souper ma stejnou Y a je v 1./4. kvadrantu
                //System.out.println("Souper ma stejnou X a je v 1./2. kvadrantu");
                angle +=90;
            }
            //-----------URCENI KVADRANTU KAM MIRIM
            // Nakonec nepotrebne
            if(my_direction >= 0 && my_direction <=90){
                //System.out.println("Mirim do 4.kvadrantu");
            }else if(my_direction >= 90 && my_direction <=180){
                //System.out.println("Mirim do 3.kvadrantu");
            }else if(my_direction >= 180 && my_direction <=270){
                //System.out.println("Mirim do 2.kvadrantu");
            }else if(my_direction >= 270 && my_direction <=360){
                //System.out.println("Mirim do 1.kvadrantu");
            }


            //-----------NAHODNY OBSTREL - 33% sance zasahu
            Random r = new Random();
            int rand = r.nextInt(3);
            if(rand == 0){
                //prima strela
                angle += 0;
            }else if(rand == 1){
                //pridej 5 stupnu
                angle += 5;
            }else{
                //odeber 5 stupnu
                angle -= 5;
            }

            angle -= my_direction;
            //System.out.println((int)angle);
            return (int)angle;
       }else{
            return 0;
       }
   }
   
   //funkce na oznacovani nejblizsich nepratel
   private int ClosestEnemy(Info info, int r){
       int my_x = info.getX();
       int my_y = info.getY();

       while(r > 50 && r<(GameData.GAME_WIDTH+GameData.GAME_HEIGTH)/4){
           r++;
           for(int i=0;i<info.getEnemies().size();i++){
               int e_x = info.getEnemies().get(i).getX();
               int e_y = info.getEnemies().get(i).getY();
           
           
                if((e_x-my_x)*(e_x-my_x) + (e_y-my_y)*(e_y-my_y) < r*r){
                    //System.out.println("Tank s id: " + i +" je v rosahu "+r+"px");
                    return i;
                }else{
                    //System.out.println("Mimo dosah...");

                }
            }
       }

       return -1;
       

   }
   
   //3 verze uhybani - CHYBNA
   private int AvoidGrenade3(Info info){
       int my_x = info.getX();
       int my_y = info.getY();
       
       int x, y;
       int distance;
       int angle;
       int grenade_angle;
       int grenade_angle2;
       int tolerance;
       int escape_angle = -1;
       
       if(info.getGrenades().size() > 0){
       for(int i=0;i<info.getGrenades().size();i++){
           int e_x = info.getEnemies().get(i).getX();
           int e_y = info.getEnemies().get(i).getY();
           
           x = Math.abs(my_x - e_x);
           y = Math.abs(my_y - e_y);
           
           distance = (int)Math.sqrt((x*x) + (y*y));
           angle = (int)Math.toDegrees(Math.atan((double)y/x));
           
           if (e_x > my_x) {
                        if (e_y > my_y) { 

                            grenade_angle = 270 - (90-angle);
                        } else {

                            grenade_angle = 90 + (90-angle);
                        }
                       
           } else {
                        if (e_y > my_y) {
                            grenade_angle = 270 + (90-angle);
                        } else { 
                            grenade_angle = 90 - (90-angle);
                        }
           }
           
           grenade_angle2 = Math.abs(info.getGrenades().get(i).getDirection() - grenade_angle);
           
           if (distance < 100) {
                       tolerance = 15;
                    } else if (distance < 200) {
                        tolerance = 10;
                    } else if (distance < 300) {
                        tolerance = 8;
                    } else {
                        tolerance = 6;
                    }
           
           if (grenade_angle2 <= tolerance) {
                        if ((info.getGrenades().get(i).getDirection() - grenade_angle) > 0) {
                            escape_angle = (int)angle + 90;
                        } else {
                            escape_angle = (int)angle - 90;
                        }
       }
           
         if (escape_angle >= 360){ escape_angle -= 360;}
         else if (escape_angle < 0){ escape_angle += 360;}
   
   }
            return escape_angle;
       }else{
            return -1;
       }
   }
    
   //1 verze uhybani
   private boolean AvoidGrenade(Info info){
       for(int i=0;i<info.getEnemies().size();i++){
            double angle = 0;
            int e_x = info.getX();
            int e_y = info.getY();
            
            int my_x = info.getEnemies().get(i).getX();
            int my_y = info.getEnemies().get(i).getY();
            
            
            //-----------VYPOCET UHLU
            if(my_y > e_y && my_x < e_x || my_y < e_y && my_x > e_x){
            angle += (Math.atan(Math.abs(((double)(my_x-e_x))/((double)(my_y-e_y)))))*(180.00 / Math.PI);
            }else{
            angle += 90.00 - (Math.atan(Math.abs(((double)(my_x-e_x))/((double)(my_y-e_y)))))*(180.00 / Math.PI);
            }
            
            angle = (int)angle;
            System.out.println(angle);
            if(angle == info.getEnemies().get(i).getDirection()){
                System.out.println("DANGER");
                step++;
                return true;
            }
        }
              
       return false;
   }
   
   //2 verze uhybani
   private boolean AvoidGrenade2(Info info){
            int my_x = info.getX();
            int my_y = info.getY();
            
            int r = 200;
            // pokud se v okruhu o polomeru r vyskytuje granat  - vrat true
            for(int i=0;i<info.getGrenades().size();i++){
                int e_x = info.getGrenades().get(i).getX();
                int e_y = info.getGrenades().get(i).getY();
                
                int pyth = (e_x-my_x)*(e_x-my_x) + (e_y-my_y)*(e_y-my_y);
                //System.out.println(pyth);
                
                if(pyth <= r*r){
                    return true;
                }else{
                    return false;
                }
            }
            return false;
   }
   
   private int AvoidGrenade4(Info info){
       int my_x = info.getX();
       int my_y = info.getY();
       
       int e_x,e_y = -1;
       int x,y;
       
       double angle = -1;
       double angle2 = -1;
       
       for(int i = 0;i<info.getGrenades().size();i++){
           e_x = info.getEnemies().get(i).getX();
           e_y = info.getEnemies().get(i).getY();
           
           x = Math.abs(my_x - e_x);
           y = Math.abs(my_y - e_y); 
           
           angle = Math.toDegrees(Math.atan((double)y/(double)x));
           
           //----------URCENI KVADRANTU
           if (e_x > my_x) {
                        if (e_y > my_y) { 

                            angle2 = 270 - (90-angle);
                        } else {

                            angle2 = 90 + (90-angle);
                        }
                       
           } else {
                        if (e_y > my_y) {
                            angle2 = 270 + (90-angle);
                        } else { 
                            angle2 = 90 - (90-angle);
                        }
           }
           //----------URCENI KVADRANTU
           
       }
       return (int)angle2;
   }
   
   // Funkce ke kontrole hranic herniho uzemi
   private boolean CheckArea(int my_x, int my_y){
       if(my_x > GameData.GAME_WIDTH-27 || my_x < 27 || my_y > GameData.GAME_HEIGTH-27 || my_y < 27){
           return true;
       }else{
           return false;
       }
   }
    @Override
    public Command planNextMove(Info info) {
        int my_x = info.getX();
        int my_y = info.getY();
        
        int angle = 0;
        int enemy_id=0;
        int radius = (GameData.GAME_HEIGTH + GameData.GAME_WIDTH)/8;
        Random r = new Random();
        //uhel na odklon od konce mapy
        int rand = r.nextInt(90);
        
        //uhel na uhyb granatu
        int rand2 = r.nextInt(10);
         
        //zamereni nejbliziho nepritele
        if(ClosestEnemy(info,radius) != -1){
            enemy_id = ClosestEnemy(info,radius);
        }

        
        if(info.getEnemies().isEmpty()==false){
            int e_x = info.getEnemies().get(enemy_id).getX();
            int e_y = info.getEnemies().get(enemy_id).getY();
            angle = getAngle(my_x,my_y,info.getDirection(),e_x,e_y);
        }
        
         //po sedmi uhybnych krocich vynuluj pocitadlo kroku

        //double angle = Math.atan(Math.abs(((double)(e_x-my_x))/((double)(e_y-my_y))));
        
        //info.getEnemies().get(0).getX(); // enemies 
        //info.getGrenades().get(steps)

        if(CheckArea(my_x,my_y) ==  true){
                return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, rand , null);
        }else{

            if(angle != 0){
                   return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, angle , CommandType.SHOOT);
            }else{
                  return new Command(CommandType.NONE, CommandType.NONE, 0 , null);
            }
            
            }
        }
       
        
    

    @Override
    public String getName() {
        return "SME0061";
    }
    
}

