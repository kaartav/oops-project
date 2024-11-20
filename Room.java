import java.awt.*;
import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L; // For version control in serialization
    private int x, y, width, height;
    private Color color;
    private String roomType;

    private int oldX, oldY; // Old position for reverting in case of overlap

    public Room(int x, int y, int width, int height, Color color, String roomType) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
        this.roomType = roomType;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height); // Draw the room

        g.setColor(Color.BLACK); // Draw the room's border (walls)
        g.drawRect(x, y, width, height);

        g.setColor(Color.BLACK); // Label the room type
        g.drawString(roomType, x + width / 4, y + height / 2);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getType() {
        return roomType;
    }

    // Save the current position before dragging
    public void saveCurrentPosition() {
        oldX = x;
        oldY = y;
    }

    // Get the old x and y positions for reverting
    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}
