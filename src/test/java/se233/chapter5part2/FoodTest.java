package se233.chapter5part2;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se233.chapter5part2.model.Food;

import javafx.scene.paint.Color;

import static org.junit.jupiter.api.Assertions.*;

public class FoodTest {
    private Food food;

    @BeforeEach
    public void setup() {
        food = new Food(new Point2D(0, 0));
    }

    @Test
    public void respawn_shouldBeOnNewPosition() {
        food.respawn();
        assertNotSame(food.getPosition(), new Point2D(0, 0));
    }

    // ---- Part 2 / Task 3: special food ----
    @Test
    public void normalFood_shouldBeRedAndWorthOnePoint() {
        assertFalse(food.isSpecial());
        assertEquals(Color.RED, food.getColor());
        assertEquals(1, food.getPoints());
    }

    @Test
    public void specialFood_shouldBeGreen() {
        Food special = new Food(new Point2D(0, 0), true); //สร้างอาหารพิเศษด้วย constructor แบบระบุ special = true
        assertTrue(special.isSpecial()); //ยืนยันว่าสถานะเป็นอาหารพิเศษ
        assertEquals(Color.GREEN, special.getColor()); //ตรวจสอบว่าสีที่ส่งออกมาต้องเป็นสีเขียว
    }

    @Test
    public void specialFood_shouldBeWorthFivePoints() {
        Food special = new Food(new Point2D(0, 0), true);
        assertEquals(5, special.getPoints());
    } //ตรวจสอบว่าอาหารพิเศษต้องมีแต้มเท่ากับ 5 แต้ม

    @Test
    public void respawn_manyTimes_shouldProduceBothNormalAndSpecialFood() {
        boolean sawSpecial = false, sawNormal = false;
        for (int i = 0; i < 1000; i++) {
            food.respawn();
            if (food.isSpecial()) sawSpecial = true; else sawNormal = true;
        }
        assertTrue(sawSpecial);
        assertTrue(sawNormal);
    }
}
