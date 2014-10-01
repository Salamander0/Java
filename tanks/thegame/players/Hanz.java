/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.List;
import java.util.Random;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;
import thegame.model.ModelObject;

import java.awt.Point;

/**
 *
 * @author KON0191
 */

public class Hanz implements IPlayer {
    private class TankInfo{
        public int distance;
        public int speed;
        public Point pos;
        public int direction;
        
    }
    
    private List<Integer> collisionDirection;
    private List<Integer> nextPosiotionCollisionDirection;
    private int newDirection;
    private boolean nextPositionSafe;
    private boolean thisPositionSafe;

    @Override
    public Command planNextMove(Info info) {
        Random r = new Random();
        int newDirection;
        int leftBorderCourse;
        int rightBorderCourse;
        collisionDirection = new ArrayList<Integer>();
        nextPosiotionCollisionDirection=new ArrayList<Integer>();
       // Map<Integer, Point> tankList = new TreeMap<Integer, Point>();

        Point myPos = new Point(info.getX(), info.getY());
        Point myNextPos=findNextPosition(myPos, info.getDirection());
        Point granate = new Point();
        Point tank = new Point();
        nextPositionSafe=true;
        if (!info.getGrenades().isEmpty()) {
            
            for (InfoDetail o : info.getGrenades()) {
                granate.x = o.getX();
                granate.y = o.getY();

                leftBorderCourse = findCollisionCourse(granate, findLeftBorder(myPos, findCollisionCourse(granate, myPos)));
                rightBorderCourse = findCollisionCourse(granate, findRightBorder(myPos, findCollisionCourse(granate, myPos)));
                //vypocet smeru pro strefeni leveho a praveho okraje tanku
                //System.out.println("Kolizni kurz granatu: " + findCollisionCourse(granate, myPos));

                if (leftBorderCourse < rightBorderCourse) {
                    rightBorderCourse -= 360;
                }
                //System.out.println("Levy kurz granatu: " + leftBorderCourse + " pravy kurz granatu: " + rightBorderCourse);
                if (rightBorderCourse < o.getDirection() && o.getDirection() < leftBorderCourse) {
                    //pokud je smer objektu mezi smery ke krajum tanku, je nebezpecny
                    collisionDirection.add(o.getDirection());
                    thisPositionSafe=false;
                    // System.out.println("Novy nebezpecny objekt na: " + o.getX() + " " + o.getY() + " o smeru " + o.getDirection());
                }

            }
            for (InfoDetail o : info.getEnemies()) { //stejny zpusob jako nahore, jen resis jestli na me "kouka" nejaky tank
                tank.x = o.getX();
                tank.y = o.getY();

                leftBorderCourse = findCollisionCourse(tank, findLeftBorder(myPos, findCollisionCourse(tank, myPos)));//najdi levy a pravy kraj a vypocitej na nej vektor
                rightBorderCourse = findCollisionCourse(tank, findRightBorder(myPos, findCollisionCourse(tank, myPos)));
                //vypocet smeru pro strefeni leveho a praveho okraje tanku
                //System.out.println("Kolizni kurz granatu: " + findCollisionCourse(granate, myPos));

                if (leftBorderCourse < rightBorderCourse) {
                    rightBorderCourse -= 360;
                }
                //System.out.println("Levy kurz granatu: " + leftBorderCourse + " pravy kurz granatu: " + rightBorderCourse);
                if (rightBorderCourse < o.getDirection() && o.getDirection() < leftBorderCourse) {
                    //pokud je smer objektu mezi smery ke krajum tanku, je nebezpecny
                    collisionDirection.add(o.getDirection());
                    //System.out.println("Novy nebezpecny objekt na: " + o.getX() + " " + o.getY() + " o smeru " + o.getDirection());
                }

            }
             nextPositionSafe=true;
             for (InfoDetail o : info.getGrenades()) { //zjisti zda muzeme dopredu
                granate.x = o.getX();
                granate.y = o.getY();

                leftBorderCourse = findCollisionCourse(granate, findLeftBorder(myPos, findCollisionCourse(granate, myNextPos)));
                rightBorderCourse = findCollisionCourse(granate, findRightBorder(myPos, findCollisionCourse(granate, myNextPos)));
                //vypocet smeru pro strefeni leveho a praveho okraje tanku
                //System.out.println("Kolizni kurz granatu: " + findCollisionCourse(granate, myPos));

                if (leftBorderCourse < rightBorderCourse) {
                    rightBorderCourse -= 360;
                }
                //System.out.println("Levy kurz granatu: " + leftBorderCourse + " pravy kurz granatu: " + rightBorderCourse);
                if (rightBorderCourse < o.getDirection() && o.getDirection() < leftBorderCourse) {
                    //pokud je smer objektu mezi smery ke krajum tanku, je nebezpecny
                    nextPositionSafe=false;
                    // System.out.println("Novy nebezpecny objekt na: " + o.getX() + " " + o.getY() + " o smeru " + o.getDirection());
                }

            }
            if (!collisionDirection.isEmpty()) { //jestli byly nalezeny strely mirici na tank
                newDirection = findEscapeDirection(info.getDirection(), collisionDirection);
                collisionDirection.clear();
                if (getPosDifference(info.getDirection(), newDirection) > 90) {
                    newDirection += 180;
                    newDirection = newDirection % 360;
                }
                if(nextPositionSafe==false&&thisPositionSafe==true)newDirection=(newDirection+180)%360;
                
                // System.out.println("Smer Hanze: "+info.getDirection()+" spravny smer uteku: " + escapeDirection+" nebo: "+ (escapeDirection+180)%360);
                if ((info.getDirection() - newDirection) > 0) {
                    // System.out.println("Otacim se vlevo, o: "+getPosDifference(info.getDirection(), escapeDirection));


                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT,
                            getPosDifference(info.getDirection(), newDirection), CommandType.SHOOT);

                } else if ((info.getDirection() - newDirection) < 0) {
                    //    System.out.println("Otacim se vpravo, o: "+getPosDifference(info.getDirection(), escapeDirection));


                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT,
                            getPosDifference(info.getDirection(), newDirection), CommandType.SHOOT);


                } else if ((info.getDirection() - newDirection) == 0) {
                    return new Command(CommandType.MOVE_FORWARD, CommandType.NONE,
                            0, CommandType.SHOOT);
                }
            }

