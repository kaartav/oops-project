import java.awt.*;
import java.io.Serializable;

public class Doors implements Serializable {
    private int x, y;
    private static final long serialVersionUID = 1L;
    private int width, height;
    private int oldX, oldY;
    private Room room1, room2; // Rooms it connects, null if to the outside

    public Doors(int x, int y, int width, int height, Room room1, Room room2) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.room1 = room1;
        this.room2 = room2;
        oldX = x;
        oldY = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
    }

    public void rotate() {
        int temp = this.width;
        this.width = this.height;
        this.height = temp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

}
