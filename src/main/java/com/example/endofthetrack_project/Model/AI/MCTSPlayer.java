package com.example.endofthetrack_project.Model.AI;

import com.example.endofthetrack_project.Model.Board;
import com.example.endofthetrack_project.Model.Cell;
import com.example.endofthetrack_project.Model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * The MCTSPlayer class represents a Monte Carlo Tree Search (MCTS) player for a game.
 * It is used to generate the next move for the AI player based on a given board state.
 * The class uses the UCT algorithm for selecting the most promising node to explore in the search tree.
 */
public class MCTSPlayer {

    // Constant representing the score assigned to a win state
    private static final int WIN_SCORE = 10;
    // The level of the MCTS player
    private int level;
    // The ID of the opponent player
    private int opponent;

    /**
     * Constructs a new MCTSPlayer object with a default level of 3.
     */
    public MCTSPlayer() {
        this.level = 3;
    }

    /**
     * Returns the level of the MCTS player.
     *
     * @return The level of the MCTS player
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the level of the MCTS player.
     *
     * @param level The new level of the MCTS player
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Calculates the time limit for the current level of the MCTS player.
     *
     * @return The time limit in milliseconds for the current level of the MCTS player
     */
    private int getMillisForCurrentLevel() {
        return 2 * (this.level - 1) + 1;
    }

    /**
     * Finds the next move for the AI player based on the given board state.
     * <p>
     *     The function initializes a search tree with the root node representing the current board state and the opponent as the current player.
     *     It sets a time limit for the search and performs the MCTS search until
     *     the time limit is reached. The algorithm consists of four parts:
     *
     *     Selection: Traverse the tree from the root to a leaf node using the UCT algorithm
     *     to determine the most promising node to explore next.
     *     Expansion: Expand the selected node if it represents a non-terminal state.
     *     Simulation: Simulate a random game from the selected node to a terminal state,
     *     using the default policy of selecting random moves.
     *     Back Propagation: Update the scores of the nodes in the path from the selected node
     *     to the root node based on the result of the simulated game.
     * </p>
     * <p>
     *     The function returns the board state representing the next move for the AI player.
     *     The memory efficiency of the function is good since it only stores
     *     the necessary information in the search tree.
     *     The runtime efficiency depends on the size of the search tree and the time limit
     *     set for the search.
     * </p>
     * @param board The current board state
     * @return The board state representing the next move for the AI player
     */
    public Board findNextMove(Board board) {
        int count = 0;
        // Set the time limit for the search
        long start = System.currentTimeMillis();
        long end = start + 60L * getMillisForCurrentLevel();

        // Initialize the search tree
        int playerNum = board.getCurrPlayer().getId();
        opponent = 3 - playerNum;
        Tree tree = new Tree();
        Node rootNode = tree.getRoot();
        rootNode.getState().setBoard(board);
        rootNode.getState().setPlayerNum(opponent);

        Node winnerNode = null;
        boolean canLoseNextMove = false;
        boolean flag = false;

        System.out.println("hello");
        if (canOpponentWinNextMove(rootNode.getState().getBoard())) {
            List<Node> availableNodes = new ArrayList<>();
            canLoseNextMove = true;
            expand(rootNode);
            for (Node node : rootNode.getChildArray()) {
                State state = new State(node.getState());
                if (!canOpponentWinNextMove(state.getBoard())) {
                    availableNodes.add(node);
                }
            }
            rootNode.getChildArray().clear();
            System.out.println("times: " + count);
            if (availableNodes.size() > 0) {
                Random random = new Random();
                int randomIndex = random.nextInt(availableNodes.size());
                winnerNode = availableNodes.get(randomIndex);
            }
            else {
                flag = true;
            }
        }
        if (winnerNode == null || flag){
            // Perform the MCTS search until the time limit is reached
            while (System.currentTimeMillis() < end) {
                // Part 1 - Selection
                Node selectedNode = selectPromisingNode(rootNode);
                // Part 2 - Expansion
                if (selectedNode.getState().getBoard().checkStatus() == Board.IN_PROGRESS) {
                    expand(selectedNode);
                }
                // Part 3 - Simulation
                Node nodeToExplore = selectedNode;
                if (selectedNode.getChildArray().size() > 0) {
                    nodeToExplore = selectedNode.getRandomChildNode();
                }
                nodeToExplore.getState().getBoard().getPlayers()[1]
                        .setPieces(nodeToExplore.getState().getBoard().getCurrPlayer().getPieces());

                int result = simulateGame(nodeToExplore);
                // Part 4 - Back Propagation
                backPropagation(nodeToExplore, result);
                count++;
            }
            System.out.println("times: " + count);
        }



        Board boardToReturn = null;
        // Select the child node with the highest score as the next move
        if (!canLoseNextMove) {
            if (winnerNode != null) {
                tree.setRoot(winnerNode);
                boardToReturn = winnerNode.getState().getBoard();
            } else {
                // Handle the case when winnerNode is null
                Node childWithMaxScore = rootNode.getChildWithMaxScore();
                if (childWithMaxScore != null) {
                    tree.setRoot(childWithMaxScore);
                    boardToReturn = childWithMaxScore.getState().getBoard();
                }
            }
        }
        else {
            if (winnerNode != null) {
                tree.setRoot(winnerNode);
                boardToReturn = winnerNode.getState().getBoard();
            } else {
                // Handle the case when winnerNode is null
                Node childWithMaxScore = rootNode.getChildWithMaxScore();
                if (childWithMaxScore != null) {
                    tree.setRoot(childWithMaxScore);
                    boardToReturn = childWithMaxScore.getState().getBoard();
                }
            }
        }
        return boardToReturn;
    }

