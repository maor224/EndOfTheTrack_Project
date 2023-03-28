package com.example.endofthetrack_project.Model;

import java.util.Iterator;

/**
 * Ball class that represents the ball in the game
 * @author maor
 */
public class Ball extends Piece {

    /**
     * initialize piece
     * @param color : the color of ball
     * @param id : the ball id
     */
    public Ball(String color, int id) {
        super(color, id);
    }

    /**
     * move the ball to specific position in the board
     *<p>
     * The move() function has a time complexity of O(n),
     * where n is the number of pieces in the source cell,
     * since it needs to iterate through the list of pieces in the source cell.
     * The space complexity of this function is O(1),
     * since it only uses a constant amount of memory.
     *</p>
     *
     * @param board    : game board
     * @param source_x : the source column
     * @param source_y : the source row
     * @param dest_x   : the target column
     * @param dest_y   : the target row
     */
    @Override
    public boolean move(Cell[][] board, int source_x, int source_y, int dest_x, int dest_y) {
        Cell sourceCell = board[source_y][source_x];
        Cell destCell = board[dest_y][dest_x];

        Iterator<Piece> iterator = sourceCell.getPiece().iterator();
        if (validMove(board, source_x, source_y, dest_x, dest_y)) {
            iterator.next();
            destCell.setPiece(iterator.next());
            sourceCell.getPiece().remove(this);
            return true;
        }
        return false;
    }

    /**
     * check if move is valid by the ball moving rules
     * <p>
     * The validMove() function has a time complexity of O(n),
     * where n is the number of cells that the ball needs to pass through
     * in order to get to the destination cell.
     * The space complexity of this function is O(1),
     * since it only uses a constant amount of memory.
     * </p>
     * @param board : game board
     * @param source_x : the source column
     * @param source_y : the source row
     * @param dest_x   : the target column
     * @param dest_y   : the target row
     * @return: is the move is valid (true) else (false)
     */
    @Override
    public boolean validMove(Cell[][] board, int source_x, int source_y, int dest_x, int dest_y) {
        // check if position is out of the board
        if (source_x < 0 || source_x > 6 || source_y < 0 || source_y > 7 ||
                dest_x < 0 || dest_x > 6 || dest_y < 0 || dest_y > 7) {
            return false;
        }

        if (source_x == dest_x || source_y == dest_y || Math.abs(source_x - dest_x) == Math.abs(source_y - dest_y)) {
            int dx = Integer.signum(dest_x - source_x);
            int dy = Integer.signum(dest_y - source_y);
            int x = source_x + dx;
            int y = source_y + dy;
            boolean foundOpponent = false;
            if (board[dest_y][dest_x].isEmpty()) {
                return false;
            }
            while (x != dest_x || y != dest_y) {
                if (!board[y][x].isEmpty()) {
                    Piece piece = board[y][x].getPiece().iterator().next();
                    // Opponent piece blocking the way
                    foundOpponent = !(piece.getColor().equals(this.getColor()) || piece.getColor().equals("none"));// Same-color piece blocking the way
                }
                x += dx;
                y += dy;
            }
            return !foundOpponent;
        }
        return false;
    }


    @Override
    public String toString() {
        return super.getId() + " " + super.getColor();
    }

}