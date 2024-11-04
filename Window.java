import java.awt.*;

public class Window {
    private int x, y, width, height;

    public Window(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);  // Draw window as dashed line
        Graphics2D g2d = (Graphics2D) g;
        float[] dash = {5f, 5f};
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1f, dash, 0f));
        g2d.drawRect(x, y, width, height);
    }}