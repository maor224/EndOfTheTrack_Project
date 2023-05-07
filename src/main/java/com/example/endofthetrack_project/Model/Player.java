package com.example.endofthetrack_project.Model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

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

    /**
     * constructs a new player from an existing player
     *
     * @param currPlayer the existing player to be copied
     */
    public Player(Player currPlayer) {
        this.id = currPlayer.id;
        this.color = currPlayer.color;
        this.ball = new Cell(currPlayer.ball);
        this.notValid = currPlayer.notValid;
        this.isCurrent = currPlayer.isCurrent;

        // copy the pieces array
        this.pieces = new Cell[currPlayer.pieces.length];
        for (int i = 0; i < currPlayer.pieces.length; i++) {
            this.pieces[i] = new Cell(currPlayer.pieces[i]);
        }
    }


    /**
     * Returns the pieces of the player
     * @return the pieces array
     */
    public Cell[] getPieces() {
        return pieces;
    }

    /**
     * Returns the id of the player
     * @return the player's id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the color of the player
     * @return the player's color
     */
    public String getColor() {
        return color;
    }

    /**
     * Returns the ball of the player
     * @return the player's ball
     */
    public Cell getBall() {
        return ball;
    }

    /**
     * Sets the ball of the player
     * @param ball the new ball
     */
    public void setBall(Cell ball) {
        this.ball = ball;
    }

    /**
     * Sets the pieces of the player
     * @param pieces the new pieces array
     */
    public void setPieces(Cell[] pieces) {
        this.pieces = pieces;
    }

    /**
     * Checks if it is currently the player's turn
     * @return true if it's the player's turn, false otherwise
     */
    public boolean isCurrent() {
        return isCurrent;
    }


    /**
     * Sets the player's turn status
     * @param current true if it's the player's turn, false otherwise
     */
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
        // checks if the source cell contains a piece owned by the player
        if (Arrays.asList(this.pieces).contains(sourceCell)) {
            // calls movePiece() on the source cell and checks if the move is valid
            if (sourceCell.movePiece(board, destCell.getX(), destCell.getY())) {
                Iterator<Piece> iterator = board[destCell.getY()][destCell.getX()].getPiece().iterator();
                Piece p = iterator.next();
                // checks if there is more than one piece on the destination cell
                if (iterator.hasNext()) {
                    // checks if the source cell originally had two pieces
                    if (sourceCell.getPiece().size() == 2) {
                        if (p.getColor().equals("white"))
                            // removes the last piece in the white player's captured pieces list.
                            this.pieces[sourceCell.getPiece().iterator().next().getId() - 1].getPiece().remove(this.pieces[sourceCell.getPiece().iterator().next().getId() - 1].getPiece().size() - 1);
                        if (p.getColor().equals("black"))
                            // removes the last piece in the black player's captured pieces list.
                            this.pieces[sourceCell.getPiece().iterator().next().getId() - 7].getPiece().remove(this.pieces[sourceCell.getPiece().iterator().next().getId() - 7].getPiece().size() - 1);
                    }
                    if (sourceCell.getPiece().iterator().next().getColor().equals("white")) {
                        // checks if the white player has two pieces in their captured pieces list.
                        if (this.pieces[sourceCell.getPiece().iterator().next().getId() - 1].getPiece().size() == 2) {
                            // removes the last piece in the white player's captured pieces list.
                            this.pieces[sourceCell.getPiece().iterator().next().getId() - 1].getPiece().remove(this.pieces[sourceCell.getPiece().iterator().next().getId() - 1].getPiece().size() - 1);
                        }
                    }
                    if (sourceCell.getPiece().iterator().next().getColor().equals("black")) {
                        // checks if the black player has two pieces in their captured pieces list.
                        if (this.pieces[sourceCell.getPiece().iterator().next().getId() - 7].getPiece().size() == 2) {
                            // removes the last piece in the black player's captured pieces list.
                            this.pieces[sourceCell.getPiece().iterator().next().getId() - 7].getPiece().remove(this.pieces[sourceCell.getPiece().iterator().next().getId() - 7].getPiece().size() - 1);
                        }
                    }
                    // sets the ball if it is on the destination cell
                    this.setBall(board[destCell.getY()][destCell.getX()]);
                }
                // updates the player's piece array if the piece moved is white
                if (p.getColor().equals("white")) {
                    this.pieces[p.getId() - 1] = board[destCell.getY()][destCell.getX()];
                }
                // updates the player's piece array if the piece moved is black
                if (p.getColor().equals("black")) {
                    this.pieces[p.getId() - 7] = board[destCell.getY()][destCell.getX()];
                }
                return true;
            }
            else {
                // sets a message if the move is not valid
                this.notValid = "Illegal move try again";
            }
        }
        // returns false if the source cell does not contain a piece owned by the player
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        boolean flag = true;
        for (int i = 0;i < player.getPieces().length;i++) {
            if (!player.getPieces()[i].equals(getPieces()[i])) {
                flag = false;
            }
        }
        return getId() == player.getId() &&
                isCurrent() == player.isCurrent() &&
                getColor().equals(player.getColor()) && flag
                && getBall().equals(player.getBall());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getColor(), getBall(), isCurrent());
        result = 31 * result + Arrays.hashCode(getPieces());
        return result;
    }
}
