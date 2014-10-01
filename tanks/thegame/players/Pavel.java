/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package thegame.players;


import java.util.ArrayList;
import java.util.List;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.ModelPlayer;
import thegame.model.Tank;

/**
 *
 * @author Pajan
 */
public class Pavel implements IPlayer{
    double[] enemyAngle;
    double[] enemyDistance;
    int angle,x,y;
    int xDis,yDis;
    //sort
    public static void bubbleSort(double[] array){
    for (int i = 0; i < array.length - 1; i++) {
        for (int j = 0; j < array.length - i - 1; j++) {
            if(array[j] < array[j+1]){
                double tmp = array[j];
                array[j] = array[j+1];
                array[j+1] = tmp;
            }
        }
    }
} 
    @Override
    
    public Command planNextMove(Info info) {
       enemyDistance=null;
       enemyDistance= new double[info.getEnemies().size()];
       enemyAngle=null;
       enemyAngle= new double[info.getEnemies().size()];
       x=info.getX();
       y=info.getY();
       angle=info.getDirection();
       int i=0;
       for(InfoDetail enemy : info.getEnemies())
       {
           if(enemy.getX() > x)
               xDis=enemy.getX()-x;
           else
               xDis=x-enemy.getX();
           if(enemy.getY() > y)
               yDis=enemy.getY()-y;
           else
               yDis=y-enemy.getY();
           enemyDistance[i]= Math.sqrt(xDis*xDis+yDis*yDis);           
           enemyAngle[i++]=(double)yDis/xDis;
       }
       double delka=1000;
       int tmp=0;
       for(int a=0;a<enemyDistance.length;a++)
       {
           if(enemyDistance[a]<delka)
           {    
               tmp=a;
               delka=enemyDistance[a];
           }
       }
       
       angle=(int)Math.toDegrees(Math.atan(enemyAngle[tmp]));
       
        int test_cornerx = info.getX();
        int test_cornery = info.getY();
        
        if (test_cornerx <= 20 || test_cornerx >= 1900 || test_cornery <= 20 || test_cornery >= 1900)
        {
            boolean test = false;
            int uhel = info.getDirection();
           return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, 180, CommandType.NONE);
            
            
        }
       
    
       // return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.SHOOT);
        return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, (360+info.getDirection()-angle), CommandType.SHOOT);
       
    }

    @Override
    public String getName() {
        return "Frosty";
    }
    
}
