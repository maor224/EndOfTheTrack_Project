package com.example.endofthetrack_project.View;

import com.example.endofthetrack_project.Controller.GameController;
import com.example.endofthetrack_project.Model.*;
import com.example.endofthetrack_project.OpenScreen;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
import java.util.List;


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
    private Button newGame;
    private GameController controller;
    private Stage stage;
    private static final int WIDTH = 7;
    private static final int HEIGHT = 8;
    private CellView[][] cellViews;
    private BorderPane borderPane;


    /**
     * build the user screen and the board
     * @param stage the stage of the javafx
     */
    public BoardView(Stage stage) {
        // set the stage and the gui components
        this.stage = stage;
        gridPane = new GridPane();
        turn = new Label("");
        validMove = new Label("");
        winner = new Label("");
        newGame = new Button("Start new game");
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

        newGame.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                OpenScreen openScreen = new OpenScreen();
                borderPane.getChildren().clear();
                openScreen.start(stage);
            }
        });

        VBox vBox = new VBox(turn, validMove, winner, newGame);
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setPadding(new Insets(10));




        borderPane = new BorderPane();
        borderPane.setCenter(gridPane);
        borderPane.setTop(vBox);



        Scene scene = new Scene(borderPane, screenWidth, screenHeight);

        this.stage.setTitle("Game Board");
        this.stage.setScene(scene);

    }

    public void updateBoard(Board board) {
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                this.cellViews[row][col] = new CellView(board.getBoard()[row][col]);
                setMouseEventForCellView(this.cellViews[row][col]);

                gridPane.add(this.cellViews[row][col], col, row);

            }
        }
        if (this.controller.getModel().isWinner(this.controller.getModel().getCurrPlayer())) {
            System.out.println("Win: " + this.controller.getModel().getCurrPlayer().getColor());
            winner.setText("Winner: " + this.controller.getModel().getCurrPlayer().getColor());
            winner.setFont(Font.font(20));


        }
    }

    public Stage getStage() {
        return this.stage;
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
                try {
                    controller.handleMouseEvent(cellRow, cellCol);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

        }
        else {

            cellView.getKnightView().setOnMouseClicked(event -> {


                int cellRow = GridPane.getRowIndex(cellView);
                int cellCol = GridPane.getColumnIndex(cellView);
                // do something with the cellRow and cellCol
                System.out.println(cellRow + " " + cellCol);
                try {
                    controller.handleMouseEvent(cellRow, cellCol);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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
    public void movePiece(Cell source, Cell dest, int s_row, int s_col, int d_row, int d_col) throws InterruptedException {
        // get the board from the model
        Cell[][] board = controller.getModel().getBoard();
        // get the content of the grid pane
        ObservableList<Node> children = gridPane.getChildren();
        StackPane stackPane = new StackPane();
        StackPane stackPaneDest = new StackPane();
        Iterator<Piece> iterator = source.getPiece().iterator();
        Piece p = iterator.next();

        // check if the source cell is empty

        if (source.getPiece().size() == 1 && p.getColor().equals("none")) {

            // find the source cell in the grid pane
            boolean sourceFound = false;
            for (Node node : children) {
                if(!sourceFound && GridPane.getRowIndex(node) == s_row && GridPane.getColumnIndex(node) == s_col) {
                    stackPane = (StackPane) node;
                    sourceFound = true;
                }
            }

            // find the destination cell in the grid pane
            boolean destFound = false;
            for (Node node : children) {
                if(!destFound && GridPane.getRowIndex(node) == d_row && GridPane.getColumnIndex(node) == d_col) {
                    stackPaneDest = (StackPane) node;
                    destFound = true;
                }
            }
            Rectangle rectangle;
            Circle circle = new Circle(25, Color.TRANSPARENT);
            // check if the current player is white
            if (this.controller.getModel().getCurrPlayer().getId() == 1) {
                // create new knight to display on the screen
                rectangle = new Rectangle(80, 80, Color.WHITE);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(2);
                // create empty knight to delete the previous knight
                Rectangle empty = new Rectangle(80, 80, Color.rgb(144, 100, 66));
                empty.setStroke(Color.TRANSPARENT);
                empty.setStrokeWidth(2);
                // remove the knight from the source cell
                stackPane.getChildren().remove(rectangle);
                Circle emptyBall = new Circle(25, Color.TRANSPARENT);
                // clear the source cell
                stackPane.getChildren().addAll(empty, emptyBall);
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(2);
                // add the knight to the destination cell
                stackPaneDest.getChildren().addAll(rectangle, circle);
                // update the row and column of the knight
                GridPane.setRowIndex(rectangle, d_row);
                GridPane.setColumnIndex(rectangle, d_col);
                // update positions of the knight in the board
                cellViews[s_row][s_col].setCell(board[s_row][s_col]);
                cellViews[d_row][d_col].setCell(board[d_row][d_col]);
                cellViews[s_row][s_col].setKnightView(empty);
                cellViews[s_row][s_col].setBallView(emptyBall);
            }
            else {
                // create new knight to display on the screen
                rectangle = new Rectangle(80, 80, Color.BLACK);
                rectangle.setStroke(Color.WHITE);
                rectangle.setStrokeWidth(2);
                // create empty knight to delete the previous knight
                Rectangle empty = new Rectangle(80, 80, Color.rgb(144, 100, 66));
                empty.setStroke(Color.TRANSPARENT);
                empty.setStrokeWidth(2);
                // remove the knight from the source cell
                stackPane.getChildren().remove(rectangle);
                Circle emptyBall = new Circle(25, Color.TRANSPARENT);
                // clear the source cell
                stackPane.getChildren().addAll(empty, emptyBall);
                circle.setStroke(Color.WHITE);
                circle.setStrokeWidth(2);
                // add the knight to the destination cell
                stackPaneDest.getChildren().addAll(rectangle, circle);
                // update the row and column of the knight
                GridPane.setRowIndex(rectangle, d_row);
                GridPane.setColumnIndex(rectangle, d_col);
                // update positions of the knight in the board
                cellViews[s_row][s_col].setCell(board[s_row][s_col]);
                cellViews[d_row][d_col].setCell(board[d_row][d_col]);
                cellViews[s_row][s_col].setKnightView(empty);
                cellViews[s_row][s_col].setBallView(emptyBall);
            }

            cellViews[d_row][d_col].setKnightView(rectangle);
            cellViews[d_row][d_col].setBallView(circle);
            // create new mouse event for the new positions
            setMouseEventForCellView(cellViews[s_row][s_col]);
            setMouseEventForCellView(cellViews[d_row][d_col]);
        }
        // check if the source cell had a ball
        else if (source.getPiece().size() == 1) {

            // find the source cell in the grid pane
            boolean sourceFound = false;
            for (Node node : children) {
                if(!sourceFound &&GridPane.getRowIndex(node) == s_row && GridPane.getColumnIndex(node) == s_col) {
                    stackPane = (StackPane) node;
                    sourceFound = true;
                }
            }

            // find the destination cell in the grid pane
            boolean destFound = false;
            for (Node node : children) {
                if(!destFound && GridPane.getRowIndex(node) == d_row && GridPane.getColumnIndex(node) == d_col) {
                    stackPaneDest = (StackPane) node;
                    destFound = true;
                }
            }
            Circle emptyCircle = new Circle(25, Color.TRANSPARENT);
            if (this.controller.getModel().getCurrPlayer().getId() == 1) {
                Circle circle = new Circle(25, Color.rgb(204, 200, 150));
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(2);
                stackPane.getChildren().remove(cellViews[s_row][s_col].getBallView());
                stackPaneDest.getChildren().add(circle);
                emptyCircle.setStroke(Color.BLACK);
                emptyCircle.setStrokeWidth(2);
                stackPane.getChildren().add(emptyCircle);
                GridPane.setRowIndex(circle, d_row);
                GridPane.setColumnIndex(circle, d_col);
                cellViews[s_row][s_col].setCell(board[s_row][s_col]);
                cellViews[d_row][d_col].setCell(board[d_row][d_col]);
                cellViews[s_row][s_col].setBallView(emptyCircle);
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
                emptyCircle.setStroke(Color.WHITE);
                emptyCircle.setStrokeWidth(2);
                stackPane.getChildren().add(emptyCircle);
                GridPane.setRowIndex(circle, d_row);
                GridPane.setColumnIndex(circle, d_col);
                cellViews[s_row][s_col].setCell(board[s_row][s_col]);
                cellViews[d_row][d_col].setCell(board[d_row][d_col]);
                cellViews[s_row][s_col].setBallView(emptyCircle);
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

    /**
     * This function takes a list of cells and highlights the available moves for each piece
     * on the board by changing the color of the corresponding cell in the view.
     * <p>
     * If the color of the piece is "none", the corresponding cell's KnightView is set to have
     * a fill color of GOLD.
     * <p>
     * Otherwise, the corresponding cell's BallView is set to have a fill color of GOLD.
     * <p>
     * Efficiency:
     * <p>
     * The runtime efficiency of this function is O(n),
     * where n is the number of cells in the input list, since the function iterates over
     * each cell in the list exactly once.
     * <p>
     * The memory efficiency is also O(n),
     * since the function only stores the input list and local variables,
     * and does not create any new data structures.
     *
     * @param cells the list of cells to highlight available moves for each piece on the board
     */
    public void showAvailableMoves (List<Cell> cells) {
        Iterator<Cell> iterator = cells.listIterator();
        for (Iterator<Cell> it = iterator; it.hasNext(); ) {
            Cell c = it.next();
            Iterator<Piece> pieceIterator = c.getPiece().iterator();
            Piece p = pieceIterator.next();
            if (p.getColor().equals("none")) {
                cellViews[c.getY()][c.getX()].getKnightView().setFill(Color.GOLD);
            }
            else {
                cellViews[c.getY()][c.getX()].getBallView().setFill(Color.GOLD);
            }
        }
        iterator = cells.listIterator();
    }


    /**
     * This function removes the visual indication of available moves from the board
     * by setting the fill color of the corresponding cellViews to their original value.
     * <p>
     * Efficiency:
     * <p>
     * Time Complexity: O(n), where n is the number of cells in the input list
     * <p>
     * Space Complexity: O(1), constant space complexity as the function only uses constant space
     *
     * @param cells A list of cells whose available moves need to be unshown
     */
    public void unShowAvailableMoves (List<Cell> cells) {
        Iterator<Cell> iterator = cells.listIterator();
        for (Iterator<Cell> it = iterator; it.hasNext(); ) {
            Cell c = it.next();
            Iterator<Piece> pieceIterator = c.getPiece().iterator();
            Piece p = pieceIterator.next();
            if (p.getColor().equals("none")) {
                cellViews[c.getY()][c.getX()].getKnightView().setFill(CellView.NONE_KNIGHT);
            }
            else {
                cellViews[c.getY()][c.getX()].getBallView().setFill(Color.TRANSPARENT);
            }
        }
    }

}
