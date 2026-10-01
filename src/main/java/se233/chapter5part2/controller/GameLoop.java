package se233.chapter5part2.controller;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import se233.chapter5part2.model.Direction;
import se233.chapter5part2.model.Food;
import se233.chapter5part2.model.Snake;
import se233.chapter5part2.view.GameStage;

public class GameLoop implements Runnable {
    private GameStage gameStage;
    private Snake snake;
    private Food food;
    private float interval = 1000.0f / 10;
    private volatile boolean running;
    private int score;

    public GameLoop(GameStage gameStage, Snake snake, Food food){
        this.snake = snake;
        this.gameStage = gameStage;
        this.food = food;
        running = true;
        score = 0;
    }

    private void keyProcess(){
        KeyCode curKey = gameStage.getKey();
        // Snake.setDirection() itself refuses to reverse the snake.
        if (curKey == KeyCode.UP)
            snake.setDirection(Direction.UP);
        else if (curKey == KeyCode.DOWN)
            snake.setDirection(Direction.DOWN);
        else if (curKey == KeyCode.LEFT)
            snake.setDirection(Direction.LEFT);
        else if (curKey == KeyCode.RIGHT)
            snake.setDirection(Direction.RIGHT);
        snake.move();
    }

    private void checkCollision(){
        if (snake.collided(food)){ //เช็คว่าตำแหน่งหัวงูทับกับตำแหน่งอาหารหรือไม่
            score += food.getPoints();   // บวกคะแนนเพิ่มตามค่าของอาหารนั้นๆ
            snake.grow(); //สั่งให้ลำตัวงูยาวขึ้น 1 ช่อง
            food.respawn(); //สั่งให้อาหารสุ่มไปเกิดที่พิกัดใหม่
        }
        if (snake.checkDead()){ running = false; } //ถ้าเช็คแล้วงูตาย ให้ปรับ running = false เพื่อหยุดลูปเกม
    }

    private void redraw(){
        gameStage.setScore(score); //ส่งคะแนนปัจจุบันไปให้คลาส View
        gameStage.render(snake, food); //สั่งให้วาดภาพตัวงู อาหาร และข้อความ Score บน Canvas ใหม่
    }

    public void restart() {
        if (!running) {
            this.snake = new Snake(new Point2D(GameStage.WIDTH / 2, GameStage.HEIGHT / 2));
            this.food = new Food();
            this.score = 0;
            this.running = true;
            new Thread(this).start();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void run(){
        while (running){ //ลูปการทำงานของเกม ถ้ายังไม่ตายจะประมวลผลปุ่ม ชนอาหาร และวาดหน้าจอวนไปเรื่อยๆ
            keyProcess();
            checkCollision();
            redraw();
            try{
                Thread.sleep((long)interval);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        gameStage.showGameOver(score);   // ทันทีที่ running กลายเป็น false เพื่อสั่งเปิด Popup ทันทีที่เกมจบ
    }
}
