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
import thegame.model.ModelObject;
import thegame.model.GameData;
/**
 *
 * @author Jirka
 */
public class JiriPlayer implements IPlayer{
    List<InfoDetail> enemies = new ArrayList<InfoDetail>();
    List<Boolean> isMoving = new ArrayList<Boolean>();
    boolean moved = false;
    
    @Override
    public Command planNextMove(Info info) {
        double Angle, resultAngle;
        Angle = 0;
        resultAngle = Angle;
        int my_x = info.getX();
        int my_y = info.getY();
        int enemy_x = 0;
        int enemy_y = 0;
        int gren_x = 0;
        int gren_y = 0;
        int my_direction = info.getDirection();
        int enemy_direction = 0;        
        int gren_direction = 0;
        
        List<InfoDetail> enemies = new ArrayList<InfoDetail>();
        List<InfoDetail> grenades = new ArrayList<InfoDetail>();
        
        enemies = info.getEnemies();  
        grenades = info.getGrenades();
        
        if (this.enemies.isEmpty()) {       //priradi do pole tolik polozek, kolik je tanku ve hre
            for (InfoDetail tank : enemies) {
                this.isMoving.add(Boolean.FALSE);
            }
        }
/*        
        InfoDetail closestGrenade = null;
        if(!grenades.isEmpty()) {
            int nejblizsi = 50000;
            int sizex = 0;
            int sizey = 0;
            for (InfoDetail grenade : grenades) {       //vybere se nejblizsi tank
                //nejdrive vybrat granaty, ktere na me miri
                //potom z tech vybrat ten nejblizsi
                
                gren_x = grenade.getX();
                gren_y = grenade.getY();
                gren_direction = grenade.getDirection();
                
                sizex = Math.abs(gren_x - my_x);
                sizey = Math.abs(gren_y - my_y);
                
                Angle = Math.atan(sizex/sizey) * 180 / Math.PI;
                
                if (gren_direction <= Angle + 5 && gren_direction >= Angle - 5 && this.moved == false) {
                    this.moved = true;
                    return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, 10, CommandType.SHOOT);
                }
                
                else if (gren_direction <= Angle + 5 && gren_direction >= Angle - 5 && this.moved == true) {
                    return new Command(CommandType.MOVE_FORWARD, CommandType.NONE, 0, CommandType.SHOOT);
                }
                
                if(new Point(grenade.getX(), grenade.getY()).distance(new Point(my_x, my_y)) < nejblizsi) {
                    nejblizsi = (int) (new Point(grenade.getX(), grenade.getY()).distance(new Point(my_x, my_y)));
                
                    closestGrenade = grenade;
                }
            }
                    
            this.moved = false;       
            int range = 2 * GameData.TANK_SIZE;
            
            for (InfoDetail grenade : grenades) {
                gren_x = grenade.getX();
                gren_y = grenade.getY();
                gren_direction = grenade.getDirection();
            }
                    //zjistit, jestli se nejaky granat nenachazi v okoli
        }
        * 
        */
        
        if(!enemies.isEmpty()) {                //overeni, jestli se dany tank hybe, potom se mu priradi hodnota TRUE
            int nejblizsi = 50000;
            int i = 0;
            if (!this.enemies.isEmpty()) {
                for (InfoDetail tank : enemies) {
                                
                    if (!(tank.getX() == this.enemies.get(i).getX()) ||
                        !(tank.getY() == this.enemies.get(i).getY())){
                        this.isMoving.set(i, Boolean.TRUE);
                        
                    }
                    else this.isMoving.set(i, Boolean.FALSE);
                    i++;
                }
            }
                
            this.enemies = enemies;
                        
            double sizex, sizey;
            double diffx, diffy;
            double distance;        //vzdalenost mezi mnou a nepritelem
            int difference = 0;         //rozdil mezi tim, kde je ted a kam dojede
            
            boolean movingTank = false; //pracuji s pohybujicim se tankem
            i = 0; 
            InfoDetail closestEnemy = null;
            for (InfoDetail tank : enemies) {       //vybere se nejblizsi tank
                if(new Point(tank.getX(), tank.getY()).distance(new Point(my_x, my_y)) < nejblizsi) {
                    nejblizsi = (int) (new Point(tank.getX(), tank.getY()).distance(new Point(my_x, my_y)));
                
                    closestEnemy = tank;   
                    if (this.isMoving.get(i) == true) {
                        movingTank = true;
                    }
                    else {
                        movingTank = false;
                    }
                }
                i++;
            }
                                
            enemy_x = closestEnemy.getX();
            enemy_y = closestEnemy.getY();
            enemy_direction = closestEnemy.getDirection();
            
            sizex = Math.abs(enemy_x - my_x);
            sizey = Math.abs(enemy_y - my_y); 
            
            if (movingTank) {
                distance = Math.sqrt(sizex*sizex + sizey*sizey);

                if (distance >= 0 && distance < 50) {
                    difference = 10;
                }

                else if (distance >= 50 && distance < 100) {
                    difference = 20;
                }

                else if (distance >= 100 && distance < 150) {
                    difference = 30;
                }

                else if (distance >= 150 && distance < 200) {
                    difference = 40;
                }

                else if (distance >= 200 && distance < 250) {
                    difference = 50;
                }

                else if (distance >= 250 && distance < 300) {
                    difference = 60;
                }

                else if (distance >= 300 && distance < 350) {
                    difference = 70;
                }

                else if (distance >= 350 && distance < 400) {
                    difference = 80;
                }

                else if (distance >= 400 && distance < 450) {
                    difference = 90;
                }
                
                else if (distance >= 450 && distance < 500) {
                    difference = 100;
                }
                
                else if (distance >= 500 && distance < 550) {
                    difference = 110;
                }
                
                else if (distance >= 550 && distance < 600) {
                    difference = 120;
                }
                
                else if (distance >= 600 && distance < 650) {
                    difference = 130;
                }
                
                else if (distance >= 650 && distance < 700) {
                    difference = 140;
                }
                
                else if (distance >= 700 && distance < 750) {
                    difference = 150;
                }
                
                else if (distance >= 750 && distance < 800) {
                    difference = 160;
                }
                
                else if (distance >= 800) {
                    difference = 170;
                }

                diffx = Math.abs(Math.cos(enemy_direction * Math.PI / 180)) * difference;
                diffy = Math.abs(Math.sin(enemy_direction * Math.PI / 180)) * difference;
                if (enemy_direction >= 0 && enemy_direction < 90) {
                    enemy_x += diffx;
                    enemy_y += diffy;
                }
                else if (enemy_direction >= 90 && enemy_direction < 180) {
                    enemy_x -= diffx;
                    enemy_y += diffy;
                }
                else if (enemy_direction >= 180 && enemy_direction < 270) {
                    enemy_x -= diffx;
                    enemy_y -= diffy;
                }
                else {
                    enemy_x += diffx;
                    enemy_y -= diffy;
                }

                sizex = Math.abs(enemy_x - my_x);
                sizey = Math.abs(enemy_y - my_y);           
            }
            
            if (sizey == 0) {
                Angle = 90;
            }
            else if (sizex == 0) {
                Angle = 0;
            }
            else if ((enemy_y - my_y>0 && enemy_x - my_x>0) ||
                    (enemy_y - my_y<0 && enemy_x - my_x<0)){
                Angle = Math.atan(sizey/sizex) * 180 / Math.PI;
            }
            else {
                Angle = Math.atan(sizex/sizey) * 180 / Math.PI;
            }

            //nepritel je v I. kvadrantu
            if (enemy_y - my_y>=0 && enemy_x - my_x>0) {
                resultAngle = Angle + 0;
            }
            //nepritel je ve II. kvadrantu
            else if (enemy_y - my_y>0 && enemy_x - my_x<=0) {
                resultAngle = Angle + 90;
            }
            //nepritel je ve III. kvadrantu
            else if (enemy_y - my_y<=0 && enemy_x - my_x<0) {
                resultAngle = Angle + 180;                    
            }
            //nepritel je ve IV. kvadrantu
            else if (enemy_y - my_y<0 && enemy_x - my_x>=0) {
                resultAngle = Angle + 270;                    
            }

            if (my_direction - resultAngle >=0) {
                resultAngle = my_direction - resultAngle;
                return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_LEFT, (int)resultAngle, CommandType.SHOOT);
            }
            else {
                resultAngle = resultAngle - my_direction;
                return new Command(CommandType.MOVE_FORWARD, CommandType.TURN_RIGHT, (int)resultAngle, CommandType.SHOOT);
            }
        }
        
        if (!grenades.isEmpty()) {      //granaty se posleze budou overovat drive
            int nejblizsi = 50000;
            
            double sizex, sizey;
                    //pokud je granat nejblizsi a zaroven miri na me
        }
        

        if (info.getDirection() != Math.round(resultAngle)) {
            return new Command(CommandType.NONE, CommandType.TURN_RIGHT, (int)resultAngle, CommandType.SHOOT);
        }

        return new Command(CommandType.NONE, CommandType.TURN_RIGHT, 90, CommandType.SHOOT);
        
    }

    @Override
    public String getName() {
        return "Frimlik";
    }
    
}
