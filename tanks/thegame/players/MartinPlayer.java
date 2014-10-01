/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;

/**
 *
 * @author Martin
 */
public class MartinPlayer implements IPlayer{

    private int my_x;
    private int my_y;
    private double my_direction;
    private List<InfoDetail> seznamnepritel = new ArrayList<InfoDetail>();
    private List<InfoDetail> seznamgranat = new ArrayList<InfoDetail>();
    private boolean attack;
    private boolean posun;
    
    @Override
    public Command planNextMove(Info info) {
       
        

        
        my_x = info.getX();
        my_y = info.getY();
        my_direction = info.getDirection();
        seznamnepritel = info.getEnemies();
        seznamgranat = info.getGrenades();
        double smerstrileni = 0;

        if(!seznamgranat.isEmpty())
        {
            
            int nejblizsistrela = 50000;
            
            for(InfoDetail granat : seznamgranat)
            {
                if(new Point(granat.getX(), granat.getY()).distance(new Point(my_x, my_y)) < nejblizsistrela);
                {
                    nejblizsistrela = (int) (new Point(granat.getX(), granat.getY()).distance(new Point(my_x, my_y)));
                    if(nejblizsistrela < 90)
                    {
                        if(my_x > GameData.GAME_WIDTH-20 || my_x < 20 || my_y > GameData.GAME_HEIGTH-20 || my_y < 20)
                        {
                            return new Command(CommandType.MOVE_FORWARD, CommandType.NONE , (int)my_direction-180, CommandType.NONE);
                        }
                        else
                        {
                            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, (int) granat.getDirection()+90, CommandType.NONE);
                        }
                    }
                }
            }
        }
                
         if(!seznamnepritel.isEmpty())
         {
            InfoDetail nepritel = null;
            int nejblizsicil = 50000;
            
            double sizex, sizey;
            double angle;
            
            for (InfoDetail tank : seznamnepritel)
            {
                if(new Point(tank.getX(), tank.getY()).distance(new Point(my_x, my_y)) < nejblizsicil)
                {
                    nejblizsicil = (int) (new Point(tank.getX(), tank.getY()).distance(new Point(my_x, my_y)));
                    nepritel = tank;
                }
            }
                sizex = my_x - nepritel.getX();
                sizey = my_y - nepritel.getY();
                angle = Math.atan(sizey/sizex);
                int koef = -1 ;
                if( sizex < 0)
                {
                    koef = 1;
                }
                else
                    
                {
                    angle = Math.PI - angle;
                }
                if(sizex == 0)
                {
                    angle = Math.PI / 2;
                    if( sizey < 0)
                    {
                        angle = angle * (-1);
                    }
                }
                angle = angle * koef * 180/Math.PI;
                smerstrileni = my_direction - angle;
                
                Random r = new Random();
                smerstrileni += r.nextInt(5);
        
                if(info.isCanShoot())
                {
                    attack = true;
                }
                else
                {
                    attack = false;
                }
                if(attack == true)
                {
                    return new Command(CommandType.NONE, CommandType.TURN_LEFT , (int)smerstrileni, CommandType.SHOOT);
                }
                else
                {   
                    if(my_x > GameData.GAME_WIDTH-20 || my_x < 20 || my_y > GameData.GAME_HEIGTH-20 || my_y < 20)
                    {
                        return new Command(CommandType.MOVE_FORWARD, CommandType.NONE , (int)my_direction-180, CommandType.NONE);
                    }
                    else
                    {
                        return new Command(CommandType.MOVE_FORWARD, CommandType.NONE , 0, CommandType.NONE);
                    }
                }
         }
//         if(seznamgranat.isEmpty() && seznamnepritel.isEmpty())
//         {
//            return new Command(CommandType.NONE, CommandType.NONE , 0, CommandType.NONE);
//         }
         
         return new Command(CommandType.NONE, CommandType.NONE , 0, CommandType.NONE);
         
         
         
    }
    @Override
    public String getName() {
        return "Martin";
    }
    
}