package thegame.players;

import java.util.ArrayList;
import java.util.List;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.GameData;


public class Ardente implements IPlayer
{
  private Info data;
  private List<Integer> distances;
  private int x;
  private int y;
  private int rotation = -1;


  public Ardente() {
    this.distances = new ArrayList();
  }
  
  public Command planNextMove(Info info)
  {

    this.distances.clear();
    this.x = (this.y = this.rotation = -1);

    this.data = info;
    this.x = info.getX();
    this.y = info.getY();
    this.rotation = info.getDirection();
   


    int targetInfo = getTarget();
    int target = targetInfo;
    int focus = target;
            if(data.getX() > (GameData.GAME_WIDTH-60) || data.getX() < 60){
            int dir = data.getDirection();
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, dir-180, CommandType.SHOOT);
        }
        
        if(info.getY() > (GameData.GAME_HEIGTH-60) || data.getY() < 60){
            int dir = data.getDirection();
            return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, dir-180, CommandType.SHOOT);
        }
    
    if (target == -1) {
      return new Command(CommandType.NONE, CommandType.NONE, 0, CommandType.NONE);
    }
    if (this.rotation != target) {
      if (this.rotation > target)
        focus = this.rotation - target;
      else if (this.rotation < target)
        focus = this.rotation - target;
    }else {
        focus  = 1;
    }

      return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, focus, CommandType.SHOOT); 
  }

  public String getName()
  {
    return "Ardente";
  }

  private int getTarget()
  {
    InfoDetail cil = null;
    int state;
    
    if (this.data != null) {
      for (InfoDetail o : this.data.getEnemies()) {
        if (o != null) {
            cil = o;
          
        }
      }
      if (cil == null) {
        state = -1;
        return state;
      }

      int dX = Math.abs(this.x - cil.getX());
      int dY = Math.abs(this.y - cil.getY());
      
      double distance = Math.sqrt(dX * dX + dY * dY);

      double angle = Math.toDegrees(Math.atan(dY / dX));
      System.out.println(angle+"\n");
      System.out.print(dX+": X");
      System.out.print(dY+": Y");
     if (cil.getX() >= this.x) {
        if (cil.getY() >= this.y)
        {
          angle =+ angle/2;
        }
        else
        {
          angle = 360 - angle;
        }

      }
      if (cil.getY() >= this.y) {
        angle = 180 - angle;
      }
      else {
        angle = 180 + angle;
      }


      state = ((int)angle);
      return state;
    }

    state = -1;

    return state;
  }

}