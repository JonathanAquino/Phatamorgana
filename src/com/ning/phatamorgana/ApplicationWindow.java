package com.ning.phatamorgana;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * The main application window.
 */
@SuppressWarnings("serial")
public class ApplicationWindow extends JFrame {

	/** The scroll-pane containing the directory tree. */
	private JScrollPane directoryScrollPane = new JScrollPane();
	
	/** The scroll-pane containing the source code for the selected file. */
	private JScrollPane codeScrollPane = new JScrollPane();
	
	/** The scroll-pane for the list of refactorings to be performed. */
	private JScrollPane refactoringListScrollPane = new JScrollPane();

	/** The split pane with top and bottom parts. */
	private JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeScrollPane, refactoringListScrollPane);
	
	/** The split pane with left and right parts. */
	private JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, directoryScrollPane, verticalSplitPane);
	
	/**
	 * Creates a new application window.
	 */
	public ApplicationWindow() {
		setTitle("Phatamorgana Ð Refactoring Tool for Dynamic Programming Languages");
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
	}
	
}
