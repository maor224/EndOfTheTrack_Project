package com.example.endofthetrack_project.Model;

import java.util.Objects;

/**
 * Piece class that represents the piece in the game
 * @author maor
 */
public abstract class Piece {

    /**
     * attributes of class: color of the piece, id of the piece
     */
    private String color;
    private int id;

    /**
     * initialize piece
     * @param color: the color of piece
     * @param id : the id of the piece
     */
    public Piece(String color, int id) {
        this.color = color;
        this.id = id;
    }

    public Piece(Piece piece) {
        this.color = piece.color;
        this.id = piece.id;
    }

    /**
     * move a piece to specific position in the board
     * @param board : game board
     * @param source_x: the source column
     * @param source_y: the source row
     * @param dest_x: the target column
     * @param dest_y: the target row
     */
    public abstract boolean move(Cell[][] board, int source_x, int source_y, int dest_x, int dest_y);

    /**
     * check if move is valid by the piece moving rules
     * @param board : game board
     * @param source_x: the source column
     * @param source_y: the source row
     * @param dest_x: the target column
     * @param dest_y: the target row
     * @return: is the move is valid (true) else (false)
     */
    public abstract boolean validMove(Cell[][] board, int source_x, int source_y, int dest_x, int dest_y);



    public String getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece)) return false;
        Piece piece = (Piece) o;
        return getId() == piece.getId() && getColor().equals(piece.getColor());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getColor(), getId());
    }
}
