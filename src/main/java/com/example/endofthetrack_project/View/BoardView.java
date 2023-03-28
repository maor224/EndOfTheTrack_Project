package com.example.endofthetrack_project.View;

import com.example.endofthetrack_project.Controller.GameController;
import com.example.endofthetrack_project.Model.Ball;
import com.example.endofthetrack_project.Model.Cell;
import com.example.endofthetrack_project.Model.Knight;
import com.example.endofthetrack_project.Model.Piece;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Iterator;


/**
 * BoardView class represent the gui of the game
 * @author maor
 */
public class BoardView {

    /**
     * attributes of the class
     */
    private Cell[][] cells;
    private GridPane gridPane;
    private Label turn;
    private Label validMove;
    private Label winner;
    private GameController controller;
    private Stage stage;
    private static final int WIDTH = 7;
    private static final int HEIGHT = 8;
    private CellView[][] cellViews;



    public BoardView(Stage stage) {
        // set the stage and the gui components
        this.stage = stage;
        gridPane = new GridPane();
        turn = new Label("");
        validMove = new Label("");
        winner = new Label("");
        this.cells = new Cell[HEIGHT][WIDTH];
        this.cellViews = new CellView[HEIGHT][WIDTH];

        // take the screen size
        Screen screen = Screen.getPrimary();

        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        // build the board
        for (int i = 1;i < WIDTH - 1;i++) {
            this.cells[HEIGHT - 1][i] = new Cell(i, HEIGHT - 1);
            Piece p = new Knight("white", i);
            this.cells[HEIGHT - 1][i].setPiece(p);
        }
        for (int i = 1;i < WIDTH - 1;i++) {
            this.cells[0][i] = new Cell(i, 0);
            Piece p = new Knight("black", 6 + i);
            this.cells[0][i].setPiece(p);
        }
        Ball whiteBall = new Ball("white", 6);
        Ball blackBall = new Ball("black", 12);
        this.cells[0][WIDTH / 2].setPiece(blackBall);
        this.cells[HEIGHT - 1][WIDTH / 2].setPiece(whiteBall);


        for (int i = 0; i < HEIGHT;i++){
            for (int j = 0;j < WIDTH;j++) {
                if (this.cells[i][j] == null) {
                    this.cells[i][j] = new Cell(j, i);
                    this.cells[i][j].setPiece(new Knight("none", 0));
                }
            }
        }


        // for each cell an instance is created on the board as a gui component
        // and an option of clicking on it is attached to it
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                this.cellViews[row][col] = new CellView(cells[row][col]);
                setMouseEventForCellView(this.cellViews[row][col]);

                gridPane.add(this.cellViews[row][col], col, row);

            }
        }
        gridPane.setAlignment(Pos.CENTER);


        VBox vBox = new VBox(turn, validMove, winner);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setPadding(new Insets(10));

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        borderPane.setTop(vBox);


        Scene scene = new Scene(borderPane, screenWidth, screenHeight);

        this.stage.setTitle("Game Board");
        this.stage.setScene(scene);

    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public Label getTurn() {
        return turn;
    }

    public Label getValidMove() {
        return validMove;
    }

    public Label getWinner() {
        return winner;
    }

    /**
     * show the gui on the user screen
     */
    public void show() {
        this.stage.show();
    }

    /**
     * handle mouse event when mouse clicked for each cell
     * @param cellView : the cell we want to add a mouse event to
     */
    public void setMouseEventForCellView (CellView cellView) {
        if (cellView.getBallView() != null) {
            cellView.getBallView().setOnMouseClicked(event -> {
                int cellRow = GridPane.getRowIndex(cellView);
                int cellCol = GridPane.getColumnIndex(cellView);
                // do something with the cellRow and cellCol
                System.out.println(cellRow + " " + cellCol);
                controller.handleMouseEvent(cellRow, cellCol);
            });
        }
        else {
            cellView.getKnightView().setOnMouseClicked(event -> {
                int cellRow = GridPane.getRowIndex(cellView);
                int cellCol = GridPane.getColumnIndex(cellView);
                // do something with the cellRow and cellCol
                System.out.println(cellRow + " " + cellCol);
                controller.handleMouseEvent(cellRow, cellCol);
            });
        }
    }
    public CellView[][] getCellViews() {
        return cellViews;
    }


    public void setController(GameController controller) {
        this.controller = controller;
    }

    /**
     * move the piece on gui according the model
     * @param source : source cell
     * @param dest : dest cell
     * @param s_row : source row
     * @param s_col : source column
     * @param d_row : destination row
     * @param d_col : destination column
     */
    public void movePiece(Cell source, Cell dest, int s_row, int s_col, int d_row, int d_col) {
        Cell[][] board = controller.getModel().getBoard();
        ObservableList<Node> childrens = gridPane.getChildren();
        StackPane stackPane = new StackPane();
        StackPane stackPaneDest = new StackPane();
        Iterator<Piece> iterator = source.getPiece().iterator();
        Piece p = iterator.next();
        System.out.println(source.getPiece());
        if (source.getPiece().size() == 1 && p.getColor().equals("none")) {

            for (Node node : childrens) {
                if(GridPane.getRowIndex(node) == s_row && GridPane.getColumnIndex(node) == s_col) {
                    stackPane = (StackPane) node;
                    break;
                }
            }

            for (Node node : childrens) {
                if(GridPane.getRowIndex(node) == d_row && GridPane.getColumnIndex(node) == d_col) {
                    stackPaneDest = (StackPane) node;
                    break;
                }
            }
            Rectangle rectangle;
            if (this.controller.getModel().getCurrPlayer().getId() == 1) {
                rectangle = new Rectangle(80, 80, Color.WHITE);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(2);
                Rectangle empty = new Rectangle(80, 80, Color.rgb(144, 100, 66));
                empty.setStroke(Color.TRANSPARENT);
                empty.setStrokeWidth(2);
                stackPane.getChildren().remove(rectangle);
                stackPane.getChildren().add(empty);
                stackPaneDest.getChildren().add(rectangle);
                GridPane.setRowIndex(rectangle, d_row);
                GridPane.setColumnIndex(rectangle, d_col);
                System.out.println("Rect: " + GridPane.getRowIndex(rectangle) + " " + GridPane.getColumnIndex(rectangle));
                cellViews[s_row][s_col].setCell(board[s_row][s_col]);
                cellViews[d_row][d_col].setCell(board[d_row][d_col]);
                cellViews[s_row][s_col].setKnightView(empty);
            }
            else {
                rectangle = new Rectangle(80, 80, Color.BLACK);
                rectangle.setStroke(Color.WHITE);
                rectangle.setStrokeWidth(2);
                Rectangle empty = new Rectangle(80, 80, Color.rgb(144, 100, 66));
                empty.setStroke(Color.TRANSPARENT);
                empty.setStrokeWidth(2);
                stackPane.getChildren().remove(rectangle);
                stackPane.getChildren().add(empty);
                stackPaneDest.getChildren().add(rectangle);
                GridPane.setRowIndex(rectangle, d_row);
                GridPane.setColumnIndex(rectangle, d_col);
                System.out.println("Rect: " + GridPane.getRowIndex(rectangle) + " " + GridPane.getColumnIndex(rectangle));
                cellViews[s_row][s_col].setCell(board[s_row][s_col]);
                cellViews[d_row][d_col].setCell(board[d_row][d_col]);
                cellViews[s_row][s_col].setKnightView(empty);
            }
            System.out.println("Pos: " + GridPane.getRowIndex(cellViews[d_row][d_col]) + " "
                    + GridPane.getColumnIndex(cellViews[d_row][d_col]));
            cellViews[d_row][d_col].setKnightView(rectangle);
            setMouseEventForCellView(cellViews[s_row][s_col]);
            setMouseEventForCellView(cellViews[d_row][d_col]);
        }
        else if (source.getPiece().size() == 1) {
            for (Node node : childrens) {
                if(GridPane.getRowIndex(node) == s_row && GridPane.getColumnIndex(node) == s_col) {
                    stackPane = (StackPane) node;
                    break;
                }
            }

            for (Node node : childrens) {
                if(GridPane.getRowIndex(node) == d_row && GridPane.getColumnIndex(node) == d_col) {
                    stackPaneDest = (StackPane) node;
                    break;
                }
            }
            if (this.controller.getModel().getCurrPlayer().getId() == 1) {
                Circle circle = new Circle(25, Color.rgb(204, 200, 150));
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(2);
                stackPane.getChildren().remove(cellViews[s_row][s_col].getBallView());
                stackPaneDest.getChildren().add(circle);
                GridPane.setRowIndex(circle, d_row);
                GridPane.setColumnIndex(circle, d_col);
                System.out.println("Circle: " + GridPane.getRowIndex(circle) + " " + GridPane.getColumnIndex(circle));
                cellViews[s_row][s_col].setCell(board[s_row][s_col]);
                cellViews[d_row][d_col].setCell(board[d_row][d_col]);
                cellViews[s_row][s_col].setBallView(null);
                System.out.println("Pos: " + GridPane.getRowIndex(cellViews[d_row][d_col]) + " "
                        + GridPane.getColumnIndex(cellViews[d_row][d_col]));
                cellViews[d_row][d_col].setBallView(circle);
                setMouseEventForCellView(cellViews[s_row][s_col]);
                setMouseEventForCellView(cellViews[d_row][d_col]);
                if (this.controller.getModel().isWinner(this.controller.getModel().getCurrPlayer())) {
                    System.out.println("Win: " + this.controller.getModel().getCurrPlayer().getColor());
                    winner.setText("Winner: " + this.controller.getModel().getCurrPlayer().getColor());
                    winner.setFont(Font.font(20));
                }
            }
            else {
                Circle circle = new Circle(25, Color.rgb(50, 50, 50));
                circle.setStroke(Color.WHITE);
                circle.setStrokeWidth(2);
                stackPane.getChildren().remove(cellViews[s_row][s_col].getBallView());
                stackPaneDest.getChildren().add(circle);
                GridPane.setRowIndex(circle, d_row);
                GridPane.setColumnIndex(circle, d_col);
                System.out.println("Circle: " + GridPane.getRowIndex(circle) + " " + GridPane.getColumnIndex(circle));
                cellViews[s_row][s_col].setCell(board[s_row][s_col]);
                cellViews[d_row][d_col].setCell(board[d_row][d_col]);
                cellViews[s_row][s_col].setBallView(null);
                System.out.println("Pos: " + GridPane.getRowIndex(cellViews[d_row][d_col]) + " "
                        + GridPane.getColumnIndex(cellViews[d_row][d_col]));
                cellViews[d_row][d_col].setBallView(circle);
                setMouseEventForCellView(cellViews[s_row][s_col]);
                setMouseEventForCellView(cellViews[d_row][d_col]);
                if (this.controller.getModel().isWinner(this.controller.getModel().getCurrPlayer())) {
                    System.out.println("Win: " + this.controller.getModel().getCurrPlayer().getColor());
                    winner.setText("Winner: " + this.controller.getModel().getCurrPlayer().getColor());
                    winner.setFont(Font.font(25));
                }
            }
        }
    }
}
