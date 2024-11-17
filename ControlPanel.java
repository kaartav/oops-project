import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ControlPanel extends JPanel {
    private CanvasPanel canvasPanel;

    public ControlPanel() {
        setLayout(new GridLayout(5, 1)); // Layout for buttons

        // Save button
        JButton saveButton = new JButton("Save Rooms");
        saveButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                canvasPanel.saveRoomsToFile(file);
            }
        });

        // Load button
        JButton loadButton = new JButton("Load Rooms");
        loadButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                canvasPanel.loadRoomsFromFile(file);
            }
        });

        // Add save and load buttons to the panel
        // add(saveButton);
        // add(loadButton);

        // Add buttons to add different room types
        JButton addBedroomButton = new JButton("Add Bedroom");
        JButton addBathroomButton = new JButton("Add Bathroom");
        JButton addKitchenButton = new JButton("Add Kitchen");
        JButton addLivingRoomButton = new JButton("Add Living Room");

        // Add action listeners for each button
        addBedroomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Room bedroom = new Room(100, 100, 100, 100, Color.GREEN, "Bedroom");
                canvasPanel.addRoom(bedroom);
            }
        });

        addBathroomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Room bathroom = new Room(100, 100, 80, 80, Color.PINK, "Bathroom");
                canvasPanel.addRoom(bathroom);
            }
        });

        addKitchenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Room kitchen = new Room(100, 100, 120, 100, Color.RED, "Kitchen");
                canvasPanel.addRoom(kitchen);
            }
        });

        addLivingRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Room livingRoom = new Room(100, 100, 140, 100, Color.YELLOW, "Living Room");
                canvasPanel.addRoom(livingRoom);
            }
        });

        // Add buttons to the panel
        add(addBedroomButton);
        add(addBathroomButton);
        add(addKitchenButton);
        add(addLivingRoomButton);

        // Add new buttons for relative positioning
        JButton addNorthButton = new JButton("Add Room (North)");
        JButton addSouthButton = new JButton("Add Room (South)");
        JButton addEastButton = new JButton("Add Room (East)");
        JButton addWestButton = new JButton("Add Room (West)");

        // Updated action listeners in ControlPanel
        addNorthButton.addActionListener(e -> {
            if (canvasPanel != null) {
                canvasPanel.addRoomRelative("North");
            }
        });

        addSouthButton.addActionListener(e -> {
            if (canvasPanel != null) {
                canvasPanel.addRoomRelative("South");
            }
        });

        addEastButton.addActionListener(e -> {
            if (canvasPanel != null) {
                canvasPanel.addRoomRelative("East");
            }
        });

        addWestButton.addActionListener(e -> {
            if (canvasPanel != null) {
                canvasPanel.addRoomRelative("West");
            }
        });

        // Add the new buttons to the panel
        add(addNorthButton);
        add(addSouthButton);
        add(addEastButton);
        add(addWestButton);

    }

    // Set the canvas panel so the control panel can interact with it
    public void setCanvasPanel(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;
    }
}
