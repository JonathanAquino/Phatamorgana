package com.ning.phatamorgana;

import javax.swing.UIManager;

/**
 * The top-level class for the application.
 */
public class Application {

	/**
	 * Runs the application.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		initLAF();
		new ApplicationWindow().setVisible(true);
	}

    /**
     * Sets up the look and feel.
     */
    private static void initLAF() throws Exception {
        // Apple stuff from Raj Singh 2010-10-19
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("apple.awt.showGrowBox", "true");
        if (UIManager.getLookAndFeel() != null && UIManager.getLookAndFeel().getClass().getName().equals(UIManager.getSystemLookAndFeelClassName())) {
            return;
        }
        String laf = System.getProperty("swing.defaultlaf");
        if (laf == null) { 
        	laf = UIManager.getSystemLookAndFeelClassName(); 
    	}
        UIManager.setLookAndFeel(laf);
    }
	
}
