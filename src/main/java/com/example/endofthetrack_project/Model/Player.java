package com.example.endofthetrack_project.Model;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Player class that represents the user player
 * @author maor
 */
public class Player {

    /**
     * attributes of class
     */
    private int id;
    private String color;
    private Cell[] pieces;
    private Cell ball;
    private String notValid;
    private boolean isCurrent;

    /**
     * construct new player
     * @param id : the id of the player
     * @param color : the color of the player (white or black)
     */
    public Player (int id, String color) {
        this.id = id;
        this.pieces = new Cell[5];
        this.color = color;
    }

    public Cell[] getPieces() {
        return pieces;
    }

    public int getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public Cell getBall() {
        return ball;
    }

    public void setBall(Cell ball) {
        this.ball = ball;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    /**
     * makes a move for a player based on what he played on the board
     * <p>
     *     The makeMove() method has a time complexity of O(n),
     *     where n is the number of pieces in the pieces array,
     *     as it checks if the sourceCell is a valid piece,
     *     moves the piece to the destCell,
     *     updates the ball attribute if necessary,
     *     and updates the pieces array. The method also has a space complexity of O(1),
     *     as it only modifies the attributes of the class and does not create any new data structures.
     * </p>
     * @param board : game board
     * @param sourceCell : the source cell
     * @param destCell : the destination cell
     * @return : If the move is done successfully
     */
    public boolean makeMove (Cell[][] board, Cell sourceCell, Cell destCell) {
        if (Arrays.asList(this.pieces).contains(sourceCell)) {
            if (sourceCell.movePiece(board, destCell.getX(), destCell.getY())) {
                Iterator<Piece> iterator = destCell.getPiece().iterator();
                Piece p = iterator.next();
                if (iterator.hasNext()) {
                    this.setBall(destCell);
                }
                else {
                    if (p.getColor().equals("white")) {
                        this.pieces[p.getId() - 1] = destCell;
                    }
                    if (p.getColor().equals("black")) {
                        this.pieces[p.getId() - 7] = destCell;
                    }
                }
                return true;
            }
            else {
                this.notValid = "Illegal move try again";
                System.out.println("Illegal move try again");
            }
        }
        return false;
    }

    public String getNotValid() {
        return notValid;
    }

    /**
     * check for win
     * <p>
     *     The isWinner() method has a time complexity of O(1),
     *     as it simply checks the value of the ball attribute
     *     and the color of the player to determine if there is a win.
     *     The method also has a space complexity of O(1),
     *     as it does not create any new data structures.
     * </p>
     * @return : if there is a win
     */
    public boolean isWinner () {
        if (this.ball.getPiece().size() > 1) {
            Iterator<Piece> iterator = this.ball.getPiece().iterator();
            iterator.next();
            Piece p = iterator.next();
            if (p.getColor().equals("white") && this.color.equals("white")) {
                return this.ball.getY() == 0;
            } else if (p.getColor().equals("black") && this.color.equals("black")) {
                return this.ball.getY() == 7;
            }
        }
        return false;

    }
}
