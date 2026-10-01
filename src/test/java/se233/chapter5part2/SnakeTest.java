package se233.chapter5part2;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se233.chapter5part2.model.Direction;
import se233.chapter5part2.model.Food;
import se233.chapter5part2.model.Snake;
import se233.chapter5part2.view.GameStage;

import static org.junit.jupiter.api.Assertions.*;

public class SnakeTest {
    private Snake snake;

    @BeforeEach
    public void setup() {
        snake = new Snake(new Point2D(0, 0));
    }

    @Test
    public void initialPosition_shouldBe_atOrigin() {
        assertEquals(snake.getHead(), new Point2D(0, 0));
    }

    @Test
    public void move_afterInitialized_headShouldBeInDownwardDirection() {
        snake.setDirection(Direction.DOWN);
        snake.move();
        assertEquals(snake.getHead(), new Point2D(0, 1));
    }

    @Test
    public void grow_shouldIncreaseLengthByOne() {
        snake.grow();
        assertEquals(snake.getLength(), 2);
    }

    @Test
    public void grow_shouldAddPreviousHeadToBody() {
        Point2D cur_head = snake.getHead();
        snake.move();
        snake.grow();
        assertTrue(snake.getBody().contains(cur_head));
    }

    @Test
    public void collided_withSnake_shouldBeDetected() {
        Food food = new Food(new Point2D(0, 0));
        assertTrue(snake.collided(food));
    }

    @Test
    public void checkDead_ifHitGameBorder_snakeWillDie() {
        snake = new Snake(new Point2D(30, 30));
        snake.setDirection(Direction.RIGHT);
        snake.move();
        assertTrue(snake.checkDead());
    }

    @Test
    public void checkDead_ifHitItself_snakeWillDie() {
        snake = new Snake(new Point2D(0, 0));
        snake.setDirection(Direction.DOWN);
        snake.move();
        snake.grow();
        snake.setDirection(Direction.LEFT);
        snake.move();
        snake.grow();
        snake.setDirection(Direction.UP);
        snake.move();
        snake.grow();
        snake.setDirection(Direction.RIGHT);
        snake.move();
        snake.grow();
        assertTrue(snake.checkDead());
    }

    // ---- Part 2 / Task 1: boundary defect (board tiles are 0..29) ----
    @Test
    public void checkDead_headOnLastTile_snakeStillAlive() {
        snake = new Snake(new Point2D(GameStage.WIDTH - 1, GameStage.HEIGHT - 1));
        assertFalse(snake.checkDead());
    }

    @Test
    public void checkDead_movePastRightEdge_snakeDiesImmediately() {
        snake = new Snake(new Point2D(GameStage.WIDTH - 1, 5));//สร้างงูวางไว้ที่พิกัด ซึ่งเป็นช่องสุดท้ายติดขอบขวา
        snake.setDirection(Direction.RIGHT); //สั่งให้หัวงูหันไปทางขวา
        snake.move();                       // head.x == 30, first tile outside
        assertTrue(snake.checkDead()); //ต้องส่งค่า true กลับมาทันที เพื่อยืนยันว่างูตาย
    }

    @Test
    public void checkDead_movePastBottomEdge_snakeDiesImmediately() {
        snake = new Snake(new Point2D(5, GameStage.HEIGHT - 1));
        snake.setDirection(Direction.DOWN);
        snake.move();                       // head.y == 30, first tile outside
        assertTrue(snake.checkDead());
    }

    @Test
    public void checkDead_movePastLeftEdge_snakeDiesImmediately() {
        snake = new Snake(new Point2D(0, 5));
        snake.setDirection(Direction.LEFT);
        snake.move();
        assertTrue(snake.checkDead());
    }

    @Test
    public void checkDead_movePastTopEdge_snakeDiesImmediately() {
        snake = new Snake(new Point2D(5, 0));
        snake.setDirection(Direction.RIGHT);
        snake.setDirection(Direction.UP);
        snake.move();
        assertTrue(snake.checkDead());
    }

    // ---- Part 2 / Task 4: snake must not reverse ----
    @Test
    public void setDirection_oppositeOfCurrent_shouldBeIgnored() {
        snake.setDirection(Direction.RIGHT); //กำหนดให้งูกำลังหันไปทางขวา
        snake.setDirection(Direction.LEFT); //สั่งให้เลี้ยวไปทางซ้ายทันที
        assertEquals(Direction.RIGHT, snake.getDirection()); //ยืนยันว่าคำสั่งเลี้ยวซ้ายถูกเมิน

        snake.setDirection(Direction.UP); //ครึ่งล่างทำซ้ำแบบเดียวกันกับทิศขึ้น
        snake.setDirection(Direction.DOWN); //แล้วพยายามสั่งลง
        assertEquals(Direction.UP, snake.getDirection()); //ก็ต้องยังคงเป็น
    }

    @Test
    public void setDirection_oppositeOfCurrent_headKeepsMovingForward() {
        snake = new Snake(new Point2D(5, 5));
        snake.setDirection(Direction.RIGHT);
        snake.setDirection(Direction.LEFT);
        snake.move();
        assertEquals(new Point2D(6, 5), snake.getHead());
    }

    @Test
    public void setDirection_perpendicularTurn_shouldBeAccepted() {
        snake.setDirection(Direction.RIGHT); //งูกำลังไปทางขวา
        snake.setDirection(Direction.UP); //สั่งเลี้ยวขึ้นด้านบน
        assertEquals(Direction.UP, snake.getDirection());
    }

    @Test
    public void setDirection_sameDirection_shouldBeAccepted() {
        snake.setDirection(Direction.DOWN);
        assertEquals(Direction.DOWN, snake.getDirection());
    }
}
