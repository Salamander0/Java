/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;

/**
 *
 * @author vanek
 */
public class MyPlayer implements IPlayer {
    private List<InfoDetail> dangerousGrenades;
    private int dangerRotate = 90;
    private int step = 0;

    @Override
    public Command planNextMove(Info info) {
        dangerousGrenades = new ArrayList<InfoDetail>();
        int operationSpace = 20;
        /*zjistím jestli na mě něco letí, pokud jo prchám, pokud ne najdu nejbližšího a střílím*/
        //grenades = info.getGrenades();
        boolean clear = true;
        for(InfoDetail g: info.getGrenades())
        {
            /*zjistit nejližší a pak reagovat na ni*/

            int deltaX = info.getX() - g.getX() ;
            int deltaY = info.getY() - g.getY() ;

            double podil = (double)deltaY/deltaX;
            double dir;
            if(deltaX < 0){
                dir =  180 +  Math.toDegrees(Math.atan(podil));
            }else{
                if(deltaX > 0 ){
                    dir =  360 +  Math.toDegrees(Math.atan(podil));
                }
                else{ // ´== 0
                    if(deltaY > 0){
                        dir =  90;
                    }else{
                        dir = 270;
                    }
                }
            }

            int distance = (int)Math.round(new Point(info.getX(), info.getY()).distance(new Point(g.getX(), g.getY())));
            int tolerance = (int)Math.toDegrees(Math.atan((( Math.sqrt( 2*GameData.TANK_SIZE*GameData.TANK_SIZE )/ 2 ) + 5 )/  distance)) + 1 ;
            if(correctDeg((int)dir) >= g.getDirection() - tolerance && correctDeg((int)dir) <= g.getDirection() + tolerance){ //se vzdalenostis
                clear = false;
                dangerousGrenades.add(g);
            }
        }

        if(clear == false){
                InfoDetail g = nearest(dangerousGrenades, new Point(info.getX(),info.getY()));
                CommandType rotate = CommandType.TURN_RIGHT; // pozor na zed abysme tam nevejeli

                int angle = correctDeg(g.getDirection() + dangerRotate);
                if((info.getY() <= GameData.TANK_SIZE/2 + operationSpace) || (info.getX() <= GameData.TANK_SIZE/2 + operationSpace) || (info.getX() >= GameData.GAME_WIDTH - GameData.TANK_SIZE/2 - operationSpace) || info.getY() >= GameData.GAME_HEIGTH - GameData.TANK_SIZE/2 - operationSpace){
                    dangerRotate = 270;
                }

                if(dangerRotate == 270)
                    step++;

                if(step > 100){
                    dangerRotate = 90;
                    step = 0;
                }

//                if((info.getY() >= GameData.TANK_SIZE/2 + operationSpace*4) || (info.getX() >= GameData.TANK_SIZE/2 + operationSpace*4))
//                    dangerRotate = 90;
//

                if(info.getDirection() != angle){
                    return new Command(CommandType.NONE, rotate, correctDeg(angle - info.getDirection()), CommandType.NONE);
                }
                else{
                        return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.NONE);
                }
        }

        if(clear == true && info.getEnemies().isEmpty() == false){
            InfoDetail nereastEnemi = nearest(info.getEnemies(), new Point(info.getX(),info.getY()));

            int deltaX = info.getX() - nereastEnemi.getX() ;
            int deltaY = info.getY() - nereastEnemi.getY() ;
            double podil = (double)deltaY/deltaX;
            double dir;
            if(deltaX < 0){
                dir =  180 +  Math.toDegrees(Math.atan(podil));
            }else{
                if(deltaX > 0 ){
                    dir =  360 +  Math.toDegrees(Math.atan(podil));
                }
                else{ // ´== 0
                    if(deltaY > 0){
                        dir =  90;
                    }else{
                        dir = 270;
                    }
                }
            }
           int angle = (int)Math.round(dir);
           if(info.getDirection() != correctDeg(angle + 180)){
                    return new Command(CommandType.NONE, CommandType.TURN_LEFT, correctDeg((info.getDirection() - angle) + 180), CommandType.SHOOT);
                } else {
                    return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.SHOOT);
                }
        }

        return new Command(CommandType.NONE, CommandType.NONE, 0, CommandType.NONE);


    }

    @Override
    public String getName() {
        return "MyPlayer";
    }

    private int correctDeg(int deg){
        if(deg >= 360)
            deg -= 360;
        if(deg < 0)
            deg += 360;
        return deg;
    }

    private InfoDetail nearest(List<InfoDetail> list, Point me){
        int minDistance = 999999999;
        InfoDetail nearest = null;
        for(InfoDetail e : list){
                int distance = (int)Math.round(me.distance(new Point(e.getX(), e.getY())));
                if(distance < minDistance && distance > 0){
                    minDistance = distance;
                    nearest = e;
                }
            }
        return nearest;
    }
}
