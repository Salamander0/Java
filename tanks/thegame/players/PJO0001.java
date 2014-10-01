package thegame.players;

import java.util.ArrayList;
import java.util.List;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;

/**
 *
 * @author Patrik
 */

public class PJO0001 implements IPlayer{
     private  int x, y, direction;
     int uhol, uhol_grenade;
     private int en_min_x=2000,en_min_y=2000,gr_min_x=2000,gr_min_y=2000;//vzdialenosti od tanku
     int enemy_x = 0, enemy_y = 0;//pozice najblizsieho protihraca
     double g_prepona=2000;
     double grenade_prepona=2000;
     double enemy_prepona=2000;
     int gx=0,gy=0,grenade=0;//granad

     Info info;
     private List<InfoDetail> enemies = new ArrayList<InfoDetail>();
     private List<InfoDetail> grenades = new ArrayList<InfoDetail>();
    
     @Override
        public Command planNextMove(Info info) {
        
        if(info.getEnemies().isEmpty()) { return new Command(null,null,0,null); }
         enemy_prepona=2000;grenade_prepona=2000;
         en_min_x=2000;en_min_y=2000;//pozice najblizsieho tanku
         gr_min_x=2000;gr_min_y=2000;//pozice nejblizsi strely
         direction=info.getDirection();
         x=info.getX();y=info.getY();//moje pozice

         grenades=info.getGrenades();
         enemies=info.getEnemies();
         
         Command com;
         com = new Command(CommandType.MOVE_FORWARD,null,0,null);// defaultne pokyny
         
         for (int i=0;i<enemies.size();i++)
         {  
             double p=Math.sqrt(Math.abs(enemies.get(i).getX()-x)*Math.abs(enemies.get(i).getX()-x)+Math.abs(enemies.get(i).getY()-y)*Math.abs(enemies.get(i).getY()-y));
             
            if(p < enemy_prepona){
                en_min_x=Math.abs(enemies.get(i).getX()-x);
                en_min_y=Math.abs(enemies.get(i).getY()-y);
                enemy_x = enemies.get(i).getX();
                enemy_y = enemies.get(i).getY();
                enemy_prepona=p;
            }     
         }
         
         for (int i=0;i<grenades.size();i++)
         {
             double pp=Math.sqrt(Math.abs(grenades.get(i).getX()-x)*Math.abs(grenades.get(i).getX()-x)+Math.abs(grenades.get(i).getY()-y)*Math.abs(grenades.get(i).getY()-y));
             if(pp < grenade_prepona){
                 gr_min_x=Math.abs(grenades.get(i).getX()-x);
                 gr_min_y=Math.abs(grenades.get(i).getY()-y);
                 gx= grenades.get(i).getX();
                 gy= grenades.get(i).getY();
                 grenade= grenades.get(i).getDirection();
                 grenade_prepona=pp;
             }
         }

          g_prepona=Math.sqrt(gr_min_x*gr_min_x+gr_min_y*gr_min_y);
         
         //*******************************pre enemy
         if(enemy_x >=x && enemy_y >=y){
          
             double pom1 = (double) en_min_x/en_min_y;
             uhol=(int)(Math.atan(pom1)*(180.00/Math.PI));
             uhol += 270 + direction;
             
         }
         else if(enemy_x >=x && enemy_y <=y){
          
             double pom1 = (double) en_min_y/en_min_x;
             uhol=(int)(Math.atan(pom1)*(180.00/Math.PI));
             uhol+=direction;
           
             
         }
         else if(enemy_x <=x && enemy_y <=y){
        
             double pom1 = (double) en_min_x/en_min_y;
             uhol=(int)(Math.atan(pom1)*(180.00/Math.PI));
             uhol+= 90 + direction;
         }
         else {
         
             double pom1 = (double) en_min_y/en_min_x;
             uhol=(int)(Math.atan(pom1)*(180.00/Math.PI));
             uhol += 180 + direction;
         }

            //*********************pre grenades
        
            if( g_prepona<=300)
            {
                //1.kvadrant
                if(((gx >=x && gy >=y) && (grenade>=180 && grenade<270))){
                    if(Math.abs(gx-x) < Math.abs(gy-y))
                    {
                 uhol_grenade = direction + 45;
                 return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, uhol_grenade, null);
                    }
                 else
                {
                    uhol_grenade = direction + 225;
                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, uhol_grenade, null);

                }
                }
                //2.kvadrant
                 if(((gx <=x && gy >=y) && (grenade>=270 && grenade<360))){
                    if(Math.abs(gx-x) < Math.abs(gy-y))
                    {
                 uhol_grenade = direction + 135;
                 return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, uhol_grenade, null);
                    }
                 else
                {
                    uhol_grenade = direction + 315;
                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, uhol_grenade, null);

                }
                }
                 //3.kvadrant
                  if(((gx >=x && gy <=y) && (grenade>=90 && grenade<180))){
                    if(Math.abs(gx-x) < Math.abs(gy-y))
                    {
                 uhol_grenade = direction + 315;
                 return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, uhol_grenade, null);
                    }
                 else
                {
                    uhol_grenade = direction + 135;
                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, uhol_grenade, null);

                }
                }
                  //4.kvadrant
                   if(((gx<=x && gy <=y) && (grenade>=0 && grenade<90))){
                    if(Math.abs(gx-x) < Math.abs(gy-y))
                    {
                 uhol_grenade = direction + 225;
                 return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, uhol_grenade, null);
                    }
                 else
                {
                    uhol_grenade = direction + 45;
                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, uhol_grenade, null);
                }
                }
            }

          if(info.isCanShoot()){ 
          return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT,uhol,CommandType.SHOOT);}

          return com;
    }
    
    @Override
    public String getName() {
        return "PJO0001";
    }
    
}