    private boolean canOpponentWinNextMove(Board board) {
        board.switchTurn();
        int otherPlayer = board.getCurrPlayer().getId();
        for (Cell cell : board.getPlayers()[otherPlayer - 1].getPieces()) {
            if (cell.getPiece().size() == 2) {
                List<Cell> availablePositions = board.getAvailablePositions(cell);
                for (Cell position : availablePositions) {
                    if (board.getDistanceToGoal(position, otherPlayer) == 0) {
                        board.switchTurn();
                        return true;
                    }
                }
            }
        }
        board.switchTurn();
        return false;
    }




    /**
     * The selectPromisingNode function selects the most promising node in the tree to explore next,
     * based on the UCT (Upper Confidence Bound applied to Trees) algorithm.
     * It returns the selected node.
     * <p>
     *     The function has a time complexity of O(log n),
     *     where n is the size of the search tree, as it traverses the tree from
     *     the root to a leaf node. The memory efficiency of the function is O(1),
     *     as it does not create any new data structures.
     * </p>
     * @param rootNode The root node of the search tree.
     * @return The most promising node in the search tree to explore next.
     */
    private Node selectPromisingNode(Node rootNode) {
        // Traverse the search tree from the root node to a leaf node
        // using the UCT algorithm to determine the most promising node to explore next
        Node node = rootNode;
        while (node.getChildArray().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }



    /**
     * Expands a node in the Monte Carlo Tree Search (MCTS) algorithm
     * by creating child nodes for each possible move that the current player can make.
     * <p>
     *     This function expands a node by creating new child nodes for each possible move
     *     that the current player can make.
     *     The time complexity of the function is proportional to the number of possible
     *     moves that the current player can make.
     *     The memory complexity is proportional to the number of child nodes
     *     that are added to the parent node's child array.
     * </p>
     * @param node The node to expand.
     */
    private void expand(Node node) {
        // Get the current player's pieces on the board
        Cell[] cells = node.getState().getBoard().getCurrPlayer().getPieces();
        // Create child nodes for each possible move for each piece
        for (Cell cell : cells) {
            // Get all possible states resulting from moving the current piece
            List<State> possibleStates = node.getState().getAllPossibleStates(cell);
            // Create a new child node for each possible state and add it to the parent node's child array
            for (State state : possibleStates) {
                Node newNode = new Node(state);
                newNode.setParent(node);
                newNode.getState().setPlayerNum(node.getState().getOpponent());
                node.getChildArray().add(newNode);
            }
        }
    }


    /**
     * Performs back propagation in the Monte Carlo Tree Search (MCTS) algorithm
     * by updating the score and visit count of each node
     * along the path from the expanded node to the root node.
     * <p>
     *     This function performs back propagation by updating the score
     *     and visit count of each node along the path from the expanded node to the root
     *     node in the Monte Carlo Tree Search (MCTS) algorithm.
     *     The time complexity of the function is proportional to the height of the tree,
     *     and the memory complexity is constant since the function only updates existing
     *     nodes in the tree.
     * </p>
     * @param nodeToExplore The node that was selected for exploration in the simulation phase.
     * @param playerNum     The number of the player who won the game in the simulation phase.
     */
    private void backPropagation(Node nodeToExplore, int playerNum) {
        // Traverse up the tree from the explored node to the root node, updating each node's score and visit count
        Node tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.getState().incrementVisit();
            if (tempNode.getState().getPlayerNum() == playerNum) {
                // If the current player won the simulated game, add the win score to the node's score
                tempNode.getState().addScore(WIN_SCORE);
            }
            else {
                // If the opponent player won the simulated game, subtract the win score from the node's
                tempNode.getState().addScore(WIN_SCORE * -1);
            }
            tempNode = tempNode.getParent();
        }
    }


    /**
     * Simulates a game from the given node using a random policy until a terminal state is reached, and returns the number of the player who won the game.
     * <p>
     * This function simulates a game from the given node using a random policy
     * until a terminal state is reached in the Monte Carlo Tree Search (MCTS) algorithm.
     * The function returns the number of the player who won the simulated game.
     * The time complexity of the function is proportional to the length of the simulate game,
     * and the memory complexity is constant since the function only creates
     * a copy of the node's state to simulate the game from.
     * </p>
     *
     * @param node The node to start the simulation from.
     * @return The number of the player who won the simulated game.
     */
    private int simulateGame(Node node) {
        // Create a copy of the current node's state to simulate the game from
        Node simulationNode = new Node(node.getState());
        State tempState = simulationNode.getState();
        // Check if the game is already in a terminal state
        int status = tempState.getBoard().checkStatus();
        if (status == opponent) {
            // If the opponent player has already won, set the parent node's win score
            // to minimum value and return the opponent player's number
            simulationNode.getParent().getState().setWinScore(Integer.MIN_VALUE);
            return status;
        }
        // Simulate the game until a terminal state is reached
        while (status == Board.IN_PROGRESS) {
            tempState.switchPlayer();
            tempState.getBoard().switchTurn();
            tempState.randomPlay(simulationNode);
            status = tempState.getBoard().checkStatus();
        }
        // Return the number of the player who won the simulated game
        return status;
    }

}


