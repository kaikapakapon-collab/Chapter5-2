package se233.chapter5part2.view;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.geometry.Point2D;
import se233.chapter5part2.model.Food;
import se233.chapter5part2.model.Snake;

public class GameStage extends Pane {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    public static final int TILE_SIZE = 10;
    private Canvas canvas;
    private KeyCode key;
    private int score;

    public GameStage() {
        this.setHeight(TILE_SIZE * HEIGHT);
        this.setWidth(TILE_SIZE * WIDTH);
        canvas = new Canvas(TILE_SIZE * WIDTH, TILE_SIZE * HEIGHT);
        this.getChildren().add(canvas);
    }

    public void render(Snake snake, Food food) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0,WIDTH*TILE_SIZE,HEIGHT*TILE_SIZE);
        gc.setFill(Color.BLUE);
        snake.getBody().forEach(p-> {
            gc.fillRect(((Point2D)p).getX() * TILE_SIZE, ((Point2D)p).getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        });
        gc.setFill(food.getColor());   // red = normal, green = special
        gc.fillRect(food.getPosition().getX() * TILE_SIZE, food.getPosition().getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + score, 5, 12);
    }

    /** Shows a "Game Over" popup. Safe to call from the game-loop thread. */
    public void showGameOver(int finalScore) {
        Platform.runLater(() -> { //ส่งคำสั่งไปรันบน JavaFX
            Alert alert = new Alert(Alert.AlertType.INFORMATION); //สร้างหน้าต่างแจ้งเตือนแบบ Information ขึ้นมา
            if (getScene() != null && getScene().getWindow() != null) { //กำหนดให้ Popup นี้ผูกอยู่กับหน้าต่างหลักของเกม เพื่อให้อยู่กล่งจอ
                alert.initOwner(getScene().getWindow());
            }
            alert.setTitle("Game Over"); //ตั้งชื่อแถบ Title บาร์ของหน้าต่างว่า "Game Over"
            alert.setHeaderText("Game Over"); //ตั้งหัวข้อใหญ่ด้านในหน้าต่าง
            alert.setContentText("Your score: " + finalScore + "\nPress OK, then R to restart.");
            alert.show(); //สั่งให้แสดงหน้าต่าง Popup ขึ้นมาบนจอ
        });
    }

    public KeyCode getKey() { return key; }
    public void setKey(KeyCode key) { this.key = key; }
    public void setScore(int score) { this.score = score; }
}
