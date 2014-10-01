/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;
import thegame.model.ModelObject;

/**
 *
 * @author Coolek
 */
public class Kol0232 implements IPlayer{
    private boolean underAtack = false;
    private int pocitadlo_strel = 0;
    
    private boolean jdeKulka(Info info) {
       for(InfoDetail o : info.getGrenades()){
           int smer = o.getDirection();
           int pozice1 = 180 + (int)Math.toDegrees(Math.atan2(o.getY()-info.getY(),o.getX()-info.getX()));
           if(smer < pozice1+5 && smer>pozice1-5){
               return true;
           }
       }
       return false;
    }
    
    public boolean JeNaPlose(Info info){//osetreni aby mi tak nevyjel z pole
        int m_X = info.getX();
        int m_Y = info.getY();
        if((m_X >= GameData.GAME_WIDTH-50)||(m_X <= 50)||(m_Y >= GameData.GAME_HEIGTH-50)||(m_Y <= 50)){
            return false;
        }
        return true;
    }
    @Override
    public Command planNextMove(Info info) {
        int m_X = info.getX();
        int m_Y = info.getY();
        int smer = info.getDirection();
        if(JeNaPlose(info)==false){
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, 180, CommandType.NONE);
        }
        
        if(jdeKulka(info)) {
            if(underAtack){
                return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.NONE);
            }
            else{
                underAtack = true;
                return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, 90, CommandType.NONE);
            }
        }else{
            underAtack = false;
        }

        double vzdalenost=0.0;
        double pom = 0.0;
        InfoDetail objekt = null;
        for(InfoDetail o : info.getEnemies()){
            int a,b;
            a = o.getX() - m_X;
            b = o.getY() - m_Y; 
            vzdalenost = Math.sqrt(Math.pow(a,2)+Math.pow(b,2));//vypocteni vzdalenosti tanku
            if(objekt == null){ 
                objekt = o;
                pom = vzdalenost;
            }
             if(vzdalenost < pom){
                 pom = vzdalenost;
                 objekt = o;     
             }  
         }
        
        if(objekt != null){
            int a,b;
            //vypocteni uhlu pro strelbu
            if(pocitadlo_strel==0){
                a = objekt.getX() - m_X + 20;
                b = objekt.getY() - m_Y; 
                pocitadlo_strel=(pocitadlo_strel+1)%4;
            }else if(pocitadlo_strel==1){
                a = objekt.getX() - m_X - 20;
                b = objekt.getY() - m_Y; 
                pocitadlo_strel=(pocitadlo_strel+1)%4;
            }else if(pocitadlo_strel==2){
                a = objekt.getX() - m_X;
                b = objekt.getY() - m_Y + 20; 
                pocitadlo_strel=(pocitadlo_strel+1)%4;
            }else if(pocitadlo_strel==3){
                a = objekt.getX() - m_X;
                b = objekt.getY() - m_Y - 20; 
                pocitadlo_strel=(pocitadlo_strel+1)%4;
            }else{
                a = objekt.getX() - m_X;
                b = objekt.getY() - m_Y; 
            }
            int uhel_strelby = (360-smer) + (int) Math.toDegrees(Math.atan2(b,a));
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, uhel_strelby, CommandType.SHOOT);
        }
        
        return new Command(CommandType.NONE, CommandType.NONE, 0, CommandType.NONE);
    }

    @Override
    public String getName() {
        return "KOL0232";
    }
}
