package com.ning.phatamorgana.models;

import java.io.File;
import java.util.Map;

import bsh.Interpreter;

/**
 * Loader for scripts written in BeanShell, a Java-like language.
 * @see http://www.beanshell.org/
 */
public class BeanShellScriptLoader {

    /** Hooks into the application. */
    private Map<String, Object> context;
    
    /**
     * Creates the BeanShell loader.
     * @param context hooks into the application
     */
    public BeanShellScriptLoader(Map<String, Object> context) {
        this.context = context;
    }
    
    /**
     * Loads the BeanShell scripts in the given path and its subdirectories.
     * @param directory the path to the scripts
     */
    public void loadScripts(File directory) {
        try {
            Interpreter interpreter = new Interpreter();
            interpreter.set("context", context);
            interpreter.eval("import com.ning.phatamorgana.models.*");
            interpreter.eval("import junit.framework.*");
            for (File f : directory.listFiles()) {
                if (f.isDirectory()) {
                    loadScripts(f);
                } else if (f.isFile() && f.getName().endsWith(".bsh")) {
                    interpreter.source(f.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
