/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.model;

import thegame.players.IPlayer;

/**
 *
 * @author beh01
 */
public class ModelPlayer implements Comparable<ModelPlayer>{
    private IPlayer realPlayer;
    private int kills, shoots;
    private double score;

    public ModelPlayer(IPlayer realPlayer) {
        this.realPlayer = realPlayer;
    }

    public IPlayer getRealPlayer() {
        return realPlayer;
    }

    public int getScore() {
        return (int)score;
    }
    public void addKill() {
        kills++;
    }
    public void addShoot() {
        shoots++;
    }

    public int getKills() {
        return kills;
    }

    public int getShoots() {
        return shoots;
    }

    public void incrementScore() {
        if (kills==0) return;
        if (shoots==0) return;
        score+=(kills/((double)shoots))*kills;
    }
    public void decrementScore() {
        score-=100;
    }
    @Override
    public int compareTo(ModelPlayer o) {
        if (getScore()<o.getScore()) return +1;
        else return -1;
    }
}
