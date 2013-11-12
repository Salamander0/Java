/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.control;

import java.awt.Point;
import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import thegame.info.Info;
import thegame.info.InfoDetail;
import thegame.model.Command;
import thegame.model.CommandType;
import thegame.model.Explosion;
import thegame.model.GameData;
import thegame.model.Grenade;
import thegame.model.ModelObject;
import thegame.model.ModelPlayer;
import thegame.model.Tank;
import thegame.players.HopelessPlayer;
import thegame.players.StupidPlayer;
//import thegame.players.Sypula;
import thegame.view.TheGameView;

/**
 *
 * @author beh01
 */
public class Controller {

    private List<ModelObject> objects;
    private List<ModelPlayer> players;
    private TheGameView view;
    private Thread thread;
    private boolean running = false;

    public Controller() {
        objects = new ArrayList<ModelObject>();
        players = new ArrayList<ModelPlayer>();
    }

    public void setView(TheGameView view) {
        this.view = view;
    }

    private void initializeGame() {
        players.clear();
        players.add(new ModelPlayer(new HopelessPlayer()));
        players.add(new ModelPlayer(new HopelessPlayer()));
        players.add(new ModelPlayer(new StupidPlayer()));
        players.add(new ModelPlayer(new StupidPlayer()));
 //       players.add(new ModelPlayer(new Sypula()));
 //       players.add(new ModelPlayer(new Sypula()));
        objects.clear();
        Random r = new Random();
        for(ModelPlayer p : players) {
            objects.add(new Tank(p, r.nextInt(GameData.GAME_WIDTH),
                    r.nextInt(GameData.GAME_HEIGTH), r.nextInt(360)));
        }
    }

    public void start() {
        if (running) {
            stop();
        }
        initializeGame();
        running = true;
        view.updateModel(objects, players);
        thread = new Thread() {
            @Override
            public void run() {
                while (running) {
                    synchronized (objects) {
                        ArrayList<ModelObject> toRemove = new ArrayList<ModelObject>();
                        ArrayList<ModelObject> newObjects = new ArrayList<ModelObject>();
                        //remove old explosions
                        for (ModelObject o : objects) {
                            if (o instanceof Explosion) {
                                if (((Explosion) o).notVisible()) {
                                    toRemove.add(o);
                                }
                            }
                        }
                        objects.removeAll(toRemove);
                        toRemove.clear();
                        //remove objects that leave the game area
                        for (ModelObject o : objects) {
                            if (o.getX() < 5 || o.getX() > GameData.GAME_HEIGTH - 5
                                    || o.getY() < 5 || o.getY() > GameData.GAME_HEIGTH - 5) {
                                toRemove.add(o);
                            }
                        }
                        objects.removeAll(toRemove);
                        toRemove.clear();
                        //remove hit tanks and add explosions
                        for (ModelObject tank : objects) {
                            if (tank instanceof Tank) {
                                for (ModelObject test : objects) {
                                    if (test instanceof Grenade) {
                                        double distance = new Point(tank.getX(), tank.getY()).distance(new Point(test.getX(), test.getY()));

                                        if (distance < GameData.TANK_SIZE / 2 + 1) {
                                            ((Grenade) test).getTank().getPlayer().addKill();
                                            toRemove.add(test);
                                            toRemove.add(tank);
                                            newObjects.add(new Explosion(tank.getX(), tank.getY()));
                                        }
                                    }
                                }
                            }
                        }
                        objects.removeAll(toRemove);
                        //move with tanks and grenades
                        for (ModelObject o : objects) {
                            if (o instanceof Tank) {
                                Tank tank = (Tank) o;
                                //prepare info
                                Info info = new Info(tank.getX(), tank.getY(), tank.getDirection(), tank.canShoot());
                                for (ModelObject item : objects) {
                                    if (o != item) {
                                        if (item instanceof Tank) {
                                            info.getEnemies().add(new InfoDetail(item.getX(), item.getY(), item.getDirection()));
                                        }
                                        if (item instanceof Grenade) {
                                            info.getGrenades().add(new InfoDetail(item.getX(), item.getY(), item.getDirection()));
                                        }
                                    }
                                }
                                Grenade g=null;
                                try {
                                    Command command = tank.getPlayer().getRealPlayer().planNextMove(info);
                                    g = tank.doCommand(command);
                                }catch(Exception e) {
                                    tank.getPlayer().decrementScore();
                                }
                                tank.getPlayer().incrementScore();
                                if (g != null) {
                                    newObjects.add(g);
                                }
                            }
                            if (o instanceof Grenade) {
                                Grenade g = (Grenade) o;
                                g.move();
                            }
                        }
                        objects.addAll(newObjects);
                    }

                    view.repaint();
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        };
        thread.start();
    }

    public void stop() {
        running = false;

        try {
            thread.join();
        } catch (InterruptedException ex) {
        }
        objects.clear();
        view.repaint();
    }
}
