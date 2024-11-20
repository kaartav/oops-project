import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Furniture implements Serializable {
    private int x, y, width, height, rotation;;
    private BufferedImage image;
    private int oldX, oldY;

    public Furniture(String imagePath, int x, int y) {
        this.x = x;
        this.y = y;
        loadImage(imagePath);
    }

    private void loadImage(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading image: " + imagePath);
        }
    }

    public void draw(Graphics g) {
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g;
            AffineTransform transform = new AffineTransform();

            // Rotate around the center of the image
            int centerX = x + image.getWidth() / 2;
            int centerY = y + image.getHeight() / 2;
            transform.rotate(Math.toRadians(rotation), centerX, centerY);

            // Translate to the top-left corner after rotation
            transform.translate(x, y);
            g2d.drawImage(image, transform, null);
        }
    }

    public void rotate() {
        rotation = (rotation + 90) % 360; // Rotate 90 degrees clockwise
    }

    public Rectangle getBounds() {
        if (image != null) {
            return new Rectangle(x, y, image.getWidth(), image.getHeight());
        }
        return new Rectangle(x, y, 0, 0);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Get the old x and y positions for reverting
    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }

    public void saveCurrentPosition() {
        oldX = x;
        oldY = y;
    }
}