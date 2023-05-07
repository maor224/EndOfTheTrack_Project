package com.example.endofthetrack_project.Model;


/**
 * Knight class that represents the knight in the game
 * @author maor
 */
public class Knight extends Piece {


    /**
     * initialize knight
     * @param color : the color of piece
     */
    public Knight(String color, int id) {
        super(color, id);
    }

    public Knight(Knight knight) {
        super(knight);
    }

    /**
     * move a piece to specific position in the board
     * <p>
     *     The move function has a time complexity of O(1)
     *     as it performs a constant number of operations regardless of the size of the input.
     *     Its space complexity is also O(1) as it only uses a constant amount of memory.
     * </p>
     * @param board    : game board
     * @param source_x : the source column
     * @param source_y : the source row
     * @param dest_x   : the target column
     * @param dest_y   : the target row
     * @return : If the move is done successfully
     */
    @Override
    public boolean move(Cell[][] board, int source_x, int source_y, int dest_x, int dest_y) {
        Cell destCell = board[dest_y][dest_x];
        Cell sourceCell = board[source_y][source_x];
        if (validMove(board, source_x, source_y, dest_x, dest_y)) {
            if (sourceCell.getPiece().size() == 1) {
                destCell.setPiece(sourceCell.getPiece().iterator().next());
                sourceCell.setPiece(new Knight("none", 0));
                return true;
            }
        }
        return false;
    }


    /**
     * check if move is valid by the piece moving rules
     * <p>
     *     The validMove function has a time complexity of O(1)
     *     as it performs a constant number of operations regardless of the size of the input.
     *     Its space complexity is also O(1) as it only uses a constant amount of memory.
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
        if (source_x < 0 || source_x > 6 || source_y < 0 || source_y > 7 ||
                dest_x < 0 || dest_x > 6 || dest_y < 0 || dest_y > 7) {
            return false;
        }
        if (!board[dest_y][dest_x].isEmpty())
            return false;

        int dx = Math.abs(dest_x - source_x);
        int dy = Math.abs(dest_y - source_y);
        return (dx == 1 && dy == 2) || (dx == 2 && dy == 1);
    }



    @Override
    public String toString() {
        return super.getId() + " " + super.getColor();
    }
}
