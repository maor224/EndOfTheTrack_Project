package com.example.endofthetrack_project.Model.AI;

import com.example.endofthetrack_project.Model.Board;
import com.example.endofthetrack_project.Model.Cell;
import com.example.endofthetrack_project.Model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a state in the game, consisting of the current board state,
 * the player number, visit count, and win score.
 */
public class State {
    // The current board state
    private Board board;
    // The player number
    private int playerNum;
    // The number of times this state has been visited
    private int visitCount;
    // The total score of wins for this state
    private double winScore;

    /**
     * Constructor that creates a new instance of State with an empty board.
     */
    public State() {
        board = new Board();
    }

    /**
     * Constructor that creates a new instance of State based on an existing State object.
     * @param state the State object to create a new instance of the state
     */
    public State(State state) {
        this.board = new Board(state.getBoard());
        this.playerNum = state.getPlayerNum();
        this.visitCount = state.getVisitCount();
        this.winScore = state.getWinScore();
    }

    /**
     * Constructor that creates a new instance of State with a given Board object.
     * @param board the Board object to create the new instance with the board
     */
    public State(Board board) {
        this.board = new Board(board);
    }

    /**
     * Returns the current board state.
     * @return the current board state
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Sets the current board state.
     * @param board the new board state to set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Returns the player number.
     * @return the player number
     */
    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * Sets the player number.
     * @param playerNum the new player number to set
     */
    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    /**
     * Returns the opponent player number.
     * @return the opponent player number
     */
    public int getOpponent() {
        return 3 - playerNum;
    }

    /**
     * Returns the number of times this state has been visited.
     * @return the number of times this state has been visited
     */
    public int getVisitCount() {
        return visitCount;
    }

    /**
     * Sets the number of times this state has been visited.
     * @param visitCount the new visit count to set
     */
    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    /**
     * Returns the total win score of this state.
     * @return the total win score of this state
     */
    public double getWinScore() {
        return winScore;
    }

    /**
     * Sets the total win score of this state.
     * @param winScore the new win score to set
     */
    public void setWinScore(double winScore) {
        this.winScore = winScore;
    }


    /**
     * Returns a list of all possible states that can result from moving a given cell
     * on the current board.
     * <p>
     *     Runtime efficiency: O(n^2) where n is the number of available positions
     *     on the board (in the worst case, every available position results
     *     in a valid move).
     * <p>
     *     Memory efficiency: O(n) where n is the number of available positions
     *     on the board (the list of possible states is stored in memory).
     * </p>
     * @param cell The cell to move on the board.
     * @return A list of possible resulting states.
     */
    public List<State> getAllPossibleStates(Cell cell) {
        List<State> possibleStates = new ArrayList<>();
        // Get a list of available positions on the board.
        List<Cell> availablePositions = this.board.getAvailablePositions(cell);
        // Iterate over the available positions, creating new boards and players
        // for each move and adding resulting states to the list.
        for (Cell availablePosition : availablePositions) {
            Board newBoard = new Board(this.board);
            Player newPlayer = new Player(this.board.getCurrPlayer());
            newBoard.setCurrPlayer(newPlayer);
            // If the move is valid, create a new state and add it to the list.
            if (newPlayer.makeMove(newBoard.getBoard(), cell, availablePosition)) {
                State newState = new State(newBoard);
                newState.setPlayerNum(3 - this.playerNum);
                possibleStates.add(newState);
            }
        }
        return possibleStates;
    }

    /**
     * This method increments the visit count of a node.
     */
    void incrementVisit() {
        this.visitCount++;
    }

    /**
     * Adds the given score to the total win score of the node.
     * @param score the score to be added to the total win score of the node
     */
    void addScore(double score) {
        if (this.winScore != Integer.MIN_VALUE)
            this.winScore += score;
    }




    /**
     * Performs a random play from a given node.
     * <p>
     *      Precondition: the given node must have a non-null state object.
     * </p>
     * <p>
     *      Post-condition: the state of the given node will be updated to reflect the random play.
     * </p>
     * <p>
     *      If there are no possible moves from the current state,
     *      the state remains unchanged.
     * </p>
     * <p>
     *     This method selects a random available position on the board
     *     and moves a player piece to that position.
     * </p>
     * <p>
     *     If there are multiple player pieces available,
     *     it randomly selects one of them and checks for available positions.
     * </p>
     * <p>
     *     If the move is valid, the state of the node is updated to reflect
     *     the new board state after the move.
     * </p>
     * <p>
     *     This method does not return any values.
     * </p>
     * <p>
     *     Time complexity: O(N), where N is the number of available positions on
     *     the board from the current state.
     * </p>
     * <p>
     *     Memory complexity: O(N), where N is the number of available positions on
     *     the board from the current state.
     * </p>
     * @param node the node from which the random play will start.
     */

