package com.ning.phatamorgana;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * The main application window.
 */
@SuppressWarnings("serial")
public class ApplicationWindow extends JFrame {

	/** The scroll-pane containing the file tree. */
	private JScrollPane fileTreeScrollPane = new JScrollPane();
	
	/** The file tree */
	private FileTree fileTree;
	
	/** The scroll-pane containing the source code for the selected file. */
	private JScrollPane codeScrollPane = new JScrollPane();
	
	/** The scroll-pane for the list of refactorings to be performed. */
	private JScrollPane refactoringListScrollPane = new JScrollPane();

	/** The split pane with top and bottom parts. */
	private JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeScrollPane, refactoringListScrollPane);
	
	/** The split pane with left and right parts. */
	private JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileTreeScrollPane, verticalSplitPane);
	
	/**
	 * Creates a new application window.
	 */
	public ApplicationWindow() {
		setTitle("Phatamorgana – Refactoring Tool for Dynamic Programming Languages");
		setSize(900, 665);
		getContentPane().setLayout(new BorderLayout());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) { 
            	System.exit(0); 
        	}
        });
        horizontalSplitPane.setDividerLocation(300);
        horizontalSplitPane.setOneTouchExpandable(true);
        verticalSplitPane.setDividerLocation(500);
        verticalSplitPane.setOneTouchExpandable(true);
        add(horizontalSplitPane, BorderLayout.CENTER);
        addMenuBar();
	}

	/**
	 * Installs the menu bar at the top of the window.
	 */
	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);
		JMenu fileMenu = new JMenu("File");
		JMenuItem selectSourceTreeMenuItem = new JMenuItem("Select Source Tree…");
		selectSourceTreeMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					selectSourceTree();
				} catch (Exception x) {
					handleException(x);
				}
			}
		});
		menuBar.add(fileMenu);
		fileMenu.add(selectSourceTreeMenuItem);
	}
	
	/**
	 * Responds to the given exception.
	 * @param e  the Exception that occurred.
	 */
	protected void handleException(Exception e) {
		e.printStackTrace();
	}

	/**
	 * Shows a file dialog for choosing the source tree to operate on.
	 * @throws SecurityException 
	 * @throws FileNotFoundException 
	 */
	private void selectSourceTree() throws FileNotFoundException, SecurityException {
	    JFileChooser fileChooser = new JFileChooser(); 
	    fileChooser.setDialogTitle("Select Source Tree");
	    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    fileChooser.setAcceptAllFileFilterUsed(false);
	    if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	    	if (fileTree == null) {
	    		fileTree = new FileTree(fileChooser.getSelectedFile().getAbsolutePath());
	    		fileTreeScrollPane.getViewport().add(fileTree);
	    	} else {
	    		fileTree.setRootPath(fileChooser.getSelectedFile().getAbsolutePath());
	    	}
        }
	}
	
}
