/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.ArrayList;
import java.util.List;
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
public class SIR0029 implements IPlayer{
     private  int x, y, direction;int angle,angleg;int cislo_strely,cislo=2000;
     private int min_x=2000,min_y=2000,min_gx=2000,min_gy=2000;//vzdialenosti od tanku
     int enX = 0, enY = 0;//pozice najblizsieho protihraca
     double prepona=2000;double grenade_prepona=2000;double enemy_prepona=2000;
     int gx=0,gy=0,gd=0;//granad
     int run=0,run1=0,run2=0,run3=0; //útek
     int gsx=0,gsy=0;boolean isout=false;
     Info info;
     private List<InfoDetail> enemies = new ArrayList<InfoDetail>();
     private List<InfoDetail> grenades = new ArrayList<InfoDetail>();
    @Override
    
  
    public Command planNextMove(Info info) {
       
        
         enemy_prepona=2000;grenade_prepona=2000;
         min_x=2000;min_y=2000;//pozice najblizsieho tanku
         min_gx=2000;min_gy=2000;//pozice nejblizsi strely
         direction=info.getDirection();
         x=info.getX();y=info.getY();//moje pozice
        
         Command com;
         grenades=info.getGrenades();
         enemies=info.getEnemies();
         
         { com = new Command(CommandType.MOVE_FORWARD,null,0,null);}// defaulne pokyny
         
         if(x>23 && x<=27){com = new Command(CommandType.MOVE_FORWARD,CommandType.TURN_LEFT,direction,null);}
         else if(y>23 && y<=27){com = new Command(CommandType.MOVE_FORWARD,CommandType.TURN_LEFT,direction-90,null);}
         else if(y>1975&&y<=1978){com = new Command(CommandType.MOVE_FORWARD,CommandType.TURN_LEFT,direction+90,null);}
         else if(x>1975&&x<=1978 ){com = new Command(CommandType.MOVE_FORWARD,CommandType.TURN_LEFT,direction+180,null);}
         //System.out.println(x+" "+y);
         for (int i=0;i<enemies.size();i++)
         {  
             double p=Math.sqrt(Math.abs(enemies.get(i).getX()-x)*Math.abs(enemies.get(i).getX()-x)+Math.abs(enemies.get(i).getY()-y)*Math.abs(enemies.get(i).getY()-y));
             
            if(p<enemy_prepona){
                min_x=Math.abs(enemies.get(i).getX()-x);
                min_y=Math.abs(enemies.get(i).getY()-y);
                enX = enemies.get(i).getX();
                enY = enemies.get(i).getY();
                enemy_prepona=p;
            }
            
               
         }
         
         for (int i=0;i<grenades.size();i++)
         {
             double pp=Math.sqrt(Math.abs(grenades.get(i).getX()-x)*Math.abs(grenades.get(i).getX()-x)+Math.abs(grenades.get(i).getY()-y)*Math.abs(grenades.get(i).getY()-y));
             if(pp<grenade_prepona){
                 min_gx=Math.abs(grenades.get(i).getX()-x);
                 min_gy=Math.abs(grenades.get(i).getY()-y);
                 gx= grenades.get(i).getX();
                 gy= grenades.get(i).getY();
                 gd= grenades.get(i).getDirection();
                 grenade_prepona=pp;
                 cislo_strely=i;
             }
         }
      
          prepona=Math.sqrt(min_gx*min_gx+min_gy*min_gy);
          
         if(run!=0){
            while( prepona<=180&&(gx >=x && gy >=y))
            {//System.out.println(run+" "+cislo+" "+cislo_strely);
                if(cislo!=cislo_strely){run=0;cislo=2000;}else run++;
                return new Command(CommandType.MOVE_FORWARD, CommandType.NONE,0,CommandType.NONE);
            }
        }
         if(run1!=0){
            while( prepona<=180&&(gx <=x && gy >=y ))
            {//System.out.println(run1+" "+cislo+" "+cislo_strely);
                if(cislo!=cislo_strely){run1=0;cislo=2000;}else run1++;
                return new Command(CommandType.MOVE_FORWARD, CommandType.NONE,0,CommandType.NONE);
            }
        }
          if(run2!=0){
            while( prepona<=180&& (gsx >=x && gsy <=y ))
            {//ystem.out.println(run2+" "+cislo+" "+cislo_strely);
                if(cislo!=cislo_strely){run2=0;cislo=2000;}else run2++;
                return new Command(CommandType.MOVE_FORWARD, CommandType.NONE,0,CommandType.NONE);
            }
        }
           if(run3!=0){
            while( prepona<=180&& (gsx <=x && gsy <=y ))
            {//System.out.println(run3+" "+cislo+" "+cislo_strely);
                if(cislo!=cislo_strely){run3=0;cislo=2000;}else run3++;
                return new Command(CommandType.MOVE_FORWARD, CommandType.NONE,0,CommandType.NONE);
            }
        }
          
          run=0;run1=0;run2=0;run3=0;
         //*******************************pro enemy
         if(enX >=x && enY >=y){
           //  System.out.println("4");
             double pom1 = (double) min_x/min_y;
             angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
             angle += 270 + direction;
         }
         else if(enX >=x && enY <=y){
            // System.out.println("1");
             double pom1 = (double) min_y/min_x;
             angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
             angle+=direction;
            
         }
         else if(enX <=x && enY <=y){
             //System.out.println("2");
             double pom1 = (double) min_x/min_y;
             angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
             angle+=90;angle+=direction;
         }
         else if(enX <=x && enY >=y){
            // System.out.println("3");
             double pom1 = (double) min_y/min_x;
             angle=(int)(Math.atan(pom1)*(180.00/Math.PI));
             angle += 180 + direction;
         }

            //*********************pro grenades

            if( prepona<=180)
            {
                cislo=cislo_strely;
                //4 kvadrant
                if(((gx >=x && gy >=y) && (gd>=180 && gd<270))){
                  double pom=(double) min_gy/min_gx;
                  angleg=(int)(Math.atan(pom)*(180.00/Math.PI));
                  gd-=180;
                     if(Math.abs(angleg-gd)<20){
                         
                     if(angleg-gd<0 ){
                    System.out.println(angleg+" "+gd+" R "+prepona+" 4");run++;
                    return new Command(CommandType.NONE, CommandType.TURN_LEFT,direction-(90+angleg),null);}
                 
                  else {
                    System.out.println(angleg+" "+gd+" L "+prepona+" 4");run++;
                    return new Command(CommandType.NONE, CommandType.TURN_LEFT,direction-(270+angleg),null);
                     }
                     }
                }         
                //3 kvadrant
                else if(gx <=x && gy >=y && (gd>=270 && gd<360)){
                  double pom1 = (double) min_gx/min_gy;
                    angleg=(int)(Math.atan(pom1)*(180.00/Math.PI));
                    gd-=270;
                    if(Math.abs(angleg-gd)<=20){
                      
                   if(angleg-gd<0 ){
                     
                    System.out.println(angleg+" "+gd+" R "+prepona+" 3");run1++;
                    return new Command(CommandType.NONE, CommandType.TURN_LEFT,direction-(180+angleg),null);}
                 
                 else {
                    System.out.println(angleg+" "+gd+" L "+prepona+" 3");run1++;
                    return new Command(CommandType.NONE, CommandType.TURN_LEFT,direction-(0+angleg),null);}
                     }}
                
                //1kvadrant
                else if(gx >=x && gy <=y && (gd>=90 && gd<180)){
                    
                    double pom=(double) min_gx/min_gy;
                    angleg=(int)(Math.atan(pom)*(180.00/Math.PI));
                    gd-=90;
                     
                    if(Math.abs(angleg-gd)<=20){
                     
                    if(angleg-gd<0 ){
                    System.out.println(angleg+" "+gd+" R "+prepona+" 3");run2++;
                    return new Command(CommandType.NONE, CommandType.TURN_LEFT,direction-(0+angleg),null);}
                 
                 else {
                    System.out.println(angleg+" "+gd+" L "+prepona+" 3");run2++;
                    return new Command(CommandType.NONE, CommandType.TURN_LEFT,direction-(180+angleg),null);}
                     }}
                //2kvadrant
                
                else if(gx <=x && gy <=y && (gd>=0 && gd<90)){
                 double pom1 = (double) min_gy/min_gx;
                    angleg=(int)(Math.atan(pom1)*(180.00/Math.PI));
                 if(Math.abs(angleg-gd)<=20){
                      
                     if(angleg-gd>=0 ){
                     
                    System.out.println(angleg+" "+gd+" R "+prepona+" 2");run3++;
                    return new Command(CommandType.NONE, CommandType.TURN_LEFT,direction-(90+angleg),null);}
                 
                 else {
                    System.out.println(angleg+" "+gd+" L "+prepona+" 2");run3++;
                    return new Command(CommandType.NONE, CommandType.TURN_LEFT,direction-(270+angleg),null);}}}
                }
        if(info.isCanShoot()){ return new Command(null, CommandType.TURN_LEFT,angle,CommandType.SHOOT);}
            
       
          return com;
    }

    @Override
    public String getName() {
        return "SIR0029";
    }
    
}
