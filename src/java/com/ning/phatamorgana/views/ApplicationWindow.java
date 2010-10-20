package com.ning.phatamorgana.views;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.MenuElement;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.ning.phatamorgana.models.SourceFile;

/**
 * The main application window.
 */
@SuppressWarnings("serial")
public class ApplicationWindow extends JFrame {

    /** The scroll-pane containing the file tree. */
    private JScrollPane fileTreeScrollPane = new JScrollPane();
    
    /** The file tree */
    private FileTree fileTree;
    
    /** The text area for displaying the contents of a file. */
    private JTextArea codeTextArea = new JTextArea() {
        {
            setFont(Font.decode("monospaced-12"));
        }
    };
    
    /** The scroll-pane containing the source code for the selected file. */
    private JScrollPane codeScrollPane = new JScrollPane(codeTextArea);
    
    /** The scroll-pane for the list of refactorings to be performed. */
    private JScrollPane refactoringListScrollPane = new JScrollPane();

    /** The split pane with top and bottom parts. */
    private JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, codeScrollPane, refactoringListScrollPane);
    
    /** The split pane with left and right parts. */
    private JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileTreeScrollPane, verticalSplitPane);

    /** The menu at the top of the window. */
    private JMenuBar menuBar = new JMenuBar();
    
    /**
     * Creates a new application window.
     */
    public ApplicationWindow() {
        setTitle("Phatamorgana � Refactoring Tool for Dynamic Programming Languages");
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
        addMenus();
    }

    /**
     * Installs menus at the top of the window.
     */
    private void addMenus() {
        add(menuBar, BorderLayout.NORTH);
        addMenu(new String[] {"File", "Select Source Tree�"}, new ActionListener()  {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectSourceTreeMenuItemSelected(e);
            }
        });
    }
    

    /**
     * Creates the specified menus and submenus.
     * @param menuPath the strings for the menus and submenus
     * @param actionListener the handler for the menu
     * @return the newly created menu item
     */
    public JMenuItem addMenu(String[] menuPath, final ActionListener actionListener) {
        JMenuItem menuItem = getMenuItem(menuPath[0], menuBar);
        if (menuItem == null) {
            menuItem = new JMenu(menuPath[0]);
            menuBar.add(menuItem);
            menuBar.validate(); // Refresh [Jon Aquino 2010-10-20]
        }
        for (int i = 1; i < menuPath.length; i++) {
            if (i == menuPath.length - 1) {
                JMenuItem childMenuItem = new JMenuItem(menuPath[i]);
                menuItem.add(childMenuItem);
                childMenuItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            actionListener.actionPerformed(e);
                        } catch (Exception x) {
                            handleException(x);
                        }
                    }
                });
                return childMenuItem;
            }
            JMenuItem childMenuItem = getMenuItem(menuPath[i], menuItem);
            if (childMenuItem == null) {
                childMenuItem = new JMenuItem(menuPath[i]);
            }
            menuItem.add(childMenuItem);
            menuItem = childMenuItem;
        }
        throw new RuntimeException("Shouldn't get here");
    }
    
    /**
     * Returns the menu item with the given text
     * @param string the menu item text to search for
     * @param parentMenuItem the menu item whose children to inspect
     * @return the menu item, or null if not found
     */
    private JMenuItem getMenuItem(String text, MenuElement parentMenuItem) {
        if (parentMenuItem instanceof JMenu) {
            parentMenuItem = ((JMenu)parentMenuItem).getPopupMenu();
        }
        for (MenuElement childMenuItem : parentMenuItem.getSubElements()) {
            if (childMenuItem instanceof JMenuItem && ((JMenuItem)childMenuItem).getText().equals(text)) {
                return ((JMenuItem)childMenuItem);
            }
        }
        return null;
    }

    /**
     * Responds to the given exception.
     * @param e the Exception that occurred.
     */
    protected void handleException(Exception e) {
        e.printStackTrace(System.out);
    }

    /**
     * Shows a file dialog for choosing the source tree to operate on.
     * @param e the event for the selection of the menu item
     */
    private void selectSourceTreeMenuItemSelected(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser(); 
        fileChooser.setDialogTitle("Select Source Tree");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (fileTree == null) {
                fileTree = createFileTree(fileChooser.getSelectedFile().getAbsolutePath());
            } else {
                try {
                    fileTree.setRootPath(fileChooser.getSelectedFile().getAbsolutePath());
                } catch (FileNotFoundException x) {
                    throw new RuntimeException(x);
                }
            }
        }
    }

    /**
     * Creates a FileTree
     * @param path the path to the root
     * @return the newly created FileTree
     */
    private FileTree createFileTree(String path) {
        FileTree fileTree;
        try {
            fileTree = new FileTree(path);
        } catch (Exception x) {
            throw new RuntimeException(x);
        }
        fileTreeScrollPane.getViewport().add(fileTree);
        fileTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                fileTreeNodeSelected(e);
            }
        });
        return fileTree;
    }

    /**
     * Called when the user clicks a node in the file tree.
     * @param e the event for the selection of the tree node
     */
    private void fileTreeNodeSelected(TreeSelectionEvent e) {
        File file = fileTree.getFile(e.getPath());
        if (file != null && file.isFile()) {
            codeTextArea.setText(new SourceFile(file).getContents());
            codeTextArea.setCaretPosition(0);
        }
    }
    
}
