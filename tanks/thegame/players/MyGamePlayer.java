/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;

/**
 *
 * @author Erik
 */
public class MyGamePlayer implements IPlayer{    
    private Random random = new Random(System.currentTimeMillis());
    private boolean started;
    private CommandType currentMove;
    private CommandType palba;

    @Override
    public String getName() {
        return "GRE0071";
    }

    @Override
    public Command planNextMove(thegame.info.Info info)  {
        InfoDetail mojeInfo = null;
        
        List<InfoDetail> myList = info.getEnemies();
        List<InfoDetail> granat = info.getGrenades();
        
        //testovaci segment
        CommandType turnCommand = CommandType.NONE;
        int angle = 0;
        // **
        
       if(info.getX() <= 10 || info.getX() >= 1990 || info.getY() <= 10 || info.getY() >= 1990){
           turnCommand = CommandType.TURN_LEFT;
           angle = 180;
           
       }else{
           currentMove = CommandType.MOVE_FORWARD;
                 
       }
       

       
                    double k = 0;
                    int q = 0;
                    int y = 0;
       
       
        for(InfoDetail x : myList)
        {
            
        /* double vzdalenost =  Math.sqrt((info.getX()-info.getY())^2+(x.getX()-x.getY())^2);
         if(vzdalenost < 1){

                 turnCommand = CommandType.TURN_LEFT;
             
         }*/
             
                 k = (x.getDirection()*Math.PI)/180;
                 k = Math.tan(k);
                 q = x.getY();

                 y = (int) (k*info.getX() + q);
                     
                 
                 if(y == info.getY()){
                     turnCommand = CommandType.TURN_LEFT;
                 }
             
            if(info.getDirection() == x.getDirection()){
              //  palba = CommandType.SHOOT;
            }
             


        } 
            if(random.nextInt(100)%2 == 0){
                if(random.nextInt(100)%3 == 1){
                  angle = 90;
                  turnCommand = CommandType.TURN_LEFT;
                }
                
            }else{
                if(random.nextInt(100)%7 == 1){
                  palba = CommandType.SHOOT;  
                }
            }
       

        
        InfoDetail nejblizsiTank = null;
        double vzdalenost = 0;
        double nejmensiVzdalenost = 1000000;
        
        for(InfoDetail x : myList)
        {
            //odm.((a1-b1)^2+(a2-b2)^2)
            //vzdalenost = Math.sqrt(Math.exp(x.getX()-))
            //if vzdalenost < nejmensiVzdalenost -> uloz, pokud neni mensi, nedelej nic
            if(vzdalenost < nejmensiVzdalenost)
            {
                nejmensiVzdalenost = vzdalenost;
                nejblizsiTank = x;
            }
        }
        
        //TODO otocit se na nejblizsi tank a vystrelit
        //zjistit jestli na me leti nejake strely
        //vybrat smer, kam jet
        
        

/*
        if (random.nextDouble() < currentMoveDuration * currentMoveDuration * currentMoveConst) {
            currentMove = random.nextBoolean() ? CommandType.MOVE_FORWARD : CommandType.MOVE_BACK;
            currentMoveDuration = 0;
        }

        if (random.nextDouble() < noTurnDuration * noTurnDuration * noTurnConst) {
            turnCommand = random.nextBoolean() ? CommandType.TURN_LEFT : CommandType.TURN_RIGHT;
            angle = random.nextInt(180);
            noTurnDuration = 0;
        }*/
        
       
        return new Command(currentMove, turnCommand, angle, palba);
    }
}