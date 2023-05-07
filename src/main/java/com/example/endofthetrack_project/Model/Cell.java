package com.example.endofthetrack_project.Model;

import java.util.*;

/**
 * Cell class represent a cell on the board
 * @author maor
 */

public class Cell {

    /**
     * define Cell attributes that represent the Cell position on the board
     * and the piece that the cell is holding
     */
    private int x;
    private int y;

    private List<Piece> piece;

    /**
     * create new cell
     * @param x: cell column
     * @param y: cell row
     */
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.piece = new LinkedList<>();
    }

    /**
     * Creates a new Cell object as a copy of the specified Cell object.
     *
     * @param cell the Cell object to copy
     */
    public Cell(Cell cell) {
        this.x = cell.x;
        this.y = cell.y;
        this.piece = new LinkedList<>();
        for (Piece p : cell.piece) {
            if (p instanceof Knight)
                this.piece.add(new Knight((Knight) p));
            if (p instanceof Ball)
                this.piece.add(new Ball((Ball) p));
        }
    }

    /**
     * get the cell column
     * @return cell column
     */
    public int getX() {
        return x;
    }

    /**
     * get the cell row
     * @return cell row
     */
    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * get the cell piece
     * @return cell piece
     */
    public List<Piece> getPiece() {
        return piece;
    }

    /**
     * set the cell piece
     * <p>
     * This function sets the piece in the cell.
     * If the piece is a Knight,
     * it clears the list of pieces and adds the new Knight piece.
     * Otherwise, it simply adds the new piece to the list.
     * The runtime efficiency of this function depends on the size of the list.
     * If the set is empty or contains only a Knight,
     * then the runtime efficiency is O(1).
     * If the set contains other pieces,
     * then the runtime efficiency is O(n),
     * where n is the size of the set.
     * </p>
     * @param piece: the current piece in the cell
     */
    public void setPiece(Piece piece) {
        if (piece.getClass().equals(Knight.class)) {
            this.piece.clear();
            this.piece.add(piece);
        }
        else {
            this.piece.add(piece);
        }
    }

    /**
     * check if the cell is empty
     * <p>
     *     This function checks if the cell is empty.
     *     It gets the first piece in the piece list
     *     and checks if its ID is 0 (indicating an empty cell).
     *     The runtime efficiency is O(1).
     * </p>
     *
     * @return if cell is empty (true) else false
     */
    public boolean isEmpty() {
        Piece p = this.piece.iterator().next();
        return p.getId() == 0;
    }


    /**
     * move the piece in the current cell to the destination cell
     * <p>
     *     This function moves the piece in the current cell to the destination cell.
     *     It gets the iterator of the piece list and checks the size of the set.
     *     If the set contains more than one piece,
     *     it moves the second piece in the set (the first piece is assumed to be a Knight and is not moved).
     *     Otherwise, it moves the only piece in the list.
     *     The runtime efficiency of this function depends on the size of the list.
     *     If the set is empty, then the runtime efficiency is O(1).
     *     If the set contains one or more pieces, then the runtime efficiency is O(n),
     *     where n is the size of the list.
     * </p>
     * @param board : game board
     * @param dest_x : the destination cell column
     * @param dest_y : the destination cell row
     * @return : If the move is done successfully
     */
    public boolean movePiece (Cell[][] board, int dest_x, int dest_y) {
        Iterator<Piece> iterator = this.piece.iterator();
        if (this.piece.size() > 1) {
            iterator.next();
            Piece p = iterator.next();
            return p.move(board, this.x, this.y, dest_x, dest_y);
        }
        else {
            Piece p = iterator.next();
            return p.move(board, this.x, this.y, dest_x, dest_y);
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell cell)) return false;
        return getX() == cell.getX() && getY() == cell.getY() && getPiece().equals(cell.getPiece());
    }


    @Override
    public String toString() {
        return piece.toString();
    }
}
