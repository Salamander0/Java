/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.players;

import java.util.Random;
import thegame.info.InfoDetail;
import thegame.info.Info;
import thegame.model.Command;
import thegame.model.CommandType;

/**
 *
 * @author Richard
 */
public class SCH0177 implements IPlayer {
    
   
    
     //int nepritel_X [];
     private static int nepritel_X [] = new int[400];
     private  static  int nepritel_Y [] = new int[400];
     private static int granade_X [] = new int[4000];
     private  static  int granade_Y [] = new int[4000];
     private  static  int granade_D [] = new int[4000];
     private static int granade_O [] = new int[4000];
     private static int granade_last_X [] = new int [4000];
     private static int granade_last_Y [] = new int [4000];
     private static int nepritel_last_X [] = new int [400];
     private static int nepritel_last_Y [] = new int [400];
     private static int smer_X [] = new int [400];
     private static int smer_Y [] = new int [400];
     private static int smer_g_X [] = new int [400];
     private static int smer_g_Y [] = new int [400];
     private int vzdalenost[] = new int[400];
     private int vzdalenost_g[] = new int[4000];
     
     private boolean start=true, utek=false, ohrozeni=false;
     private static int reloade=2, last, pocet_g_last;
     
    
    
    @Override
    public Command planNextMove(Info info) {
        Random r = new Random();
        
        reloade--;
        if (reloade<0) reloade=0;
        
       
        
        int moje_X = info.getX();
        int moje_Y = info.getY();
        int muj_smer = info.getDirection();
        int nejbliz=-1;
        int min=10000;
        int nejbliz_g;
        int pocet=0, pocet_g=0, pocet_ohrozeni=0;
        int uhel_strelby, uhel_kolize=0,uhel_natoceni=0;
        int ubytek_g=0;
        int smer;
       // int smer_X[], smer_Y[];
        
          
        
     //   if (reloade==5) utek=false;
        
        
         for(InfoDetail o : info.getEnemies()) {
             nepritel_X[pocet] = o.getX();
             nepritel_Y[pocet] = o.getY();
        
             pocet++;
             
         }
        // if (pocet==0) reloade=10;
         
         for(InfoDetail o : info.getGrenades()){
             granade_X[pocet_g] = o.getX();
             granade_Y[pocet_g] = o.getY();
             granade_D[pocet_g] = o.getDirection();
          
             pocet_g++;
         } 
         
         
         if(pocet_g<pocet_g_last) ubytek_g=pocet_g_last-pocet_g;
         else ubytek_g=0;
         
         
         
        

        
        /* Test ohrozeni */ 
         ohrozeni=false;
         for (int i = 0; i<pocet_g; i++ ){
             int x,y,j=0,rozdil;
             vzdalenost_g[i]=(int)Math.sqrt((int)Math.pow(Math.abs(moje_X- granade_X[i]), 2) + (int) Math.pow(Math.abs(moje_Y-granade_Y[i]), 2));
             
              x = moje_X - granade_X[i]  ;
              y = moje_Y  - granade_Y[i]  ;
              uhel_kolize = (int) Math.toDegrees(Math.atan2(y,x));
              if (uhel_kolize<0) uhel_kolize+=360;
            //  System.out.println(granade_D[i] + " ++ "+ uhel_kolize +" ++ "+ vzdalenost_g[i]);
             
              rozdil=(uhel_kolize-granade_D[i]);
              if(vzdalenost_g[i]<40) rozdil/=5;
              if (vzdalenost_g[i]<400 && ((rozdil<11 && rozdil>-11 ) || (rozdil<371 && rozdil>349))) {
               //   ohrozeni=true;
                  granade_O[j]=granade_D[i];
                  j++;
                //  System.out.println("ohrozeni");
                                }
              pocet_ohrozeni=j;
         }
         
         
         for (int i = 0; i<pocet; i++ ){
             vzdalenost[i]= (int)Math.sqrt((int)Math.pow(Math.abs(moje_X-nepritel_X[i]), 2) + (int) Math.pow(Math.abs(moje_Y-nepritel_Y[i]), 2));
                 
    }
               
         nejbliz_g = minimum_g(vzdalenost_g,pocet_g);
         
         
         if (!start) {
             for (int i = 0; i<pocet; i++ ){
             smer_X[i] = nepritel_last_X[i]-nepritel_X[i];
             smer_Y[i] = nepritel_last_Y[i]-nepritel_Y[i];
            
                   
             smer_X[i]=(vzdalenost[i]/20)*smer_X[i];
             smer_Y[i]=(vzdalenost[i]/20)*smer_Y[i];
                }
             
             for (int i = 0; i<pocet_g; i++ ){
              smer_g_X[i] = granade_last_X[i+ubytek_g] - granade_X[i];
              smer_g_Y[i] = granade_last_Y[i+ubytek_g] - granade_Y[i];
              
              smer_g_X[i] = (vzdalenost_g[i]/20)*smer_g_X[i];
              smer_g_Y[i] = (vzdalenost_g[i]/20)*smer_g_Y[i];
              
              smer_g_X[i] = granade_X[i] - smer_g_X[i];
              smer_g_Y[i] = granade_Y[i] - smer_g_Y[i];
              
             
              
              if ( (smer_g_X[i] >=moje_X-28 && smer_g_X[i] <=moje_X+28) && (smer_g_Y[i]>=moje_Y-28 && smer_g_Y[i]<=moje_Y+28) && vzdalenost_g[i]<500  ){
                    ohrozeni=true;
                    if (nejbliz_g==i) { 
                        uhel_kolize=granade_D[i];
                       
                    
                //    System.out.println("**ohrozeni**");

                    }
              }
              }
             
         }
         
        
         nejbliz = minimum(vzdalenost,pocet /*,last*/);
         
        // System.out.println("++"+nejbliz);
         if (pocet>1 && vzdalenost[nejbliz]<500) last=nejbliz; 
         else last=-1;
         
          int x,y;
          x = nepritel_X [nejbliz] - smer_X[nejbliz] - moje_X;
          y = nepritel_Y [nejbliz] - smer_Y[nejbliz] - moje_Y;
          uhel_strelby = (360-muj_smer) + (int) Math.toDegrees(Math.atan2(y,x));
          
          
          start=false;
          kopirovani( pocet, pocet_g );
          
          
       /*   if (pocet==0) {
              uhel_natoceni=(360-muj_smer) + uhel_kolize +90;
              return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, uhel_natoceni, CommandType.NONE);
          }*/
          if (pocet==0 && ohrozeni==false  ) return new Command(CommandType.MOVE_BACK, CommandType.TURN_RIGHT, 10, CommandType.NONE);

          if(ohrozeni==false && reloade==0 && vzdalenost[nejbliz]<3500){
              reloade=10;
            //  System.out.println("neohrozen");
              return new Command(CommandType.NONE, CommandType.TURN_RIGHT, uhel_strelby, CommandType.SHOOT);
          }
          else if(ohrozeni==false && reloade>=0 && vzdalenost[nejbliz]>240) {
          //    System.out.println("neohrozen");
              return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, uhel_strelby+(r.nextInt(30)-15), CommandType.NONE);
          }
          else if(ohrozeni==false && reloade>=0 && vzdalenost[nejbliz]<=240) {
          //    System.out.println("neohrozen");
              return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, uhel_strelby+130, CommandType.NONE);
          }
          else   {
              uhel_natoceni=(360-muj_smer) + uhel_kolize +90;
              return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, uhel_natoceni, CommandType.NONE);
          }
    
         
    }

  private static int minimum (int[] list, int pocet  /*, int last*/) {
  int min = 0;
  for (int p = 0; p < pocet; p++){
 //  if (p!=last)   
    if (list[p] < list[min] ) {
         min = p;
    }
  }
  return min;
  }
  
  private static int minimum_g (int[] list, int pocet) {
  int min = 0;
  for (int p = 0; p < pocet; p++){   
    if (list[p] < list[min]) {
         min = p;
    }
  }
  return min;
  }
  
  private static void kopirovani(int pocet, int pocet_g){
      for(int i=0; i<pocet; i++){
      nepritel_last_X[i] = nepritel_X[i];
      nepritel_last_Y[i] = nepritel_Y[i];
      }
      for(int i=0; i<pocet_g; i++){
      granade_last_X[i] = granade_X[i];
      granade_last_Y[i] = granade_Y[i];
      }
      pocet_g_last = pocet_g;
      
  }
  


    @Override
    public String getName() {
        return "SCH0177☺";
    }
    
}
