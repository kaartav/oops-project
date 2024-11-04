import javax.swing.*;
import java.awt.*;

public class FloorPlanner extends JFrame {
    private ControlPanel controlPanel;
    private CanvasPanel canvasPanel;

    public FloorPlanner() {
        // Initialize the control panel and canvas panel
        controlPanel = new ControlPanel();
        canvasPanel = new CanvasPanel();

        // Set up the main window layout
        setLayout(new BorderLayout());
        add(controlPanel, BorderLayout.WEST); // Control Panel on the left
        add(canvasPanel, BorderLayout.CENTER); // Canvas Panel in the center

        // Set frame properties
        setTitle("2D Floor Planner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        // Link control panel with canvas
        controlPanel.setCanvasPanel(canvasPanel);
    }

    public static void main(String[] args) {
        new FloorPlanner();
    }
}
