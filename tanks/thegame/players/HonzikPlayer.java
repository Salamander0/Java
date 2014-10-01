/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.Random;
import thegame.info.Info;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;
/**
 *
 * @author beh01
 */
public class HonzikPlayer implements IPlayer {
    private boolean Check(Info info)
    {
       if(info.getX()> GameData.GAME_WIDTH-20 || info.getX() < 20 || info.getY()> GameData.GAME_HEIGTH-20 || info.getY() < 20){
           return false;
       }else{
           return true;
       }
       
    }
    
    private boolean greenade(Info info, int angle2)
    {
        
       int e_x = info.getGrenades().get(0).getX(); 
       int e_y = info.getGrenades().get(0).getY();
       int pomx,pomy,pomx1,pomy1;
       int pos =0;
       for(int i=1;i<info.getGrenades().size();i++)
       {
           pomx  =(info.getGrenades().get(i).getX()-info.getX());
           if(pomx<0)
               pomx *=-1;
           pomx1 = e_x -info.getX() ;
            if(pomx1<0)
               pomx1 *=-1;       
           pomy  =(info.getGrenades().get(i).getY()-info.getY());
           if(pomy<0)
               pomy *=-1;
           pomy1 = e_y -info.getY() ;
            if(pomy1<0)
               pomy1 *=-1; 
           if(pomx< pomx1 && pomy < pomy1)
           {
               e_x = info.getGrenades().get(i).getX();
               e_y = info.getGrenades().get(i).getY();
               pos = i;
           }
       }
       pomx1 = e_x -info.getX() ;
       pomy1 = e_y -info.getY() ;
       if(pomx1<0)
           pomx1 *=-1;       
       if(pomy1<0)
           pomy1 *=-1; 
       
        int angle = info.getGrenades().get(pos).getDirection();
       
       if(info.getGrenades().isEmpty()==false)
       {
           
           if(info.getDirection() != info.getGrenades().get(pos).getDirection())
           {
           /* if(info.getX()> e_x && info.getY()> e_y && (angle >0 && angle < 270)) 
            {
                    if(pomx1 <40 && pomy1 <40)
                    {
                        System.out.println("Alert1");
                        angle =info.getDirection();
                        if(angle >90 && angle < 180)
                        {
                            return false;
                        }
                           
                    }
            }
            if(info.getX()> e_x && info.getY()< e_y && (angle >0 && angle < 90)) 
            {
                     if(pomx1 <40 && pomy1 <40)
                    {
                        System.out.println("Alert2");
                        angle =info.getDirection();
                        if(angle >180 && angle < 270)
                        {
                            return false;
                        }
                    }
            }
            if(info.getX()< e_x && info.getY()< e_y && (angle >90 && angle < 180)) 
            {
                     if(pomx1 <40 && pomy1 <40)
                    {
                        System.out.println("Alert3");
                        angle =info.getDirection();
                        if(angle >270 && angle < 360)
                        {
                            return false;
                        }
                    }
            }
            if(info.getX()< e_x && info.getY()> e_y && (angle >180 && angle < 270)) 
            {
                    if(pomx1 <40 && pomy1 <40)
                    {
                        System.out.println("Alert4");
                        angle =info.getDirection();
                        if(angle >0 && angle < 90)
                        {
                            return false;
                        }
                    }
            }
            if(info.getX()== e_x && info.getY()< e_y && (angle >85 && angle < 95)) 
            {
                     if(pomx1 <40 && pomy1 <40)
                    {
                        System.out.println("Alert5");
                        return false;
                    }
            }
            if(info.getX()== e_x && info.getY()> e_y && (angle >265 && angle < 275)) 
            {
                     if(pomx1 <40 && pomy1 <40)
                    {
                        System.out.println("Alert6");
                        return false;
                    }
            }
            if(info.getX() < e_x && info.getY() == e_y && (angle >175 && angle < 185)) 
            {
                     if(pomx1 <40 && pomy1 <40)
                    {
                        System.out.println("Alert7");
                        return false;
                    }
            }
            if(info.getX() > e_x && info.getY() == e_y && (angle >355 && angle < 5)) 
            {
                    if(pomx1 <40 && pomy1 <40)
                    {
                        System.out.println("Alert8");
                        return false;
                    }
            }*/
              return false;
           }
           return true;
       }
       else
       {
          return true;
       }
    }
     private int getAngle(Info info,int e_x, int e_y){
       double angle = 0;
       
       int my_x= info.getX();
       int my_y= info.getY();
       int smer = info.getDirection();
       
       if((my_y > e_y && my_x < e_x )||( my_y < e_y && my_x > e_x))
       {
            angle += (Math.atan(Math.abs(((double)(my_x-e_x))/((double)(my_y-e_y)))))*(180.00 / Math.PI);
       }
       else
       {
            angle += 90.00 - (Math.atan(Math.abs(((double)(my_x-e_x))/((double)(my_y-e_y)))))*(180.00 / Math.PI);
       }
      
       if(my_y > e_y && my_x > e_x){
           angle +=180;
       }
       else if(my_y > e_y && my_x < e_x){
           angle +=270;
       }
       else if(my_y < e_y && my_x < e_x){
            angle +=0;
       }
       else if(my_y < e_y && my_x > e_x){
            angle +=90;
       }
       else if(my_y == e_y && my_x > e_x){
            angle +=0;
       }
       else if(my_y == e_y && my_x < e_x){
            angle +=180;
       }
       else if(my_y > e_y && my_x == e_x){
            angle +=270;
       }
       else if(my_y < e_y && my_x == e_x){
            angle +=90;
       }
     
       Random r = new Random();
       int rand = r.nextInt(3);
       if(rand == 0){
           //prima strela
           angle += 0;
       }else if(rand == 1){
           angle += 7;
       }else{
           angle -= 7;
       }
       
       angle -= smer;
       return (int)angle;
   }
    @Override
    public Command planNextMove(Info info) {
      
       if(info.getEnemies().isEmpty()==false)
       {
       int e_x = info.getEnemies().get(0).getX(); 
       int e_y = info.getEnemies().get(0).getY();
       int pomx,pomy,pomx1,pomy1;
       int pos =0;
       for(int i=1;i<info.getEnemies().size();i++){
           pomx  =(info.getEnemies().get(i).getX()-info.getX());
           if(pomx<0)
               pomx *=-1;
           pomx1 = e_x -info.getX() ;
            if(pomx1<0)
               pomx1 *=-1;       
           pomy  =(info.getEnemies().get(i).getY()-info.getY());
           if(pomy<0)
               pomy *=-1;
           pomy1 = e_y -info.getY() ;
            if(pomy1<0)
               pomy1 *=-1; 
           if(pomx< pomx1 && pomy < pomy1)
           {
               e_x = info.getEnemies().get(i).getX();
               e_y = info.getEnemies().get(i).getY();
               pos = i;
           }
       }
       
       
           int angle =0;
            e_x = info.getEnemies().get(pos).getX();
            e_y = info.getEnemies().get(pos).getY();
           angle = getAngle(info,e_x,e_y);
           if(Check(info))
           {   
               // if(greenade(info, angle))
               // {
                  return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT,angle , CommandType.SHOOT);           
           /* }
                else
              {
                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, 5 , CommandType.SHOOT);  
                }*/
           }
            else
            {
                return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, (angle+180) , CommandType.NONE);           
            }
        }
       else
       {
           return new Command(CommandType.NONE, CommandType.NONE, 0 , CommandType.NONE);
       }
       
            
           
     
         
    }

    @Override
    public String getName() {
        return "Honzik";
    }
    
}
