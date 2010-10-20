package com.ning.phatamorgana.models;

import java.util.Map;

/**
 * A BeanShell/JRuby/Jython/other script that is run during startup and
 * when menu items are clicked. 
 */
public interface Plugin {

    /**
     * Returns the name of the plugin.
     * @return human-readable name of the plugin
     */
    public String getName();
    
    /**
     * Returns a description of the plugin.
     * @return a brief (no more than 500 words) description of what the plugin does
     */
    public String getDescription();
    
    /**
     * The name of the creator of the plugin.
     * @return the author's first, middle, and last names, or null to be anonymous
     */
    public String getAuthor();
    
    /**
     * Called when the application starts.
     * @param context  hooks into the application
     */
    public void initialize(Map<String, Object> context);
    
    /**
     * Called by menu items and buttons.
     * @param context  hooks into the application
     */
    public void execute(Map<String, Object> context);
}
