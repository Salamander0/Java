/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame;

import thegame.control.Controller;
import thegame.view.TheGameView;

/**
 *
 * @author beh01
 */
public class Thegame {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        TheGameView  view = new TheGameView(controller);
        view.setVisible(true);

    }
}
