import java.awt.*;
import java.io.Serializable;

public class Window implements Serializable {
    private int x, y;
    private static final long serialVersionUID = 1L;
    private int width, height;
    public int oldX, oldY;
    private Room room; // Associated room

    public Window(int x, int y, int width, int height, Room room) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.room = room;
        oldX = x;
        oldY = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 5, 5 }, 0));
        g2.drawRect(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void rotate() {
        int temp = this.width;
        this.width = this.height;
        this.height = temp;

        // Adjust position to maintain the center
        this.x = this.oldX + (this.oldX + this.oldY - (this.width - this.height)) / 2;
        this.y = this.oldY + (this.width - this.height) / 2;
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
