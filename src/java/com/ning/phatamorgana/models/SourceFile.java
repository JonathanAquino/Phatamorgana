package com.ning.phatamorgana.models;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * A file containing source code. 
 */
public class SourceFile {

    /** Marker for the start of the selection. */
    public static final String SELECTION_START = "{SELECTION-START}";
    
    /** Marker for the end of the selection. */
    public static final String SELECTION_END = "{SELECTION-END}";
    
    /** The file being represented. */
    private File file;
    
    /** The contents of the file. */
    protected String contents;
    
    /** The start of the selection. */
    protected int selectionStart = 0;
    
    /** The end of the selection. */
    protected int selectionEnd = 0;

    /**
     * Creates a SourceFile.
     * @param file the file being represented
     */
    public SourceFile(File file) {
        if (!file.isFile()) {
            throw new RuntimeException("Not a file: " + file.getAbsolutePath());
        }
        this.file = file;
    }
    
    /**
     * Creates a SourceFile.
     */
    protected SourceFile() {
    }
    
    /**
     * Sets the boundaries of the selection
     * @param selectionStart the position of the selection start
     * @param selectionEnd the position of the selection end
     */
    public void setSelection(int selectionStart, int selectionEnd) {
        this.selectionStart = selectionStart;
        this.selectionEnd = selectionEnd;
    } 
    
    /**
     * Returns the contents of the file.
     * @return the source code inside the file
     */
    public String getContents() {
        if (contents != null) {
            return contents;
        }
        // TODO: Allow the user to choose the encoding. [Jon Aquino 2010-10-19]
        try {
            contents = FileUtils.readFileToString(file, "UTF-8");
            return contents;
        } catch (IOException e) {
            try {
                contents = FileUtils.readFileToString(file);
                return contents;
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            }
        }
    }
    
    /**
     * Returns the contents of the file, with the selection delimited by
     * {SELECTION-START} and {SELECTION-END} markers.
     * @return the source code inside the file
     */
    public String getContentsWithSelectionMarkers() {
        String contents = getContents();
        return contents.substring(0, selectionStart) + SELECTION_START + contents.substring(selectionStart, selectionEnd) + SELECTION_END + contents.substring(selectionEnd);
    }
    
}
