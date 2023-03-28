package com.example.endofthetrack_project.Controller;

import com.example.endofthetrack_project.Model.Board;
import com.example.endofthetrack_project.View.BoardView;
import javafx.scene.text.Font;

/**
 * controller class that manage the game
 * @author maor
 */

public class GameController {

    /**
     * attributes of the controller
     */
    private Board model;
    private BoardView view;
    private int current_row;
    private int current_col;
    private int dest_col;
    private int dest_row;
    private static int count = 0;

    /**
     * construct the controller and set the controller object in the view
     * also set the turn label to the first player
     * @param model : the model of the project that represent the board
     * @param view : the gui of the project in javafx
     */
    public GameController(Board model, BoardView view) {
        this.model = model;
        this.view = view;
        this.view.setController(this);
        this.view.getTurn().setText(this.getModel().getCurrPlayer().getColor() + " turn");
        this.view.getTurn().setFont(Font.font(20));
    }

    /**
     * display the javafx window in the user screen
     */
    public void startGame() {
        view.show();
    }

    /**
     * @return : the model of the game
     */
    public Board getModel() {
        return model;
    }

    /**
     * @return : the view of the game
     */
    public BoardView getView() {
        return view;
    }

    /**
     * this function handle the mouse clicked events and move
     * the piece both in the model and view
     * @param row : the row of the item that the user clicked on
     * @param col : the column of the item that the user clicked on
     */
    public void handleMouseEvent (int row, int col) {
        // handles the first click (for the source cell)
        if (count == 0) {
            this.current_row = row;
            this.current_col = col;
            if (model.validPosition(row, col)) {
                count++;
            }
        }
        // handles the second click (for the destination cell)
        else {
            this.dest_col = col;
            this.dest_row = row;
            // doing the move both in the model and view
            if (this.model.getCurrPlayer().makeMove(this.model.getBoard(), this.model.getBoard()[this.current_row][this.current_col], this.model.getBoard()[this.dest_row][this.dest_col])) {
                System.out.println(this.model);
                this.view.getValidMove().setText("Valid Move");
                this.view.getValidMove().setFont(Font.font(20));
                this.view.getCellViews()[this.dest_row][this.dest_col].setCell(this.model.getBoard()[this.dest_row][this.dest_col]);
                this.view.movePiece(this.model.getBoard()[this.current_row][this.current_col], this.model.getBoard()[this.dest_row][this.dest_col], this.current_row, this.current_col, this.dest_row, this.dest_col);
                switchTurn();
            }
            else {
                // display in the screen if the current move is not valid
                this.view.getValidMove().setText(this.model.getCurrPlayer().getNotValid());
                this.view.getValidMove().setFont(Font.font(20));
            }
            count = 0;

//            switchTurn();
        }
    }

    /**
     * switch the turns between the players
     */
    public void switchTurn () {
        model.switchTurn();
        this.view.getTurn().setText(this.getModel().getCurrPlayer().getColor() + " turn");
        this.view.getTurn().setFont(Font.font(20));
    }



}
