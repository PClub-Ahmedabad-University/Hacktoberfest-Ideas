package sprite;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.awt.Rectangle;

public class Ball {

    public float positionX;
    public float positionY;
    public float velocityX;
    public float velocityY;
    public int size;
    public int level;

    public Ball(float positionX, float positionY, float velocityX, float velocityY, int size) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.size = size;
        level = 1;
    }

    public void update(Bounds bounds) {
        if ((positionX + velocityX <= 0) || (size + positionX + velocityX >= bounds.getMaxX())) {
            velocityX *= -1;
        }
        if ((positionY + velocityY <= 0) || (size + positionY + velocityY >= bounds.getMaxY())) {
            velocityY *= -1;
        }

        positionX += velocityX;
        positionY += velocityY;
    }

    public void render(GraphicsContext gc) {
        Color c;
        switch (level) {
            case 1:
                c = Color.rgb(3, 247, 255);
                break;
            case 2:
                c = Color.rgb(60, 240, 195);
                break;
            case 3:
                c = Color.rgb(70, 224, 121);
                break;
            case 4:
                c = Color.rgb(224, 206, 70);
                break;
            default:
                c = Color.rgb(232, 98, 14);
                break;
        }
        gc.setFill(c);
        gc.fillOval(positionX, positionY, size, size);
    }

    public Rectangle getRectangle() {
        return new Rectangle((int) positionX, (int) positionY, size, size);
    }
}