import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class CanvasPanel extends JPanel {
    private static final int GRID_SIZE = 20; // Grid size for snapping
    private ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Window> windows = new ArrayList<>();
    // private ArrayList<Door> doors = new ArrayList<>();
    private Room selectedRoom = null; // Room being dragged
    private int offsetX, offsetY; // Offset for smooth dragging
    public Room selectedRoomForRelativePos = null;

    public CanvasPanel() {
        setBackground(Color.LIGHT_GRAY);

        // Mouse listener to handle dragging
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Check if a room is clicked
                if (e.isShiftDown()) {
                    // Shift+Click: Handle special case
                    selectedRoomForRelativePos = getRoomAt(e.getX(), e.getY());
                }
                selectedRoom = getRoomAt(e.getX(), e.getY());
                if (selectedRoom != null) {
                    // Store the offset between the mouse click and room's top-left corner
                    offsetX = e.getX() - selectedRoom.getX();
                    offsetY = e.getY() - selectedRoom.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // When mouse is released, check if the new position overlaps
                if (selectedRoom != null) {
                    snapToGrid(selectedRoom); // Snap room to the grid
                    if (checkOverlap(selectedRoom)) {
                        JOptionPane.showMessageDialog(CanvasPanel.this, "Room overlap detected!");
                        // If overlap, return the room to its original position
                        selectedRoom.setX(selectedRoom.getOldX());
                        selectedRoom.setY(selectedRoom.getOldY());
                    }
                    selectedRoom = null; // Deselect the room
                    repaint(); // Repaint the canvas
                }
            }
        };

        addMouseListener(mouseAdapter);
        // Mouse motion listener to handle dragging
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedRoom != null) {
                    // Update room position based on mouse movement
                    selectedRoom.setX(e.getX() - offsetX);
                    selectedRoom.setY(e.getY() - offsetY);
                    repaint(); // Redraw the canvas with the room in the new location
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // Not used, but required by MouseMotionListener interface
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g); // Draw the grid first

        // Draw the rooms
        for (Room room : rooms) {
            room.draw(g); // Draw each room on the canvas
        }

        // Draw windows
        for (Window window : windows) {
            window.draw(g);
        }

    }

    // // Helper method to check if a location is occupied by any room
    // private boolean isOccupied(int x, int y) {
    // for (Room room : rooms) {
    // if (room.getX() == x && room.getY() == y) {
    // return true;
    // }
    // }
    // return false;
    // }

    // Add a new room at a default location
    public void addRoom(Room newRoom) {
        // int iterations = 0;
        int DEF_X = 50;
        int x = 100;
        int y = 100;

        // Set the calculated position for the new room

        while (true) {
            if (!checkOverlap(newRoom)) {
                rooms.add(newRoom);
                repaint(); // Redraw the canvas
                break;
            } else {
                x += DEF_X;
                newRoom.setX(x);
                snapToGrid(newRoom);
            }
            if (x > 1200 && y != 200) {
                x = 100;
                y = 200;
                newRoom.setY(y);
                newRoom.setX(x);
            }
        }

    }

    // Snap a room to the grid
    private void snapToGrid(Room room) {
        int newX = Math.round(room.getX() / GRID_SIZE) * GRID_SIZE;
        int newY = Math.round(room.getY() / GRID_SIZE) * GRID_SIZE;
        room.setX(newX);
        room.setY(newY);
    }

    // Get the room at a specific x, y position
    private Room getRoomAt(int x, int y) {
        for (Room room : rooms) {
            if (room.getBounds().contains(x, y)) {
                room.saveCurrentPosition(); // Save the original position in case of overlap
                return room;
            }
        }
        return null;
    }

    private boolean checkOverlap(Room newRoom) {
        for (Room room : rooms) {
            if (room != newRoom && newRoom.getBounds().intersects(room.getBounds())) {
                return true; // Overlap detected
            }
        }
        return false;
    }

    // Draw the grid
    private void drawGrid(Graphics g) {
        g.setColor(Color.GRAY);
        for (int i = 0; i < getWidth(); i += GRID_SIZE) {
            for (int j = 0; j < getHeight(); j += GRID_SIZE) {
                g.drawRect(i, j, GRID_SIZE, GRID_SIZE);
            }
        }
    }

    // Add a window to the room
    public void addWindow(Window window) {
        windows.add(window);
        repaint();
    }

    public void saveRoomsToFile(File file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(rooms);
            JOptionPane.showMessageDialog(this, "Rooms saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save rooms: " + e.getMessage());
        }
    }

    // Method to load rooms from a file
    @SuppressWarnings("unchecked")
    public void loadRoomsFromFile(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            rooms = (ArrayList<Room>) in.readObject();
            repaint(); // Redraw the canvas to show loaded rooms
            JOptionPane.showMessageDialog(this, "Rooms loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Failed to load rooms: " + e.getMessage());
        }
    }

    private String selectRoomType() {
        String[] roomTypes = { "Bedroom", "Bathroom", "Kitchen", "Living Room" };
        int selection = JOptionPane.showOptionDialog(
                this,
                "Select a room type:",
                "Room Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                roomTypes,
                roomTypes[0]);

        if (selection != -1) {
            return roomTypes[selection];
        } else {
            return null; // User cancelled the selection
        }
    }

    public void addRoomRelative(String direction) {
        if (selectedRoomForRelativePos == null) {
            JOptionPane.showMessageDialog(this, "Please select a room first.");
            return;
        }

        // Prompt the user to select a room type
        String roomType = selectRoomType();
        if (roomType == null) {
            return; // User canceled the selection
        }

        // Define the room properties based on the selected room type
        Room newRoom = null;
        switch (roomType) {
            case "Bedroom":
                newRoom = new Room(0, 0, 100, 100, Color.GREEN, "Bedroom");
                break;
            case "Bathroom":
                newRoom = new Room(0, 0, 80, 80, Color.PINK, "Bathroom");
                break;
            case "Kitchen":
                newRoom = new Room(0, 0, 120, 100, Color.RED, "Kitchen");
                break;
            case "Living Room":
                newRoom = new Room(0, 0, 140, 100, Color.YELLOW, "Living Room");
                break;
        }

        switch (direction) {
            case "North":
                newRoom.setX(selectedRoomForRelativePos.getX());
                newRoom.setY(selectedRoomForRelativePos.getY() - selectedRoomForRelativePos.getHeight());
                break;
            case "South":
                newRoom.setX(selectedRoomForRelativePos.getX());
                newRoom.setY(selectedRoomForRelativePos.getY() + selectedRoomForRelativePos.getHeight());
                break;
            case "East":
                newRoom.setX(selectedRoomForRelativePos.getX() + selectedRoomForRelativePos.getWidth());
                newRoom.setY(selectedRoomForRelativePos.getY());
                break;
            case "West":
                newRoom.setX(selectedRoomForRelativePos.getX() - newRoom.getWidth());
                newRoom.setY(selectedRoomForRelativePos.getY());
                break;
        }

        snapToGrid(newRoom);

        if (!checkOverlap(newRoom)) {
            rooms.add(newRoom);
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Cannot place room here. Overlap detected.");
        }
    }

    // public void alignRoom(String alignment) {
    // if (selectedRoomForRelativePos == null) {
    // JOptionPane.showMessageDialog(this, "Please select a room first.");
    // return;
    // }

    // switch (alignment) {
    // case "Left":
    // selectedRoomForRelativePos.setX(GRID_SIZE);
    // break;
    // case "Right":
    // selectedRoomForRelativePos.setX(getWidth() - selectedRoom.getWidth() -
    // GRID_SIZE);
    // break;
    // case "Center":
    // selectedRoomForRelativePos.setX((getWidth() - selectedRoom.getWidth()) / 2);
    // break;
    // }

    // snapToGrid(selectedRoomForRelativePos);
    // repaint();
    // }

}