         //neleti na me zadny granat
        if (!info.getEnemies().isEmpty()) {
            {
                tank=findNearestEnemy(myPos, info.getEnemies());
                
                newDirection = findCollisionCourse(myPos, findNextPosition(tank, findNearestEnemyDirection(tank, info.getEnemies())));
                //findNextPosition(tank, findNearestEnemyDirection(tank, info.getEnemies()));
                //findNextPosition(tank,)
                if ((info.getDirection() - newDirection) > 0) {
                    // System.out.println("Otacim se vlevo, o: "+getPosDifference(info.getDirection(), escapeDirection));


                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT,
                            getPosDifference(info.getDirection(), newDirection), CommandType.SHOOT);

                } else if ((info.getDirection() - newDirection) < 0) {
                    //    System.out.println("Otacim se vpravo, o: "+getPosDifference(info.getDirection(), escapeDirection));


                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT,
                            getPosDifference(info.getDirection(), newDirection), CommandType.SHOOT);


                } else if ((info.getDirection() - newDirection) == 0) {
                    return new Command(CommandType.MOVE_FORWARD, CommandType.NONE,
                            0, CommandType.SHOOT);
                }

            }
        }

        /*else{
         * 
         return new Command(CommandType.NONE, CommandType.NONE, 0, CommandType.NONE);
         }
         */
        
        }
        return new Command(CommandType.NONE, CommandType.NONE, 0, CommandType.NONE);
    }

    @Override
    public String getName() {
        return "Hanz";
    }

    private int findCollisionCourse(Point startPosition, Point finalPosition) {
        //zjisti smer, ktery je nutny pro zasah obeti
        //vzorec pro vypocet uhlu vektoru u s vektorem v(1,0)
        int u1 = finalPosition.x - startPosition.x;  //u[p1-e1,
        int u2 = finalPosition.y - startPosition.y;    //p2-e2]
        // int v1=1;  //v[1,0]
        // int v2=0;
        int direction;

        direction = (int) (Math.toDegrees(Math.acos((u1) / (Math.sqrt(u1 * u1 + u2 * u2) * 1))) + 0.5);

        if (finalPosition.y < startPosition.y) {
            direction = 360 - direction;
        }

        return (direction < 0) ? -1 * direction : direction;
    }

    private int findEscapeDirection(int yourDirection, List<Integer> collisionDirection) {
        //najdi smer uteku podle stavajicich smeru leticich objektu
        int escapeDirection = -1;
        if (collisionDirection != null) {
            int fromUpCount = 0, fromDownCount = 0;
            int fromUp = 0, fromDown = 0;


            for (Integer dir : collisionDirection) {
                if (dir >= 0 && dir < 180) { //soucet uhlu 0 - 180
                    fromUp += dir;
                    fromUpCount++;
                    //System.out.println(fromUp);
                } else {               //soucet uhlu 180 - 360
                    fromDown += dir;
                    fromDownCount++;
                }

            }
            if (fromUpCount > 0 && fromDownCount > 0) { //jsou-li nenulove zprumeruj
                fromUp = fromUp / fromUpCount;
                fromDown = fromDown / fromDownCount;

                if (getPosDifference(fromUp, fromDown) == 180)//pokud jdou proti sobe
                {
                    escapeDirection = (fromUp + fromDown) / 2;
                } else {
                    escapeDirection = getNearestDirection(yourDirection, (fromUp + fromDown) / 2 + 90, (fromUp + fromDown) / 2 - 90);
                }
            } else if (fromUpCount <= 0 && fromDownCount <= 0) {
                return -1;
            } else if (fromUpCount == 0) {
                fromDown = fromDown / fromDownCount;
                escapeDirection = getNearestDirection(yourDirection, fromDown + 90, fromDown - 90);

            } else if (fromDownCount == 0) {
                fromUp = fromUp / fromUpCount;
                escapeDirection = getNearestDirection(yourDirection, fromUp + 90, fromUp - 90);
            }
            //zprumerovat smery
        }
        return escapeDirection;
    }

    private int getPosDifference(int one, int two) {
        //kladny rozdil
        if (one > two) {
            return one - two;
        } else {
            return two - one;
        }
    }

    private int getNearestDirection(int yourDirection, int firstDirection, int secondDirection) {
        if (getPosDifference(yourDirection, firstDirection) < getPosDifference(yourDirection, secondDirection)) {
            return firstDirection;
        } else {
            return secondDirection;
        }
    }

    private int lookAt() {



        return 0;
    }

    private Point findLeftBorder(Point myPos, int collisionCourse) {
        //nalezeni leveho okraje tanku z pohledu strely
        double angle = Math.PI * (collisionCourse + 90.0) / 180.0;
        int x = (int) (Math.round((50 / 2 + 1) * Math.cos(angle)));
        int y = (int) (Math.round((50 / 2 + 1) * Math.sin(angle)));
        //System.out.println(""+x+", "+y+" "+(collisionCourse+90));
     /*   System.out.println("cos: "+Math.cos(collisionCourse + 90));
         System.out.println("sin: "+Math.sin(collisionCourse + 90));*/
        return new Point(x + myPos.x, y + myPos.y);
    }

    private Point findRightBorder(Point myPos, int collisionCourse) {
        //nalezeni praveho okraje tankuk z pohledu strely
        double angle = Math.PI * (collisionCourse - 90.0) / 180.0;
        int x = (int) (Math.round((50 / 2 + 1) * Math.cos(angle)));
        int y = (int) (Math.round((50 / 2 + 1) * Math.sin(angle)));
        // System.out.println(""+x+", "+y+" "+(collisionCourse-90));
    /*    System.out.println("cos: "+Math.cos(collisionCourse - 90));
         System.out.println("sin: "+Math.sin(collisionCourse - 90));*/
        return new Point(x + myPos.x, y + myPos.y);
    }

    private Point findNextPosition(Point position, int direction) {
        //nalezeni praveho okraje tankuk z pohledu strely
        double angle = Math.PI * (direction) / 180.0;
        int x = (int) (Math.round((50 / 2 + 1) * Math.cos(angle)));
        int y = (int) (Math.round((50 / 2 + 1) * Math.sin(angle)));
        // System.out.println(""+x+", "+y+" "+(collisionCourse-90));
    /*    System.out.println("cos: "+Math.cos(collisionCourse - 90));
         System.out.println("sin: "+Math.sin(collisionCourse - 90));*/
        return new Point(x + position.x, y + position.y);
    }

    private int getDistance(Point first, Point second) {
        return (int) (first.distance(second) + 0.5);
    }
    private Point findNearestEnemy(Point myPos,List<InfoDetail> tanks){
        Point tank=new Point();
        Point returntank=new Point();
        int distance=1000000;
        
        for(InfoDetail o: tanks){
            tank.x = o.getX();
            tank.y = o.getY();
            if(getDistance(myPos,tank)<distance){
                distance=getDistance(myPos,tank);
                returntank.x=o.getX();
                returntank.y=o.getY();
            }
        }
        
        
        return returntank;
    }
    private int findNearestEnemyDirection(Point myPos,List<InfoDetail> tanks){
        Point tank=new Point();
        int direction=-1;
        int distance=1000000;
        for(InfoDetail o: tanks){
            tank.x=o.getX();
            tank.y=o.getY();
            if(getDistance(myPos,tank)<distance){
                distance=getDistance(myPos,tank);
                tank.x = o.getX();
                tank.y = o.getY();
                direction=o.getDirection();
            }
        }
        
        
        return direction;
    }
}