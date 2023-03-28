package com.example.endofthetrack_project.Model;

import java.util.*;

/**
 * Board class represent the model of the game and includes the logic of the game
 * @author maor
 */
public class Board {

    /**
     * the attributes of the Board
     */
    private static final int WIDTH = 7;
    private static final int HEIGHT = 8;
    private Cell[][] board;
    private Player currPlayer;
    private ArrayList<Piece> pieces;
    private Player[] players;


    /**
     * initialize the board
     * <p>
     *     The constructor function initializes the board, pieces, and players.
     *     It calls initPlayers() and buildBoard() functions.
     *     The function has a time complexity of O(WIDTH * HEIGHT) to initialize the board,
     *     and O(1) to initialize pieces and players.
     *     The memory complexity is O(WIDTH * HEIGHT) for the board 2D array,
     *     O(1) for currPlayer, and O(WIDTH) for each of the two players arrays.
     * </p>
     */
    public Board() {
        this.board = new Cell[HEIGHT][WIDTH];
        this.pieces = new ArrayList<>();
        this.players = new Player[2];
        initPlayers();
        buildBoard();
    }

    /**
     * initialize the players
     * <p>
     *     This function initializes the two players.
     *     It has a time complexity of O(1) and a memory complexity of O(1) for each player.
     * </p>
     */
    public void initPlayers() {
        this.players[0] = new Player(1, "white");
        this.players[1] = new Player(2, "black");
        this.currPlayer = players[0];
        this.currPlayer.setCurrent(true);
    }

    public Player[] getPlayers() {
        return players;
    }

    /**
     * build the board in the 2d array
     * <p>
     *     This function builds the game board.
     *     It initializes white pieces, black pieces, and balls.
     *     The function has a time complexity of O(WIDTH * HEIGHT) to initialize the board,
     *     O(WIDTH) to initialize the pieces,
     *     and O(1) to initialize the balls.
     *     The memory complexity is O(WIDTH * HEIGHT) for the board 2D array
     *     and O(WIDTH) for the two pieces ArrayLists and the two players ball attributes.
     * </p>
     */
    public void buildBoard() {
        // initialize white pieces
        for (int i = 1; i < WIDTH - 1; i++) {
            this.board[HEIGHT - 1][i] = new Cell(i, HEIGHT - 1);
            Piece p = new Knight("white", i);
            this.board[HEIGHT - 1][i].setPiece(p);
            this.pieces.add(p);
            this.players[0].getPieces()[i - 1] = this.board[HEIGHT - 1][i];

        }
        // initialize black pieces
        for (int i = 1; i < WIDTH - 1; i++) {
            this.board[0][i] = new Cell(i, 0);
            Piece p = new Knight("black", 6 + i);
            this.board[0][i].setPiece(p);
            this.pieces.add(p);
            this.players[1].getPieces()[i - 1] = this.board[0][i];
        }
        // initialize balls
        Ball whiteBall = new Ball("white", 6);
        Ball blackBall = new Ball("black", 12);
        this.board[0][WIDTH / 2].setPiece(blackBall);
        this.board[HEIGHT - 1][WIDTH / 2].setPiece(whiteBall);
        this.players[0].setBall(this.board[HEIGHT - 1][WIDTH / 2]);
        this.players[1].setBall(this.board[0][WIDTH / 2]);
        this.players[0].getBall().setX(WIDTH / 2);
        this.players[0].getBall().setY(HEIGHT - 1);
        this.players[1].getBall().setX(WIDTH / 2);
        this.players[1].getBall().setY(0);


        // initialize the rest of the board
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (this.board[i][j] == null) {
                    this.board[i][j] = new Cell(j, i);
                    this.board[i][j].setPiece(new Knight("none", 0));
                }
            }
        }
    }


    /**
     * switch the turns between the players
     * <p>
     *     This function switches the current player to the other player.
     *     It has a time complexity of O(1) and a memory complexity of O(1).
     * </p>
     */
    public void switchTurn () {
        if (players[0].isCurrent()) {
            this.currPlayer = players[1];
            this.currPlayer.setCurrent(true);
            players[0].setCurrent(false);
        }
        else  {
            this.currPlayer = players[0];
            this.currPlayer.setCurrent(true);
            players[1].setCurrent(false);
        }
    }

    public Player getCurrPlayer() {
        return currPlayer;
    }

    /**
     * check if the piece in this cell is included in the pieces of the current player
     * <p>
     *     This function checks if the piece in the cell at (row, col) is included in the pieces of the current player.
     *     It has a time complexity of O(1) and a memory complexity of O(1).
     * </p>
     * @param row : the row of the piece in the board
     * @param col : the column of the piece in the board
     * @return : if the piece in this cell is included in the pieces of the current player
     */
    public boolean validPosition(int row, int col) {
        Cell cell = board[row][col];
        if (cell.getPiece().size() == 2) {
            return this.currPlayer.getBall().equals(cell);
        }
        else return Arrays.asList(this.currPlayer.getPieces()).contains(cell);
    }

    public Cell[][] getBoard() {
        return board;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    /**
     * check for win of the current player
     * @param p : the current player
     * @return : if there is a win
     */
    public boolean isWinner (Player p) {
        return p.isWinner();
    }

    /**
     * @return : string that represent the current board state
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Cell[] cells : this.board) {
            for (int j = 0; j < this.board[0].length; j++) {
                if (cells[j] != null) {
                    s.append(cells[j].toString()).append(" ");
                } else {
                    s.append(cells[j]).append(" ");
                }
            }
            s.append("\n");
        }
        return s.toString();
    }
}
