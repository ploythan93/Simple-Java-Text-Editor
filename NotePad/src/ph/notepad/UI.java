/**
 * @name        Simple Java NotePad
 * @package     ph.notepad
 * @file        UI.java
 * @author      SORIA Pierre-Henry
 * @email       pierrehs@hotmail.com
 * @link        http://github.com/pH-7
 * @copyright   Copyright Pierre-Henry SORIA, All Rights Reserved.
 * @license     Apache (http://www.apache.org/licenses/LICENSE-2.0)
 * @create      2012-05-04
 * @update      2012-05-06
 */

package ph.notepad;

// GUI
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.*;
// Input Stream
import java.io.*;
// Various
import java.util.Scanner;

public class UI extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private Container container;
	private JTextArea textArea;
	private JMenuBar menuBar;
	private JMenu menuFile, menuEdit, menuFind, menuAbout;
	private JMenuItem newFile, openFile, saveFile, close, clearFile, quickFind, aboutMe, aboutSoftware;
	
	public UI() {	 
		container = getContentPane();
		
		// Set the initial size of the window
		setSize(500, 300); 
		
		// Set the title of the window
		setTitle("Undefined | " + Main.NAME);
		
		// Set the default close operation (exit when it gets closed)
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		
		// Set a default font for the TextArea
		textArea = new JTextArea("", 0,0);
		textArea.setFont(new Font("Century Gothic", Font.BOLD, 12)); 
		
		// This is why we didn't have to worry about the size of the TextArea!
		getContentPane().setLayout(new BorderLayout()); // the BorderLayout bit makes it fill it automatically
		getContentPane().add(textArea);

		// Set the Menus
		menuFile = new JMenu("File");
		menuEdit = new JMenu("Edit");
		menuFind = new JMenu("Search");
		menuAbout = new JMenu("About");
		
		// Set the Items Menu
		newFile = new JMenuItem("New");
		openFile = new JMenuItem("Open");
		saveFile = new JMenuItem("Save");
		close = new JMenuItem("Close");
		clearFile = new JMenuItem("Clear");
		quickFind = new JMenuItem("Quick");
		aboutMe = new JMenuItem("About Me");
		aboutSoftware = new JMenuItem("About Software");
		
		// Set the Menu Bar into the our GUI
		menuBar = new JMenuBar();
		menuBar.add(menuFile); 
		menuBar.add(menuEdit);
		menuBar.add(menuFind);
		menuBar.add(menuAbout); 
		
		container.add(menuBar, BorderLayout.NORTH);  
		
		// New File
		newFile.addActionListener(this);  // Adding an action listener (so we know when it's been clicked).
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK)); // Set a keyboard shortcut
		menuFile.add(newFile); // Adding the file menu
		
		// Open File
		openFile.addActionListener(this);
		openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK)); 
		menuFile.add(openFile); 
		
		// Save File
		saveFile.addActionListener(this);
		saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		menuFile.add(saveFile);
        
		// Close File
		
		/*
		 * Along with our "CTRL+F4" shortcut to close the window, we also have
		 * the default closer, as stated at the beginning of this tutorial.
		 * this means that we actually have TWO shortcuts to close:
		 * 1) the default close operation (example, Alt+F4 on Windows)
		 * 2) CTRL+F4, which we are about to define now: (this one will appear in the label).
		 */
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.CTRL_MASK));
		close.addActionListener(this);
		menuFile.add(close);

		// Clear File (Code)
		clearFile.addActionListener(this);
		clearFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK)); 
		menuEdit.add(clearFile);
		
		// Find Word
		quickFind.addActionListener(this);
		quickFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		menuFind.add(quickFind);
		
		// About Me
		aboutMe.addActionListener(this);
		aboutMe.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		menuAbout.add(aboutMe);
		
		// About Software
		aboutSoftware.addActionListener(this);
		aboutSoftware.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		menuAbout.add(aboutSoftware);
		
	}
	
	public void actionPerformed (ActionEvent e) {
		// If the source of the event was our "close" option
		if(e.getSource() == close)
			this.dispose(); // dispose all resources and close the application
		
		// If the source was the "new" file option
		else if(e.getSource() == newFile) {
			FEdit.clear(textArea);
		}
		// If the source was the "open" option
		else if(e.getSource() == openFile) {
			JFileChooser open = new JFileChooser(); // open up a file chooser (a dialog for the user to browse files to open)
			int option = open.showOpenDialog(this); // get the option that the user selected (approve or cancel)
			
			/*
			 * NOTE: because we are OPENing a file, we call showOpenDialog~
			 * if the user clicked OK, we have "APPROVE_OPTION"
			 * so we want to open the file
			 */
			if(option == JFileChooser.APPROVE_OPTION) {
				FEdit.clear(textArea); // clear the TextArea before applying the file contents
				try {
					// create a scanner to read the file (getSelectedFile().getPath() will get the path to the file)
					Scanner scan = new Scanner(new FileReader(open.getSelectedFile().getPath()));
					while (scan.hasNext()) // while there's still something to read
						textArea.append(scan.nextLine() + "\n"); // append the line to the TextArea
				} catch (Exception ex) { // catch any exceptions, and...
					// ...write to the debug console
					System.out.println(ex.getMessage());
				}
			}
		}
		// If the source of the event was the "save" option
		else if(e.getSource() == saveFile) {
			// Open a file chooser
			JFileChooser fileChoose = new JFileChooser(); 
			// Open the file, only this time we call
			int option = fileChoose.showSaveDialog(this); 
			
			/*
			 * ShowSaveDialog instead of showOpenDialog
			 * if the user clicked OK (and not cancel)
			 */
			if(option == JFileChooser.APPROVE_OPTION) {
				try {
					File file = fileChoose.getSelectedFile();
					// Set the new title of the window
					setTitle(file.getName() + " | " + Main.NAME);
					// Create a buffered writer to write to a file
					BufferedWriter out = new BufferedWriter(new FileWriter(file.getPath()));
					// Write the contents of the TextArea to the file
					out.write(textArea.getText()); 
					// Close the file stream
					out.close(); 
				} catch (Exception ex) { // again, catch any exceptions and...
					// ...write to the debug console
					System.out.println(ex.getMessage());
				}
			}
		}
		
		// Clear File (Code)
		if(e.getSource() == clearFile) {
			FEdit.clear(textArea);
		}
		// Find 
		if(e.getSource() == quickFind) {
			new Find(textArea);
		}	
		
		// About Me
		else if(e.getSource() == aboutMe) {
			new About().me();
		}
		// About Software
		else if(e.getSource() == aboutSoftware) {
			new About().software();
		}
		
	}
		
}
