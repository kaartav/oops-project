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
        JButton saveButton = new JButton("Save file");
        saveButton.addActionListener((ActionEvent e) -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                canvasPanel.saveRoomsToFile(file);
            }
        });

        // Load button
        JButton loadButton = new JButton("Load files");
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

        customizeButton(addBedroomButton);
        customizeButton(addBathroomButton);

        customizeButton(addKitchenButton);

        customizeButton(addLivingRoomButton);

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

        customizeButton(addNorthButton);
        customizeButton(addSouthButton);
        customizeButton(addWestButton);
        customizeButton(addEastButton);

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

        // Add furniture buttons
        // Add action listeners for furniture buttons
        JButton addBedButton = new JButton("Add Bed");
        addBedButton.setPreferredSize(new Dimension(30, 30));
        addBedButton.addActionListener(e -> canvasPanel.addBed());
        customizeButton(addBedButton);

        add(addBedButton);

        JButton addSofaButton = new JButton("Add Sofa");
        addSofaButton.setPreferredSize(new Dimension(30, 30));
        addSofaButton.addActionListener(e -> canvasPanel.addSofa());
        customizeButton(addSofaButton);
        add(addSofaButton);

        JButton addTVButton = new JButton("Add TV");
        addTVButton.setPreferredSize(new Dimension(30, 30));
        addTVButton.addActionListener(e -> canvasPanel.addTV());
        customizeButton(addTVButton);

        add(addTVButton);

        JButton addTable = new JButton("Add Table");
        addTable.setPreferredSize(new Dimension(30, 30));
        addTable.addActionListener(e -> canvasPanel.addTable());
        customizeButton(addTable);
        add(addTable);

        JButton addChair = new JButton("Add Chair");
        addChair.setPreferredSize(new Dimension(30, 30));
        addChair.addActionListener(e -> canvasPanel.addChair());
        customizeButton(addChair);
        add(addChair);

        // Add Sink button
        JButton addSink = new JButton("Add Sink");
        addSink.addActionListener(e -> canvasPanel.addSink());
        customizeButton(addSink);
        add(addSink);
        JButton addCookingArea = new JButton("Add Cooking Area");
        addCookingArea.addActionListener(e -> canvasPanel.addCookingArea());
        customizeButton(addCookingArea);
        add(addCookingArea);

        JButton addShower = new JButton("Add Shower");
        addShower.addActionListener(e -> canvasPanel.addShower());
        customizeButton(addShower);
        add(addShower);

        JButton addComode = new JButton("Add Comode");
        addComode.addActionListener(e -> canvasPanel.addComode());
        customizeButton(addComode);
        add(addComode);

        JButton deleteButton = new JButton("Delete Selected");
        customizeButton(deleteButton);
        deleteButton.addActionListener(e -> {
            if (canvasPanel != null) {
                canvasPanel.deleteSelected();
            }
        });
        add(deleteButton);

        JButton addDoorButton = new JButton("Add Door");
        JButton addWindowButton = new JButton("Add Window");

        customizeButton(addWindowButton);
        customizeButton(addDoorButton);

        addDoorButton.addActionListener((ActionEvent e) -> {
            Room room1 = canvasPanel.SelectedRoomforRelativetedRoom(); // Room to add door
            Room room2 = null; // Second room or null for outside

            // Calculate position based on user input or default
            int x = room1.getX() + room1.getWidth() - 10; // Example position
            int y = room1.getY() + room1.getHeight() / 2 - 5;
            canvasPanel.addDoor(room1, room2, x, y, 10, 20);
        });

        addWindowButton.addActionListener((ActionEvent e) -> {
            Room room = canvasPanel.SelectedRoomforRelativetedRoom();
            int x = room.getX() + 10;
            int y = room.getY() - 10;
            canvasPanel.addWindow(room, x, y, 40, 5);
        });

        add(addDoorButton);
        add(addWindowButton);

    }

    private void customizeButton(JButton button) {
        // Set default background color to a dark shade
        button.setBackground(new Color(40, 40, 40)); // Dark gray background
        button.setOpaque(true);
        button.setForeground(Color.WHITE); // Set text color to white

        // Remove default button border and focus border for cleaner look
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        button.setFocusPainted(false); // Prevent focus outline from appearing when clicked

        // Set font for the button (you can adjust the size if needed)
        button.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));

        // Set the size of the button (optional, depending on your layout)
        button.setPreferredSize(new Dimension(100, 30));

        // Add mouse listener to implement hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                // Change the background color on hover to make the button darker
                button.setBackground(new Color(30, 30, 30)); // Slightly darker gray
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Revert to the original background color when hover ends
                button.setBackground(new Color(40, 40, 40)); // Original dark gray color
            }
        });
    }

    // Set the canvas panel so the control panel can interact with it
    public void setCanvasPanel(CanvasPanel canvasPanel) {
        this.canvasPanel = canvasPanel;
    }

}
