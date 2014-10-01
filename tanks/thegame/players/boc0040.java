/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.ArrayList;
import java.util.List;
import java.lang.*;
import java.util.Random;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;

/**
 *
 * @author beh01
 */
public class boc0040 implements IPlayer {
    private Info info;
    private InfoDetail infodetail;
    private int uhel = 0;
    private int pomx = 0;
     private int pomy = 0;
      private int x = 0;
     private int y = 0;
    private int pom ;
    private int pom2 ;
     private int p1 ;
    private int p2 ;
    private double c = 0;
    private double e = 0;
    private double G =0;
    private double H =0;
    private double b = 0;
    private double a = 0;
    private double g = 0;
    private double alfa = 0;
    private double alfa1 = 0;

    @Override
    public Command planNextMove(Info info) {
        uhel++;
        List<InfoDetail> enemies = new ArrayList<InfoDetail>();


       enemies=info.getEnemies();

       for(InfoDetail enemy : enemies)
       {
        //System.out.println("x0 : "+info.getX());
        //System.out.println("y0 : "+info.getY());
        //System.out.println("uhel:  "+uhel);

         //System.out.println("A0 : "+enemy.getX());

          //System.out.println("B0 : "+enemy.getY());
        pomx=enemy.getX();
         pomy=enemy.getX();

         x = info.getX();
         y = info.getY();
       }

       for(InfoDetail enemy : enemies)
       {
        p1 = info.getX()-x;
       p2= info.getY()-y;
        //System.out.println("x1 : "+p1);
        //System.out.println("y1 : "+p2);
        //System.out.println("uhel:  "+uhel);
       pom = enemy.getX()-pomx;
       pom2= enemy.getY()-pomy;
        //System.out.println("A1 : "+pom);

          //System.out.println("B1 : "+pom2);

          c = Math.sqrt((p1-pom)*(p1-pom)+(p2-pom2)*(p2-pom2));
           //System.out.println("c : "+c);

           e = c/GameData.GRENADE_SPEED;
            //System.out.println("e : "+e);

            G = pom + (e*pom);
            H = pom2 + (e*pom2);

            //System.out.println("G : "+G);
            //System.out.println("H : "+H);

            b = Math.sqrt((p1-G)*(p1-G)+(p2-H)*(p2-H));
            a = Math.sqrt((x-G)*(x-G)+(y-H)*(y-H));
            g = Math.sqrt((x-p1)*(x-p1)+(y-p2)*(y-p2));
            alfa = Math.cos((b*b)+(g*g)-(a*a)/(2*b*g));
            alfa1 = Math.acos(alfa);
            //System.out.println("alfa : "+alfa1);


       }

       if( uhel>360)
       {
        uhel =0;
            return new Command(CommandType.MOVE_FORWARD, CommandType.NONE,(int)alfa1, CommandType.SHOOT);
       }
        if(info.getY()< 100|| info.getY()> 900 )
        {

          return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT,uhel, CommandType.SHOOT);
        }
            if(info.getX()< 100|| info.getX()> 900 )
        {

          return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT,uhel, CommandType.SHOOT);
        }
      return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT,1, CommandType.SHOOT);
    }

    @Override
    public String getName() {
        return "My Player";
    }

}
