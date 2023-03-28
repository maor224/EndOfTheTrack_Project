package com.example.endofthetrack_project;

import com.example.endofthetrack_project.Controller.GameController;
import com.example.endofthetrack_project.Model.Board;
import com.example.endofthetrack_project.View.BoardView;
import javafx.application.Application;
import javafx.stage.Stage;

public class BoardViewTest extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Board board = new Board();
        BoardView view = new BoardView(primaryStage);
        GameController controller = new GameController(board, view);
        controller.startGame();
    }
}
