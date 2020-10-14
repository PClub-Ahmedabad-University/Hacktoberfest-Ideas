package sprite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.awt.Rectangle;

public class Box {

    public float positionX;
    public float positionY;
    public int height;
    public int width;
    public int level;

    /*
     * 1 - 72, 252, 56 2 - 196, 56, 252 3 - 242, 29, 111 4 - 232, 26, 26
     */

    public Box(float positionX, float positionY, int width, int height) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        level = 1;
    }

    public void update(boolean hit) {
        this.level--;
    }

    public void render(GraphicsContext gc) {
        if (level > 0) {
            Color c;
            switch (level) {
                case 1:
                    c = Color.rgb(72, 252, 56);
                    break;
                case 2:
                    c = Color.rgb(196, 56, 252);
                    break;
                case 3:
                    c = Color.rgb(242, 29, 111);
                    break;
                case 4:
                    c = Color.rgb(232, 26, 26);
                    break;
                default:
                    c = Color.rgb(145, 137, 137);
                    break;
            }
            gc.setFill(c);
            gc.fillRoundRect(positionX + 4, positionY + 4, width - 8, height - 8, 4, 4);
        }
    }

    public Rectangle getRectangle() {
        return new Rectangle((int) positionX, (int) positionY, width, height);
    }
}