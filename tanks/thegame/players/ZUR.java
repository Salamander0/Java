/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.List;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;

/**
 *
 * @author beh01
 */
public class ZUR implements IPlayer{

    @Override
    public Command planNextMove(Info info) {
        // you can NONE, MOVE_FORWARD, MOVE_BACK, TURN_LEFT, TURN_RIGHT, SHOOT
        InfoDetail myInfo = new InfoDetail(info.getX(), info.getY(), info.getDirection());
        
        if(info.getEnemies().isEmpty() && info.getGrenades().isEmpty()){
            return new Command(CommandType.NONE, CommandType.NONE, 0, CommandType.NONE);
        }
        
        if(info.getGrenades().isEmpty()){
            return new Command(CommandType.NONE, CommandType.TURN_RIGHT, aimRight(firstEnemy(info.getEnemies(), myInfo), myInfo), CommandType.SHOOT);
        }
        
        InfoDetail granat=firstGranateForMe(myInfo, info.getGrenades());
        if(granat!=null){
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, 90+granat.getDirection()-myInfo.getDirection(), CommandType.NONE);
        }
        
        if(!info.getEnemies().isEmpty()){
            return new Command(CommandType.NONE, CommandType.TURN_RIGHT, aimRight(firstEnemy(info.getEnemies(), myInfo), myInfo), CommandType.SHOOT);
        }
        
        return new Command(CommandType.NONE, CommandType.NONE, 0, CommandType.NONE);
    }
    
    private InfoDetail firstEnemy(List<InfoDetail> enemies, InfoDetail me){
        double vzdalenost=distanceFrom(enemies.get(0), me);
        InfoDetail enemy = enemies.get(0);
        for(int i=1; i<enemies.size(); i++){
            if(vzdalenost>distanceFrom(enemies.get(i), me)){
                enemy=enemies.get(i);
            }
        }
        return enemy;
    }
    
    private InfoDetail firstGranateForMe (InfoDetail me, List<InfoDetail> granates){
        if(granates.isEmpty()){
            return null;
        }
        InfoDetail granat=null;
        for(int i=0; i<granates.size(); i++){
            if(isForMe(me, granates.get(i))){
                granat=granates.get(i);
                break;
            }
        }
        if(granat==null){
            return null;
        }
        for(int i=0; i<granates.size(); i++){
            if(isForMe(me, granates.get(i)) && distanceFrom(granates.get(i), me)<distanceFrom(granat, me)){
                granat=granates.get(i);
            }
        }
        return granat;
    }
    
    private boolean isForMe(InfoDetail me, InfoDetail granat){
        
        int uhel=getAngle(granat, me);
        int m_uhel, v_uhel;
        int tank_size=(int)Math.sqrt(Math.pow(GameData.TANK_SIZE, 2)*2)/2;
        
        if(me.getY()<granat.getY()){
            if(me.getX()>granat.getX()){
                m_uhel=getAngle(me.getX(), me.getY(), granat.getX()-tank_size, granat.getY()-tank_size);
                v_uhel=getAngle(me.getX(), me.getY(), granat.getX()+tank_size, granat.getY()+tank_size);
            }else{
                m_uhel=getAngle(me.getX(), me.getY(), granat.getX()-tank_size, granat.getY()+tank_size);
                v_uhel=getAngle(me.getX(), me.getY(), granat.getX()+tank_size, granat.getY()-tank_size);
            }
        }else{
            if(me.getX()<granat.getX()){
                m_uhel=getAngle(me.getX(), me.getY(), granat.getX()+tank_size, granat.getY()+tank_size);
                v_uhel=getAngle(me.getX(), me.getY(), granat.getX()-tank_size, granat.getY()-tank_size);
            }else{
                m_uhel=getAngle(me.getX(), me.getY(), granat.getX()+tank_size, granat.getY()-tank_size);
                v_uhel=getAngle(me.getX(), me.getY(), granat.getX()-tank_size, granat.getY()+tank_size);
            }
        }
        
        return uhel>m_uhel && uhel<v_uhel;
    }
    
    private int aimRight(InfoDetail enemy, InfoDetail me){
        return getAngle(enemy, me)-me.getDirection();
    }
    
    private double distanceFrom(InfoDetail enemy, InfoDetail me){
        return distanceFrom(enemy.getX(), enemy.getY(), me.getX(), me.getY());
    }
    
    private double distanceFrom(int aX, int aY, int bX, int bY){
        int a=Math.abs(aX-bX);
        int b=Math.abs(aY-bY);
        return Math.sqrt(a*a+b*b);
    }
    
    private int getAngle(InfoDetail a, InfoDetail b){
        return getAngle(a.getX(), a.getY(), b.getX(), b.getY());
    }
    
    private int getAngle(int aX, int aY, int bX, int bY){
        
        int svisla_vzdalenost=Math.abs(aY-bY);
        double prepona=distanceFrom(aX, aY, bX, bY);
        
        if(aY<bY){
            if(aX>bX){
                return (360-(int)Math.toDegrees(Math.sinh(svisla_vzdalenost/prepona)));
            }else{
                return (180+(int)Math.toDegrees(Math.sinh(svisla_vzdalenost/prepona)));
            }
        }else{
            if(aX<bX){
                return (180-(int)Math.toDegrees(Math.sinh(svisla_vzdalenost/prepona)));
            }else{
                return ((int)Math.toDegrees(Math.sinh(svisla_vzdalenost/prepona)));
            }
        }
    }

    @Override
    public String getName() {
        return "ZUR0020";
    }
    
}
