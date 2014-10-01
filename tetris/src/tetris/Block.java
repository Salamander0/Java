/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

/**
 *
 * @author beh01
 */
public class Block {
    private static int data[][][][]=new int[][][][]
    {
        //Line
        {{{0,-1},{0,0},{0,1},{0,2}},
         {{-1,0},{0,0},{1,0},{2,0}},
         {{0,-1},{0,0},{0,1},{0,2}},
         {{-1,0},{0,0},{1,0},{2,0}}},
        //LeftL
        {{{-2,0},{-1,0},{0,0},{0,1}},
         {{1,0},{0,0},{0,1},{0,2}},
         {{-1,0},{0,0},{1,0},{2,0}},
         {{0,-2},{0,-1},{0,0},{-1,0}}}
    };

    private Board board;
    private BlockType type;
    private int row, column, rotation;

    public Block(Board board, BlockType type, int row, int column) {
        this.board=board;
        this.type=type;
        this.row =row;
        this.column=column;
    }

    public int[][] getCellsCoordinates() {
        return getCellsCoordinates(row, column, rotation);
    }
    private int[][] getCellsCoordinates(int row, int column,int rotation) {
        int result[][] = new int[4][2];
        for(int i=0;i<4;i++) {
            result[i][0] = row + data[type.ordinal()][rotation][i][0];
            result[i][1] = column + data[type.ordinal()][rotation][i][1];
        }
        return result;
    }
    private boolean check(int[][] coordinates) {
        for(int i=0;i<4;i++) {
            if (coordinates[i][0]<0 || coordinates[i][0]>=board.getNumberOfRows()) return false;
            if (coordinates[i][1]<0 || coordinates[i][1]>=board.getNumberOfColumns()) return false;
            if (board.getCell(coordinates[i][0],coordinates[i][1])) return false;
        }
        return true;
    }
    public boolean moveLeft() {
        if (check(getCellsCoordinates(row, column-1,rotation))) {
            column--;
            return true;
        }
        return false;
    }
    public boolean moveRight() {
        if (check(getCellsCoordinates(row, column+1,rotation))) {
            column++;
            return true;
        }
        return false;
    }
    public boolean moveDown() {
       if (check(getCellsCoordinates(row+1, column, rotation))) {
            row++;
            return true;
        }
        return false;
    }
    public boolean rotate() {
        if (check(getCellsCoordinates(row, column, (rotation+1)%4))) {
            rotation=(rotation+1)%4;
            return true;
        }
        return false;
    }
}
