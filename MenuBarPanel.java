import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBarPanel extends JMenuBar implements ActionListener {

    JMenuItem loadItem;
    JMenuItem saveItem;
    JMenuItem exitItem;

    private CanvasPanel canvasPanel;

    public MenuBarPanel(CanvasPanel canvasPanel) {

        this.canvasPanel = canvasPanel;


        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");

        loadItem = new JMenuItem("Load");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        loadItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);


        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        this.add(fileMenu);
        this.add(editMenu);
        this.add(helpMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadItem) {
            JFileChooser fileChooser = new JFileChooser();
        
        // Show the Open dialog; return if the user approves file selection
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            // Assume canvasPanel has a method `loadRoomsFromFile`
            canvasPanel.loadRoomsFromFile(file);
            
            System.out.println("Loaded file: " + file.getAbsolutePath());
        } else {
            System.out.println("File selection was canceled.");
        }
        } else if (e.getSource() == saveItem) {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                canvasPanel.saveRoomsToFile(file); // Save to file
                System.out.println("Saved to file: " + file.getAbsolutePath());
            }
        } else if (e.getSource() == exitItem) {
            System.exit(0); // Exit the application
        }
    }
    
}
