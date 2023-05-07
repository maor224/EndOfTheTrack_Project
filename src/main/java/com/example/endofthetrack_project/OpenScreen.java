package com.example.endofthetrack_project;

import com.example.endofthetrack_project.Controller.GameController;
import com.example.endofthetrack_project.Model.Board;
import com.example.endofthetrack_project.View.BoardView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Objects;

public class OpenScreen extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Screen screen = Screen.getPrimary();

        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        // Create a background with a gradient effect
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);

        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #53a0fb, #3871c1);");

        // Create a welcome message
        Text welcomeText = new Text("Welcome to End of the Track!");
        welcomeText.setFont(Font.font("Arial", 48));
        welcomeText.setFill(Color.WHITE);
        DropShadow ds = new DropShadow();
        ds.setRadius(5.0);
        ds.setOffsetX(3.0);
        ds.setOffsetY(3.0);
        ds.setColor(Color.color(0, 0, 0, 0.3));
        welcomeText.setEffect(ds);

        // Create buttons for human vs AI game modes
        Button human = new Button("Human");
        human.setPrefWidth(200);
        human.setPrefHeight(50);
        human.setStyle("-fx-background-color: #ecf0f1; -fx-font-size: 24; -fx-text-fill: #333333;");
        human.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Board board = new Board();
                vbox.getChildren().clear();
                BoardView view = new BoardView(primaryStage);
                GameController controller = null;
                try {
                    controller = new GameController(board, view, false);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                controller.startGame();
            }
        });

        Button ai = new Button("AI");
        ai.setPrefWidth(200);
        ai.setPrefHeight(50);
        ai.setStyle("-fx-background-color: #ecf0f1; -fx-font-size: 24; -fx-text-fill: #333333;");
        ai.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Board board = new Board();
                vbox.getChildren().clear();
                BoardView view = new BoardView(primaryStage);
                try {
                    GameController controller = new GameController(board, view, true);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        vbox.getChildren().addAll(welcomeText, human, ai);
        Scene newScene = new Scene(vbox, screenWidth, screenHeight);

        primaryStage.setScene(newScene);

        // show the stage
        primaryStage.show();
    }

}
