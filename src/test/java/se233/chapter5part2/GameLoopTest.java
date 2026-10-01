package se233.chapter5part2;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se233.chapter5part2.controller.GameLoop;
import se233.chapter5part2.model.Direction;
import se233.chapter5part2.model.Food;
import se233.chapter5part2.model.Snake;
import se233.chapter5part2.view.GameStage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GameLoopTest {
    private GameStage gameStage;
    private Snake snake;
    private Food food;
    private GameLoop gameLoop;

    @BeforeEach
    public void setUp() {
        gameStage = new GameStage();
        snake = new Snake(new Point2D(0, 0));
        food = new Food(new Point2D(0, 1));
        gameLoop = new GameLoop(gameStage, snake, food);
    }

    private void clockTickHelper() throws Exception {
        ReflectionHelper.invokeMethod(gameLoop, "keyProcess", new Class[0]);
        ReflectionHelper.invokeMethod(gameLoop, "checkCollision", new Class[0]);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class[0]);
    }

    @Test
    public void keyProcess_pressRight_snakeTurnRight() throws Exception {
        ReflectionHelper.setField(gameStage, "key", KeyCode.RIGHT);
        ReflectionHelper.setField(snake, "direction", Direction.DOWN);
        clockTickHelper();
        Direction currentDirection = (Direction) ReflectionHelper.getField(snake, "direction");
        assertEquals(Direction.RIGHT, currentDirection);
    }

    @Test
    public void collided_snakeEatFood_shouldGrow() throws Exception {
        clockTickHelper();
        assertTrue(snake.getLength() > 1);
        clockTickHelper();
        assertNotSame(food.getPosition(), new Point2D(0, 1));
    }

    @Test
    public void collided_snakeHitBorder_shouldDie() throws Exception {
        ReflectionHelper.setField(gameStage, "key", KeyCode.LEFT);
        clockTickHelper();
        Boolean running = (Boolean) ReflectionHelper.getField(gameLoop, "running");
        assertFalse(running);
    }

    @Test
    public void redraw_calledThreeTimes_snakeAndFoodShouldRenderThreeTimes() throws Exception {
        GameStage mockGameStage = Mockito.mock(GameStage.class);
        Snake mockSnake = Mockito.mock(Snake.class);
        Food mockFood = Mockito.mock(Food.class);
        GameLoop gameLoop = new GameLoop(mockGameStage, mockSnake, mockFood);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class[0]);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class[0]);
        ReflectionHelper.invokeMethod(gameLoop, "redraw", new Class[0]);
        verify(mockGameStage, times(3)).render(mockSnake, mockFood);
    }

    // ---- Part 2 / Task 1: Game Over popup ----
    @Test
    public void run_whenSnakeDies_shouldShowGameOverPopupOnce() {
        GameStage mockGameStage = Mockito.mock(GameStage.class); //ใช้ Mockito จำลองหน้าจอ GameStage ขึ้นมา โดยไม่ต้องเปิดหน้าต่าง UI จริง
        Mockito.when(mockGameStage.getKey()).thenReturn(KeyCode.LEFT); // (0,0) -> (-1,0) : dead ตั้งค่าจำลองว่าผู้เล่นกดปุ่มเลี้ยวซ้าย
        GameLoop loop = new GameLoop(mockGameStage, new Snake(new Point2D(0, 0)), new Food(new Point2D(9, 9))); //สร้างงูอยู่ที่จุด
        loop.run(); //รันเกมลูป พองูอยู่ (0,0) แล้วเลี้ยวซ้าย งูจะเดินหลุดขอบซ้ายไปที่ (-1,0) แล้วตายทันที ลูปจะหลุดออก
        verify(mockGameStage, times(1)).showGameOver(Mockito.anyInt()); //ตรวจสอบว่าคำสั่ง showGameOver บนหน้าจอถูกเรียกใช้งานขึ้นมา
    }

    @Test
    public void checkCollision_snakeAlive_shouldNotShowGameOverPopup() throws Exception {
        GameStage mockGameStage = Mockito.mock(GameStage.class);
        GameLoop loop = new GameLoop(mockGameStage, new Snake(new Point2D(5, 5)), new Food(new Point2D(9, 9)));
        ReflectionHelper.invokeMethod(loop, "keyProcess", new Class[0]);
        ReflectionHelper.invokeMethod(loop, "checkCollision", new Class[0]);
        verify(mockGameStage, Mockito.never()).showGameOver(Mockito.anyInt());
    }

    // ---- Part 2 / Task 2: scoring ----
    @Test
    public void score_atStart_shouldBeZero() {
        assertEquals(0, gameLoop.getScore());
    } //ตรวจสอบสถานะเริ่มต้นของเกมว่า คะแนนเริ่มต้น (getScore()) ต้องเป็น 0

    @Test
    public void collided_snakeEatNormalFood_scoreShouldIncreaseByOne() throws Exception {
        clockTickHelper();                 // snake (0,0)->(0,1) eats normal food at (0,1)
        assertEquals(1, gameLoop.getScore());
    }

    @Test
    public void collided_snakeMissFood_scoreShouldNotChange() throws Exception {
        food = new Food(new Point2D(10, 10));
        gameLoop = new GameLoop(gameStage, snake, food);
        clockTickHelper();
        assertEquals(0, gameLoop.getScore());
    }

    // ---- Part 2 / Task 3: special food scoring ----
    @Test
    public void collided_snakeEatSpecialFood_scoreShouldIncreaseByFive() throws Exception {
        food = new Food(new Point2D(0, 1), true); //วางอาหารพิเศษไว้ตรงหน้าของงูที่
        gameLoop = new GameLoop(gameStage, snake, food);
        clockTickHelper(); //สั่งงูเดิน 1 ก้าวด้วย clockTickHelper(); เพื่อกลืนอาหาร
        assertEquals(5, gameLoop.getScore()); //ตรวจสอบว่าแต้มรวมของเกมต้องเด้งขึ้นเป็น 5 คะแนนทันที
    }

    // ---- Part 2 / Task 4: no reversing ----
    @Test
    public void keyProcess_pressOppositeDirection_snakeKeepsCurrentDirection() throws Exception {
        ReflectionHelper.setField(snake, "direction", Direction.RIGHT);
        ReflectionHelper.setField(gameStage, "key", KeyCode.LEFT);
        ReflectionHelper.invokeMethod(gameLoop, "keyProcess", new Class[0]);
        assertEquals(Direction.RIGHT, snake.getDirection());
    }
}
