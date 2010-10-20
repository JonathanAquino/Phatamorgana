package com.ning.phatamorgana;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ning.phatamorgana.models.BeanShellScriptLoader;
import com.ning.phatamorgana.models.JRubyScriptLoader;
import com.ning.phatamorgana.models.UnitTest;
import com.ning.phatamorgana.views.ApplicationWindow;

/**
 * The top-level class for the application.
 */
public class Application {

    /**
     * Runs the application.
     * @param args the command-line arguments; only one is expected: the 
     *         path to the scripts directory
     */
    public static void main(final String[] args) {
        Application application = new Application();
        application.run(args[0]);
    }
    
    /**
     * Runs the application.
     * @param scriptPath path to the scripts directory
     */
    public void run(final String scriptPath) {
        initializeLAF();
        ApplicationWindow applicationWindow = new ApplicationWindow();
        applicationWindow.setVisible(true);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put("applicationWindow", applicationWindow);
        context.put("unitTests", new ArrayList<UnitTest>());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new BeanShellScriptLoader(context).loadScripts(new File(scriptPath));
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
                try {
                    new JRubyScriptLoader(context).loadScripts(new File(scriptPath));
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
        });
    }

    /**
     * Sets up the look and feel.
     */
    private void initializeLAF() {
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
