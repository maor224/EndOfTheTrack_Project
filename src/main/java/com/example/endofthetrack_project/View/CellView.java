package com.example.endofthetrack_project.View;

import com.example.endofthetrack_project.Model.Cell;
import com.example.endofthetrack_project.Model.Piece;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Iterator;

/**
 * CellView class represent a cell component on board gui
 */
public class CellView extends StackPane {

    /**
     * attributes of class
     */
    private Cell cell;
    private Rectangle knightView;
    private Circle ballView;
    private static final int RECT_SIZE = 80;
    private static final int CIRCLE_SIZE = 25;
    private static final Color WHITE_KNIGHT = Color.WHITE;
    private static final Color BLACK_KNIGHT = Color.BLACK;
    public static final Color NONE_KNIGHT = Color.rgb(144, 100, 66);
    private static final Color WHITE_BALL = Color.rgb(204, 200, 150);
    private static final Color BLACK_BALL = Color.rgb(50, 50, 50);


    /**
     * build cell gui component according the cell from the model
     * @param cell : a cell from the board
     */
    public CellView(Cell cell) {
        this.cell = cell;

        if (this.cell.getPiece().size() == 2) {
            Iterator<Piece> iterator = this.cell.getPiece().iterator();
            Piece knight = iterator.next();
            Piece ball = iterator.next();
            if (knight.getColor().equals("white") && ball.getColor().equals("white")) {
                this.knightView = new Rectangle(RECT_SIZE, RECT_SIZE, WHITE_KNIGHT);
                this.knightView.setStroke(Color.BLACK);
                this.knightView.setStrokeWidth(2);

                this.ballView = new Circle(CIRCLE_SIZE, WHITE_BALL);
                this.ballView.setStroke(Color.BLACK);
                this.ballView.setStrokeWidth(2);
                getChildren().addAll(knightView, ballView);
            } else if (knight.getColor().equals("black") && ball.getColor().equals("black")) {
                this.knightView = new Rectangle(RECT_SIZE, RECT_SIZE, BLACK_KNIGHT);
                this.knightView.setStroke(WHITE_KNIGHT);
                this.knightView.setStrokeWidth(2);

                this.ballView = new Circle(CIRCLE_SIZE, BLACK_BALL);
                this.ballView.setStroke(Color.WHITE);
                this.ballView.setStrokeWidth(2);
                getChildren().addAll(knightView, ballView);
            }
        } else if (this.cell.getPiece().size() == 1) {
            Iterator<Piece> iterator = this.cell.getPiece().iterator();
            Piece knight = iterator.next();
            if (knight.getColor().equals("white")) {
                this.knightView = new Rectangle(RECT_SIZE, RECT_SIZE, WHITE_KNIGHT);
                this.knightView.setStroke(Color.BLACK);
                this.knightView.setStrokeWidth(2);

                this.ballView = new Circle(CIRCLE_SIZE, Color.TRANSPARENT);
                this.ballView.setStroke(Color.BLACK);
                this.ballView.setStrokeWidth(2);

                getChildren().addAll(knightView, ballView);
            } else if (knight.getColor().equals("black")) {
                this.knightView = new Rectangle(RECT_SIZE, RECT_SIZE, BLACK_KNIGHT);
                this.knightView.setStroke(Color.WHITE);
                this.knightView.setStrokeWidth(2);

                this.ballView = new Circle(CIRCLE_SIZE, Color.TRANSPARENT);
                this.ballView.setStroke(Color.WHITE);
                this.ballView.setStrokeWidth(2);

                getChildren().addAll(knightView, ballView);
            } else {
                this.knightView = new Rectangle(RECT_SIZE, RECT_SIZE, NONE_KNIGHT);
                this.knightView.setStroke(Color.TRANSPARENT);
                this.knightView.setStrokeWidth(2);


                getChildren().addAll(knightView);
            }

        }
    }



    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Rectangle getKnightView() {
        return knightView;
    }

    public void setKnightView(Rectangle knightView) {
        this.knightView = knightView;

    }

    public Circle getBallView() {
        return ballView;
    }

    public void setBallView(Circle ballView) {
        this.ballView = ballView;
    }
}