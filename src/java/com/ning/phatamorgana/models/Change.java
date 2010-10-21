package com.ning.phatamorgana.models;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * A reversible change to a file.
 */
public class Change {
    
    /** The file being changed. */
    private File file;
    
    /** The old contents of the file. */
    private String oldContents;
    
    /** The new contents of the file. */
    private String newContents;
    
    /**
     * Creates a Change object
     * @param file  the file being changed
     * @param oldContents  the old contents of the file
     * @param newContents  the new contents of the file
     */
    public Change(File file, String oldContents, String newContents) {
        this.file = file;
        this.oldContents = oldContents;
        this.newContents = newContents;
    }
    
    /**
     * Writes the new contents to disk.
     */
    public void execute() {
        // TODO: Allow the user to choose the encoding. [Jon Aquino 2010-10-19]
        try {
            FileUtils.writeStringToFile(file, newContents, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Writes the old contents to disk.
     */
    public void unexecute() {
        // TODO: Allow the user to choose the encoding. [Jon Aquino 2010-10-19]
        try {
            FileUtils.writeStringToFile(file, oldContents, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}