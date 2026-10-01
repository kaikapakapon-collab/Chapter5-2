package se233.chapter5part2.model;

import javafx.geometry.Point2D;
import se233.chapter5part2.view.GameStage;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    private Direction direction;
    private Point2D head;
    private Point2D prev_tail;
    private List body;

    public Snake(Point2D position) {
        direction = Direction.DOWN;
        body = new ArrayList<>();
        this.head = position;
        this.body.add(this.head);
    }

    public void move() {
        head = head.add(direction.current);
        prev_tail = (Point2D) body.remove(body.size()- 1);
        body.add(0, head);
    }

    public boolean checkDead() { //ประกาศเมธอดเช็คว่างูตายหรือยัง
        boolean isOutOfBound = head.getX() < 0 || head.getY() < 0 || head.getX() >= GameStage.WIDTH || head.getY() >= GameStage.HEIGHT; //เช็คว่าหัวงูหลุดออกนอกจอฝั่งซ้ายหรือฝั่งบนหรือไม่
        boolean isHitBody = body.lastIndexOf(head) > 0; //เช็คว่าตำแหน่งของหัวไปซ้ำกับชิ้นส่วนลำตัวหรือไม่ ถ้า index มากว่า 0 แปลว่างูกัดตัวเอง
        return isOutOfBound || isHitBody; // ส่งค่ากลับ ถ้าหลุดขอบจอ หรือชนตัวตัวเอง อย่างใดอย่างหนึ่งเป็นจริง จะถือว่างูตายทันที
    }

    /** Changes direction, but ignores a request to reverse (e.g. RIGHT -> LEFT). */
    public void setDirection(Direction direction) { //เมธอดเปลี่ยนทิศทางของงู
        if (direction == null || direction == this.direction.opposite()) return; //ตรวจสอบว่าถ้าทิศใหม่ที่ส่งเข้ามาคือทิศตรงข้ามของทิศปัจจุบัน
        this.direction = direction; //ถ้าไม่ใช่ทิศตรงข้าม จึงจะยอมอัปเดตทิศทางใหม่งู
    }
    public Direction getDirection() { return this.direction; }
    public Point2D getHead() { return head; }
    public boolean collided(Food food) { return head.equals(food.getPosition()); }
    public void grow() { body.add(prev_tail); }
    public int getLength() { return body.size(); }
    public List getBody() { return body; }
}