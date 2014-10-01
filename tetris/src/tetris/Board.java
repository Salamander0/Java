/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

/**
 *
 * @author beh01
 */
public class Board {

    boolean data[][];

    public Board() {
        data = new boolean[20][10];
    }
    public boolean getCell(int row, int column) {
        return data[row][column];
    }
    public void setCell(int row, int column, boolean value) {
        data[row][column]=value;
    }
    public int getNumberOfRows() {
        return data.length;
    }
    public int getNumberOfColumns() {
        return data[0].length;
    }

}
