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
import thegame.players.Ardente;
import thegame.players.BRE0084;
import thegame.players.Bagzila;
import thegame.players.Dificult;
import thegame.players.HopelessPlayer;
import thegame.players.StupidPlayer;
import thegame.players.Hanz;
import thegame.players.HonzikPlayer;
import thegame.players.JiriPlayer;
import thegame.players.Jurasek;
import thegame.players.KneysPlayer;
import thegame.players.Kol0232;
import thegame.players.MartinPlayer;
import thegame.players.MotakPlayer;
import thegame.players.MyAI;
import thegame.players.MyGamePlayer;
import thegame.players.MyPlayer;
import thegame.players.PJO0001;
import thegame.players.Pavel;
import thegame.players.SCH0177;
import thegame.players.SIR0029;
import thegame.players.SWA0010;
import thegame.players.Strigo33;
import thegame.players.Sypula;
import thegame.players.TomPlayer;
import thegame.players.VNE0005;
import thegame.players.VYB0028;
import thegame.players.ZAK0049;
import thegame.players.ZDZ0005;
import thegame.players.ZUR;
import thegame.players.boc0040;
import thegame.players.cec0080;
import thegame.players.pop0041;
import thegame.players.sme0061;
import thegame.players.Salamander;
import thegame.players.GUN0018;
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
        players.add(new ModelPlayer(new Salamander()));
        players.add(new ModelPlayer(new GUN0018()));
//        players.add(new ModelPlayer(new Hanz()));
//        players.add(new ModelPlayer(new BRE0084()));
//        players.add(new ModelPlayer(new pop0041()));
//        players.add(new ModelPlayer(new VNE0005()));
//        players.add(new ModelPlayer(new ZDZ0005()));
//        players.add(new ModelPlayer(new ZAK0049()));
//        players.add(new ModelPlayer(new Sypula()));
//        players.add(new ModelPlayer(new SIR0029()));
//        players.add(new ModelPlayer(new PJO0001()));
        players.add(new ModelPlayer(new Dificult()));
//        players.add(new ModelPlayer(new Pavel()));
//        players.add(new ModelPlayer(new VYB0028()));
//        players.add(new ModelPlayer(new MyPlayer()));
//        players.add(new ModelPlayer(new MotakPlayer()));
//        players.add(new ModelPlayer(new MyGamePlayer()));
//        players.add(new ModelPlayer(new Bagzila()));
//        players.add(new ModelPlayer(new KneysPlayer()));
//        players.add(new ModelPlayer(new sme0061()));
//        players.add(new ModelPlayer(new boc0040()));
//        players.add(new ModelPlayer(new Strigo33()));
//        players.add(new ModelPlayer(new Ardente()));
//        players.add(new ModelPlayer(new MartinPlayer()));
//        players.add(new ModelPlayer(new MyAI()));
//        players.add(new ModelPlayer(new HonzikPlayer()));
//        players.add(new ModelPlayer(new JiriPlayer()));
//        players.add(new ModelPlayer(new TomPlayer()));
//        players.add(new ModelPlayer(new Jurasek()));
//        players.add(new ModelPlayer(new SWA0010()));
//        players.add(new ModelPlayer(new Kol0232()));
//        players.add(new ModelPlayer(new SCH0177()));
//        players.add(new ModelPlayer(new ZUR()));
//        players.add(new ModelPlayer(new cec0080()));
        objects.clear();
        Random r = new Random();
        for (ModelPlayer p : players) {
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
                            if (o instanceof Grenade) {
                                if (((Grenade) o).notVisible()) {
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
                                Grenade g = null;
                                try {
                                    Command command = tank.getPlayer().getRealPlayer().planNextMove(info);
                                    g = tank.doCommand(command);
                                } catch (Exception e) {
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
