import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import sprite.Ball;
import sprite.Box;
import java.awt.Point;

public class Game extends Application {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static Ball ball;

    public static GraphicsContext gc;
    public static final int PLAYER_WIDTH = 150;
    public static final int PLAYER_HEIGHT = 8;

    public static final Random RAND = new Random();
    public static double playerPosX = WIDTH / 2 - PLAYER_WIDTH / 2;
    public static double playerPosY = 570;

    public static boolean gameOver = true;

    public Box boxes[][];
    public static int boxRows = 6;
    public static int boxCols = 8;
    public int boxHeight = 30;
    public int boxWidth = 80;

    public static int score = 0;
    public static int boxLeft = boxRows * boxCols;
    public static boolean gameWon = false;

    public static Canvas canvas;

    @Override
    public void start(Stage stage) throws Exception {
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> {
            if (!gameOver) {
                updateView();
            } else if (gameWon) {
                gc.setFill(Color.GREENYELLOW);
                gc.setFont(Font.font(46));
                gc.setTextAlign(TextAlignment.CENTER);
                gc.fillText("You won the game!\nCongrats", WIDTH / 2, HEIGHT / 2);
                score++;
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        setup();

        stage.setScene(new Scene(new StackPane(canvas)));
        stage.getScene().setFill(Color.BROWN);
        stage.show();
        stage.setTitle("B R I C K    B R E A K E R");
        stage.getScene().setOnKeyPressed(e -> {
            KeyCode c = e.getCode();
            if (c == KeyCode.RIGHT) {
                if (playerPosX + 5 + PLAYER_WIDTH > WIDTH) {
                    playerPosX = WIDTH - PLAYER_WIDTH;
                } else {
                    playerPosX += 5;
                }
            } else if (c == KeyCode.LEFT) {
                if (playerPosX - 5 < 0) {
                    playerPosX = 0;
                } else {
                    playerPosX -= 5;
                }
            }
        });

        stage.getScene().setOnMouseMoved(e -> {
            playerPosX = e.getX() - PLAYER_WIDTH / 2;
            if (playerPosX + PLAYER_WIDTH > WIDTH) {
                playerPosX = WIDTH - PLAYER_WIDTH;
            } else if (playerPosX < 0) {
                playerPosX = 0;
            }
        });

        gc.setFill(Color.grayRgb(20));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setStroke(Color.DARKSEAGREEN);
        gc.setFont(Font.font(36));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.strokeText("BRICK BREAKER\nClick to start", WIDTH / 2, HEIGHT / 2.5d);

        stage.getScene().setOnMouseClicked(e -> {
            if (gameOver) {
                gameOver = false;
                setup();
            }
        });

    }

    private void updateView() {
        // set background
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        ball.update(canvas.getBoundsInLocal());
        refreshBoxes();
        gc.setFill(Color.BEIGE);
        gc.fillRect(playerPosX, playerPosY, PLAYER_WIDTH, PLAYER_HEIGHT);
        // ball ka position [playerPosX, playerPosX + PLAYER_WIDTH] && [playerPosY]
        if (ball.positionX >= playerPosX && ball.positionX <= (playerPosX + PLAYER_WIDTH)
                && (ball.positionY + ball.size) >= playerPosY) {
            ball.velocityY *= -1;
        }
        ball.render(gc);
        drawBoxes();

        gc.setStroke(Color.CORNFLOWERBLUE);
        gc.setFont(Font.font(26));
        gc.strokeText("Score: " + score, 100, 50);

        if (boxLeft == 0) {
            gameWon = true;
            gameOver = true;
        }

        if (ball.positionY > 570) {
            gc.setFill(Color.RED);
            gc.setFont(Font.font(46));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("You lost the game!\nG A M E    O V E R\nYour Score: " + score, WIDTH / 2, HEIGHT / 2 - 70);
            gameOver = true;
            setup();
        }
    }

    private void refreshBoxes() {
        for (int i = 0; i < boxRows; i++) {
            for (int j = 0; j < boxCols; j++) {
                if (boxes[i][j] != null) {
                    if (isColliding(boxes[i][j])) {
                        score++;
                        boxes[i][j].level--;
                        if (boxes[i][j].level == 0) {
                            boxes[i][j] = null;
                            boxLeft--;
                        }
                    }
                }
            }
        }
    }

    private boolean isColliding(Box box) {
        boolean collision = false;

        Rectangle ballRect = ball.getRectangle();
        Rectangle boxRect = box.getRectangle();

        int ballLeft = (int) ballRect.getMinX();
        int ballTop = (int) ballRect.getMinY();

        Point pointRight = new Point(ballLeft + ball.size + 1, ballTop);
        Point pointLeft = new Point(ballLeft - 1, ballTop);
        Point pointTop = new Point(ballLeft, ballTop - 1);
        Point pointBottom = new Point(ballLeft, ballTop + ball.size + 1);

        if (boxRect.contains(pointRight) || boxRect.contains(pointLeft)) {
            ball.velocityX *= -2.00;
            collision = true;
        }

        if (boxRect.contains(pointTop) || boxRect.contains(pointBottom)) {
            ball.velocityY *= -2.00;
            collision = true;
        }

        return collision;
    }

    private void drawBoxes() {
        for (int i = 0; i < boxRows; i++) {
            for (int j = 0; j < boxCols; j++) {
                if (boxes[i][j] != null) {
                    boxes[i][j].render(gc);
                }
            }
        }
    }

    private void setup() {
        ball = new Ball(WIDTH / 2, 400, ((RAND.nextInt(2) == 0) ? 1 : -1) * RAND.nextInt(3) + 1,
                ((RAND.nextInt(2) == 0) ? 1 : -1) * RAND.nextInt(3) + 1, 20);

        if (Math.abs(ball.velocityX) < 0.1d) {
            ball.velocityX = 1;
        }
        if (Math.abs(ball.velocityY) < 0.1d) {
            ball.velocityY = 1;
        }
        boxes = new Box[boxRows][boxCols];

        score = 0;
        boxLeft = boxRows * boxCols;
        gameWon = false;

        int lastPosX = 80;
        int lastPosY = 50 + boxRows * boxHeight;

        for (int i = 0; i < boxRows; i++) {
            for (int j = 0; j < boxCols; j++) {
                boxes[i][j] = new Box(lastPosX, lastPosY, boxWidth, boxHeight);
                boxes[i][j].level = Math.min(4, i + 1);
                lastPosX += boxWidth;
            }
            lastPosY -= boxHeight;
            lastPosX = 80;
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}
