/** 
 * File System Tree Control Implementation. 
 * 
 * From "Core Java": "People have often asked what the licensing requirements 
 * for using the sample code in a commercial situation are. You can freely use 
 * any code from this book for non-commercial use. However, if you do want to 
 * use the code as a basis for a commercial project, we simply require that 
 * every person on the development team for that project own a copy of Core Java."
 * 
 * @author Kim Topley
 * @see "The Tree Control: Managing Data with JTree", http://www.informit.com/articles/article.aspx?p=26327&seqNum=16
 */

package com.ning.phatamorgana.views; 

import javax.swing.*; 
import javax.swing.tree.*; 
import javax.swing.event.*; 

import java.io.*; 

@SuppressWarnings("serial")
public class FileTree extends JTree {
	public FileTree(String path) throws FileNotFoundException, SecurityException {
		super((TreeModel)null); // Create the JTree itself 

		// Use horizontal and vertical lines
		putClientProperty("JTree.lineStyle", "Angled"); 

		setRootPath(path); 

		// Listen for Tree Selection Events
		addTreeExpansionListener(new TreeExpansionHandler());
	}

	/**
	 * Sets the path to the root node
	 * @param path  the path of the file tree's root
	 */
	public void setRootPath(String path) throws FileNotFoundException {
		// Create the first node 
		FileTreeNode rootNode = new FileTreeNode(null, path); 

		// Populate the root node with its subdirectories
		rootNode.populateDirectories(true);
		setModel(new DefaultTreeModel(rootNode));
	} 

	// Returns the full pathname for a path, or null if not a known path
	public String getPathName(TreePath path) {
		Object o = path.getLastPathComponent();
		if (o instanceof FileTreeNode) {
			return ((FileTreeNode)o).file.getAbsolutePath();
		} return null;
	} 

	// Returns the File for a path, or null if not a known path
	public File getFile(TreePath path) { 
		Object o = path.getLastPathComponent();
		if (o instanceof FileTreeNode) {
			return ((FileTreeNode)o).file;
		}
		return null;
	} 

	// Inner class that represents a node in this file system tree
	protected static class FileTreeNode extends DefaultMutableTreeNode {
		public FileTreeNode(File parent, String name) throws SecurityException, FileNotFoundException { 
			this.name = name; 

			// See if this node exists and whether it is a directory
			file = new File(parent, name);
			if (!file.exists()) {
				throw new FileNotFoundException("File " + name + " does not exist");
			}

			isDir = file.isDirectory(); 

			// Hold the File as the user object.
			setUserObject(file); 

		} 

		// Override isLeaf to check whether this is a directory 
		public boolean isLeaf() {
			return !isDir; 
		} 

		// Override getAllowsChildren to check whether
		// this is a directory
		public boolean getAllowsChildren() {
			return isDir;
		} 

		// For display purposes, we return our own name 
		public String toString() { 
			return name; 
		}
		
		// If we are a directory, scan our contents and populate
		// with children. In addition, populate those children
		// if the "descend" flag is true. We only descend once,
		// to avoid recursing the whole subtree.
		// Returns true if some nodes were added
		boolean populateDirectories(boolean descend) {
			boolean addedNodes = false; 
			// Do this only once 
			if (populated == false) {
				if (interim == true) { 
					// We have had a quick look here before: remove the dummy node that we added last time
					removeAllChildren();
					interim = false; 
				} 

				String[] names = file.list();// Get list of contents 

				// Process the directories
				for (int i = 0; i < names.length; i++) {
					String name = names[i];  
					File d = new File(file, name);
					try {
						FileTreeNode node = new FileTreeNode(file, name);
						this.add(node);
						addedNodes = true;
						if (d.isDirectory()) {
							if (descend) {
								node.populateDirectories(false); 
							}
							if (descend == false) {
								// Only add one node if not descending 
								break; 
							}
						} 
					} catch (Throwable t) {
						// Ignore phantoms or access problems
					} 
				} 

				// If we were scanning to get all subdirectories,
				// or if we found no subdirectories, there is no
				// reason to look at this directory again, so
				// set populated to true. Otherwise, we set interim
				// so that we look again in the future if we need to
				if (descend == true || addedNodes == false) {
					populated = true; 
				} else {
					// Just set interim state
					interim = true;
				}
			}
			return addedNodes; 
		} 

		protected File file;// File object for this node 
		protected String name;// Name of this node 
		protected boolean populated;
		// true if we have been populated 
		protected boolean interim;
		// true if we are in interim state 
		protected boolean isDir;// true if this is a directory
	} 

	// Inner class that handles Tree Expansion Events
	protected class TreeExpansionHandler implements TreeExpansionListener {
		public void treeExpanded(TreeExpansionEvent evt) { 
			TreePath path = evt.getPath();// The expanded path 
			JTree tree = (JTree)evt.getSource();// The tree 

			// Get the last component of the path and arrange to have it fully populated.
			FileTreeNode node = 
				(FileTreeNode)path.getLastPathComponent();
			if (node.populateDirectories(true)) {
				((DefaultTreeModel)tree.getModel()).
				nodeStructureChanged(node);
			} 
		} 

		public void treeCollapsed(TreeExpansionEvent evt) {
			// Nothing to do 
		} 
	} 
} 