    public void randomPlay(Node node) {
        // Get available positions for all player pieces
        List<Cell> availablePositions = new ArrayList<>();
        Cell[] cells = node.getState().getBoard().getCurrPlayer().getPieces();
        for (Cell cell : cells) {
            availablePositions.addAll(this.board.getAvailablePositions(cell));
        }

        // Shuffle the available positions randomly
        Collections.shuffle(availablePositions);

        // Try moving to the available positions in random order, with priority given to winning and blocking the opponent
        int eval = evaluate(node.getState().getBoard());
        boolean prioritizeWinning = (eval > 0);
        boolean prioritizeBlocking = (eval < 0);
        boolean prioritizeDefensive = (eval < -5); // if enemy is close to my territory
        boolean prioritizeOffensive = (eval > 5); // if enemy is far from my territory
        boolean prioritizePass = (eval > 10); // if opponent is far from me and ball is in a good position to pass

        for (Cell selectedCell : availablePositions) {
            for (Cell cell : cells) {
                if (this.board.getAvailablePositions(cell).contains(selectedCell)) {
                    if (this.board.getCurrPlayer().makeMove(this.board.getBoard(), cell, selectedCell)) {
                        // If prioritizing winning, blocking, or defensive moves, only make the move if it brings the player closer to their goal
                        // If prioritizing offensive moves or passing, make the move regardless
                        int newEval = evaluate(this.board);
                        boolean opponentCanWinNextMove = canOpponentWinNextMove(this.board);
                        if ((!prioritizeWinning || newEval > eval) && (!prioritizeBlocking || newEval < eval)
                                && (!prioritizeDefensive || newEval < eval) && (!prioritizeOffensive || newEval > eval)
                                && (!prioritizePass || newEval >= eval) &&
                                !this.board.getBoard().equals(node.getState()
                                        .getBoard().getBoard()) && opponentCanWinNextMove) {
                            node.getState().setBoard(this.board);
                            return;
                        }
                        this.board.getCurrPlayer().makeMove(this.getBoard().getBoard(), cell, selectedCell);
                    }
                }
            }
        }
    }



    public int evaluate(Board board) {
        int currentPlayer = board.getCurrPlayer().getId();
        int otherPlayer = (currentPlayer == 1) ? 2 : 1;

        // Check if opponent ball can be blocked
        boolean blockOpponent = false;
        for (Cell cell : board.getPlayers()[otherPlayer - 1].getPieces()) {
            if (cell.getPiece().size() == 2 && !blockOpponent) {
                int distance = board.getDistanceToGoal(cell, currentPlayer);
                if (distance == 1) {
                    blockOpponent = true;
                }
            }
        }
        if (blockOpponent) {
            return -10;
        }

        // Check if opponent is in my half of the board and prioritize defense
        boolean defensive = false;
        for (Cell cell : board.getPlayers()[otherPlayer - 1].getPieces()) {
            int distance = board.getDistanceToGoal(cell, currentPlayer);
            if (distance <= 3 && !defensive) {
                defensive = true;
            }
        }

        // Check if opponent is far from me and prioritize offense
        boolean offensive = false;
        for (Cell cell : board.getPlayers()[otherPlayer - 1].getPieces()) {
            int distance = board.getDistanceToGoal(cell, currentPlayer);
            if (distance > 3 && !offensive) {
                offensive = true;
            }
        }

        // Check if the ball is in a position to score a goal
        for (Cell cell : board.getCurrPlayer().getPieces()) {
            if (cell.getPiece().size() == 2) {
                int distance = board.getDistanceToGoal(cell, currentPlayer);
                if (distance == 0) {
                    return 10;
                }
            }
        }

        // Move the ball as far forward as possible
        int currentDistance = Integer.MAX_VALUE;
        Cell farthestCell = null;
        for (Cell cell : board.getCurrPlayer().getPieces()) {
            if (cell.getPiece().size() == 2) {
                int distance = board.getDistanceToGoal(cell, currentPlayer);
                if (distance < currentDistance) {
                    currentDistance = distance;
                    farthestCell = cell;
                }
            }
        }


        // check the closest piece to the end of the board
        int farthestDistance = currentDistance;
        if (farthestCell != null) {
            List<Cell> availablePositions = board.getAvailablePositions(farthestCell);
            for (Cell position : availablePositions) {
                int distance = board.getDistanceToGoal(position, currentPlayer);
                if (distance < farthestDistance) {
                    farthestDistance = distance;
                }
            }
        }

        if (defensive) {
            return 2 * (farthestDistance + 1);
        } else if (offensive) {
            return 10 * (farthestDistance + 1);
        } else {
            return farthestDistance + 1;
        }
    }

    // Helper function to check if the opponent can win the next move
    private boolean canOpponentWinNextMove(Board board) {
        int currentPlayer = board.getCurrPlayer().getId();
        int otherPlayer = (currentPlayer == 1) ? 2 : 1;
        for (Cell cell : board.getPlayers()[otherPlayer - 1].getPieces()) {
            if (cell.getPiece().size() == 2) {
                List<Cell> availablePositions = board.getAvailablePositions(cell);
                for (Cell position : availablePositions) {
                    if (board.getDistanceToGoal(position, currentPlayer) == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



    /**
     * Switches the current player.
     * This method changes the current player number to the opposite of its
     * current value.
     */
    public void switchPlayer() {
        this.playerNum = 3 - this.playerNum;
    }
}
