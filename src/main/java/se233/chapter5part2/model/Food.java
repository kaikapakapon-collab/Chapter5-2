package se233.chapter5part2.model;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import se233.chapter5part2.view.GameStage;
import java.util.Random;

public class Food {
    public static final int NORMAL_POINTS = 1; //กำหนดแต้มคงที่ของอาหารธรรมดา = 1 แต้ม
    public static final int SPECIAL_POINTS = 5; //กำหนดแต้มคงที่ของอาหารพิเศษ = 5 แต้ม
    private static final double SPECIAL_CHANCE = 0.2; //ตั้งโอกาสเกิดอาหารพิเศษไว้ที่ 20% (0.2)

    private Point2D position;
    private Random rn;
    private boolean special; //ตัวแปรเก็บสถานะว่าอาหารชิ้นนี้เป็นแบบพิเศษหรือไม่

    public Food(Point2D position) {
        this(position, false);
    }
    public Food(Point2D position, boolean special) { //ตัวนี้สร้างขึ้นมาเพื่อให้ Unit Test กำหนดได้เอง ว่าอยากได้อาหารธรรมดาหรือพิเศษที่พิกัดไหน
        this.rn = new Random();
        this.position = position;
        this.special = special;
    }
    public Food() {
        this.rn = new Random();
        this.position = new Point2D(rn.nextInt(GameStage.WIDTH), rn.nextInt(GameStage.HEIGHT));
        this.special = false;
    }
    public void respawn() {
        Point2D prev_position = this.position;
        do {
            this.position = new Point2D(rn.nextInt(GameStage.WIDTH), rn.nextInt(GameStage.HEIGHT));
        } while (prev_position == this.position); //สุ่มพิกัดใหม่จนกว่าจะได้ตำแหน่งที่ไม่ซ้ำกับตำแหน่งเดิม
        this.special = rn.nextDouble() < SPECIAL_CHANCE; //สุ่มตัวเลข 0.0 ถึง 1.0 ถ้าได้ค่าน้อยกว่า 0.2 จะตั้งค่า special = true ทันที
    }
    public Point2D getPosition() {
        return position;
    }
    public boolean isSpecial() { return special; } //เมธอด Getter สำหรับเช็คสถานะอาหาร
    public int getPoints() { return special ? SPECIAL_POINTS : NORMAL_POINTS; } //ถ้า special == true จะคืนค่า 5 คะแนน ถ้าธรรมดาจะคืนค่า 1 คะแนน
    public Color getColor() { return special ? Color.GREEN : Color.RED; } // ถ้าเป็นอาหารพิเศษจะคืนค่าสีเขียว ถ้าธรรมดาคืนค่าสีแดง
}
