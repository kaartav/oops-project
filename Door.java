import java.awt.*;
public class Door {
    private int x, y, width, height;

    public Door(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);  // Draw door as solid rectangle
        g.fillRect(x, y, width, height);
    }
}