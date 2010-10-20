package com.ning.phatamorgana;

import javax.swing.UIManager;
import com.ning.phatamorgana.views.ApplicationWindow;

/**
 * The top-level class for the application.
 */
public class Application {

	/**
	 * Runs the application.
	 */
	public static void main(String[] args) {
		initLAF();
		new ApplicationWindow().setVisible(true);
	}

    /**
     * Sets up the look and feel.
     */
    private static void initLAF() {
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
        try {
			UIManager.setLookAndFeel(laf);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
	
}
