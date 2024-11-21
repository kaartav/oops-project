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
    // private ArrayList<Door> doors = new ArrayList<>();
    private Room selectedRoom = null; // Room being dragged
    private int offsetX, offsetY; // Offset for smooth dragging
    public Room selectedRoomForRelativePos = null;

    private int furnitureOffsetX, furnitureOffsetY;
    private ArrayList<Furniture> furnitureItems = new ArrayList<>();
    private Furniture selectedFurniture = null;
    private Furniture selectedFurnitureforUsing = null;

    private ArrayList<Doors> doors = new ArrayList<>();
    private ArrayList<Window> windows = new ArrayList<>();
    private Doors selectedDoor = null;
    private Window selectedWindow = null;
    private int doorOffsetX, doorOffsetY;
    private int windowOffsetX, windowOffsetY;

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
                    selectedFurnitureforUsing = getFurnitureAt(e.getX(), e.getY());

                }
                if (SwingUtilities.isRightMouseButton(e)) {

                    if (!(getFurnitureAt(e.getX(), e.getY()) == null)) {
                        rotateFurnitureAt(e.getX(), e.getY());
                    } else if (!(getDoorAt(e.getX(), e.getY()) == null)) {
                        rotateDoorAt(e.getX(), e.getY());
                    } else if (!(getWindowAt(e.getX(), e.getY()) == null)) {
                        rotateWindowsAt(e.getX(), e.getY());
                    }

                }
                selectedRoom = getRoomAt(e.getX(), e.getY());
                selectedFurniture = getFurnitureAt(e.getX(), e.getY());
                selectedDoor = getDoorAt(e.getX(), e.getY());
                selectedWindow = getWindowAt(e.getX(), e.getY());

                if (selectedDoor != null) {
                    doorOffsetX = e.getX() - selectedDoor.getX();
                    doorOffsetY = e.getY() - selectedDoor.getY();
                } else if (selectedWindow != null) {
                    windowOffsetX = e.getX() - selectedWindow.getX();
                    windowOffsetY = e.getY() - selectedWindow.getY();
                } else if (selectedFurniture != null) {
                    furnitureOffsetX = e.getX() - selectedFurniture.getX();
                    furnitureOffsetY = e.getY() - selectedFurniture.getY();
                } else if (selectedRoom != null) {
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
                } else if (selectedFurniture != null) {

                    if (checkOverlapFurniture(selectedFurniture)) {
                        JOptionPane.showMessageDialog(CanvasPanel.this, "furniture overlap detected!");
                        // If overlap, return the room to its original position
                        selectedFurniture.setX(selectedFurniture.getOldX());
                        selectedFurniture.setY(selectedFurniture.getOldY());
                    }
                    selectedFurniture = null; // Deselect the room
                    repaint();
                } else if (selectedWindow != null) {
                    if (checkOverlapWindowswithdoors(selectedWindow)) {
                        JOptionPane.showMessageDialog(CanvasPanel.this, "Window overlap detected!");
                        selectedWindow.setX(selectedWindow.getOldX());
                        selectedWindow.setY(selectedWindow.getOldY());
                    }
                    selectedWindow = null; // Deselect the window
                    repaint();
                } else if (selectedDoor != null) {
                    if (checkOverlapDoorswithwindows(selectedDoor)) {
                        JOptionPane.showMessageDialog(CanvasPanel.this, "Door overlap detected!");
                        selectedDoor.setX(selectedDoor.getOldX());
                        selectedDoor.setY(selectedDoor.getOldY());
                    }
                    selectedDoor = null; // Deselect the door
                    repaint();
                }
            }
        };

        addMouseListener(mouseAdapter);
        // Mouse motion listener to handle dragging
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedDoor != null) {
                    selectedDoor.setX(e.getX() - doorOffsetX);
                    selectedDoor.setY(e.getY() - doorOffsetY);
                    repaint();
                } else if (selectedWindow != null) {
                    selectedWindow.setX(e.getX() - windowOffsetX);
                    selectedWindow.setY(e.getY() - windowOffsetY);
                    repaint();
                } else if (selectedFurniture != null) {
                    selectedFurniture.setX(e.getX() - furnitureOffsetX);
                    selectedFurniture.setY(e.getY() - furnitureOffsetY);
                    repaint();
                } else if (selectedRoom != null) {
                    selectedRoom.setX(e.getX() - offsetX);
                    selectedRoom.setY(e.getY() - offsetY);
                    repaint();
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
        for (Furniture furniture : furnitureItems) {
            furniture.draw(g);
        }
        for (Doors door : doors) {
            door.draw(g);
        }
        for (Window window : windows) {
            window.draw(g);
        }

    }

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

    private Furniture getFurnitureAt(int x, int y) {
        for (Furniture Furniture : furnitureItems) {
            if (Furniture.getBounds().contains(x, y)) {
                Furniture.saveCurrentPosition(); // Save the original position in case of overlap
                return Furniture;
            }
        }
        return null;
    }

    // Get the window at a specific x, y position
    private Window getWindowAt(int x, int y) {
        for (Window window : windows) {
            if (new Rectangle(window.getX(), window.getY(), window.getWidth(), window.getHeight())
                    .contains(x, y)) {
                // window.saveCurrentPosition(); // Save the original position in case of
                // overlap
                return window;
            }
        }
        return null;
    }

    // Get the door at a specific x, y position
    private Doors getDoorAt(int x, int y) {
        for (Doors door : doors) {
            if (new Rectangle(door.getX(), door.getY(), door.getWidth(), door.getHeight())
                    .contains(x, y)) {
                // door.saveCurrentPosition(); // Save the original position in case of overlap
                return door;
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

    private boolean checkOverlapFurniture(Furniture newFurniture) {
        for (Furniture furniture : furnitureItems) {
            if (furniture != newFurniture && newFurniture.getBounds().intersects(furniture.getBounds())) {
                return true; // Overlap detected
            }
        }

        return false;
    }

    private boolean checkOverlapWindows(int x, int y, int width, int height) {
        for (Window window : windows) {
            if (new Rectangle(window.getX(), window.getY(), window.getWidth(),
                    window.getHeight())
                    .intersects(new Rectangle(x, y, width, height))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkOverlapWindowswithdoors(Window newWindow) {
        for (Doors door : doors) {
            if (newWindow.getBounds().intersects(door.getBounds())) {
                return true; // Overlap detected
            }
        }
        return false; // No overlap
    }

    private boolean checkOverlapDoorswithwindows(Doors door) {
        for (Window window : windows) {
            if (window.getBounds().intersects(door.getBounds())) {
                return true; // Overlap detected
            }
        }
        return false; // No overlap
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
            // Save rooms first
            out.writeObject(rooms);

            // Save furniture items, doors, and windows separately
            out.writeObject(furnitureItems);
            out.writeObject(doors);
            out.writeObject(windows);

            JOptionPane.showMessageDialog(this, "File saved successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save file: " + e.getMessage());
        }
    }

    // Method to load rooms from a file
    @SuppressWarnings("unchecked")
    public void loadRoomsFromFile(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            rooms = (ArrayList<Room>) in.readObject();
            furnitureItems = (ArrayList<Furniture>) in.readObject();
            doors = (ArrayList<Doors>) in.readObject();
            windows = (ArrayList<Window>) in.readObject();
            repaint(); // Redraw the canvas to show loaded rooms
            JOptionPane.showMessageDialog(this, "Rooms loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Failed to load file: " + e.getMessage());
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

    public void addFurniture(Furniture furniture) {

        furnitureItems.add(furniture);
        repaint();

    }

    // Add furniture based on button click in ControlPanel
    public void addBed() {
        Furniture bed = new Furniture("C:\\Users\\kaartav\\Desktop\\oops\\bed.png", 15, 15); // Update the path as
                                                                                             // necessary
        addFurniture(bed);
    }

    public void addTable() {
        Furniture bed = new Furniture("C:\\Users\\kaartav\\Desktop\\oops\\table.png", 15, 15); // Update the path as
                                                                                               // necessary
        addFurniture(bed);
    }

    public void addChair() {
        Furniture bed = new Furniture("C:\\Users\\kaartav\\Desktop\\oops\\chair.png", 15, 15); // Update the path as
                                                                                               // necessary
        addFurniture(bed);
    }

    public void addSofa() {
        Furniture sofa = new Furniture("C:\\Users\\kaartav\\Desktop\\oops\\sofa.png", 15, 15); // Update the path as
                                                                                               // necessary
        addFurniture(sofa);
    }

    public void addTV() {
        Furniture tv = new Furniture("C:\\Users\\kaartav\\Desktop\\oops\\TV.png", 20, 20); // Update the path as
                                                                                           // necessary
        addFurniture(tv);
    }

    public void addSink() {
        Furniture sink = new Furniture("C:\\Users\\kaartav\\Desktop\\oops\\sink.png", 10, 10); // Update the path as
                                                                                               // necessary
        addFurniture(sink);
    }

    public void addCookingArea() {
        Furniture cookingArea = new Furniture("C:\\Users\\kaartav\\Desktop\\oops\\cookingArea.png", 20, 20); // Update
                                                                                                             // the path
                                                                                                             // as
                                                                                                             // necessary
        addFurniture(cookingArea);
    }

    public void addShower() {
        Furniture shower = new Furniture("C:\\Users\\kaartav\\Desktop\\oops\\shower.png", 12, 12); // Update the path as
                                                                                                   // necessary
        addFurniture(shower);
    }

    public void addComode() {
        Furniture comode = new Furniture("C:\\Users\\kaartav\\Desktop\\oops\\comode.png", 8, 8); // Update the path as
                                                                                                 // necessary
        addFurniture(comode);
    }

    public void deleteSelected() {
        if (selectedRoomForRelativePos != null) {
            rooms.remove(selectedRoomForRelativePos);
            selectedRoomForRelativePos = null; // Clear selection
            repaint();
        } else if (selectedFurnitureforUsing != null) {
            furnitureItems.remove(selectedFurnitureforUsing);
            selectedFurnitureforUsing = null; // Clear selection
            repaint();
        } else {
            JOptionPane.showMessageDialog(this, "No room or furniture selected!");
        }
    }

    public void rotateFurnitureAt(int x, int y) {
        selectedFurniture = getFurnitureAt(x, y);

        selectedFurniture.rotate();
        repaint();
    }

    public void rotateDoorAt(int x, int y) {
        selectedDoor = getDoorAt(x, y);
        selectedDoor.rotate();
        repaint();
    }

    public void rotateWindowsAt(int x, int y) {
        selectedWindow = getWindowAt(x, y);

        selectedWindow.rotate();
        repaint();
    }

    public void addDoor(Room room1, Room room2, int x, int y, int width, int height) {
        if (room1 != null && room2 != null && room1.getType().equals("Bedroom") && room2 == null) {
            JOptionPane.showMessageDialog(this, "Bedroom cannot have doors to the outside!");
            return;
        }
        Doors door = new Doors(x, y, width, height, room1, room2);
        doors.add(door);
        repaint();
    }

    public void addWindow(Room room, int x, int y, int width, int height) {
        if (checkOverlapWindows(x, y, width, height)) {
            JOptionPane.showMessageDialog(this, "Windows cannot overlap!");
            return;
        }
        Window window = new Window(x, y, width, height, room);
        windows.add(window);
        repaint();
    }

    public Room SelectedRoomforRelativetedRoom() {
        return selectedRoomForRelativePos;
    }

}
