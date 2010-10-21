package com.ning.phatamorgana.models;

import org.apache.commons.lang.StringUtils;

/**
 * A set of changes to a set of files.
 */
public class ChangeSet {

    /** Marker for the start of code before a change. */
    private static final String OLD_START = "{OLD-START}";
    
    /** Marker for the end of code before a change. */
    private static final String OLD_END = "{OLD-END}";

    /** Marker for the start of code after a change. */
    private static final String NEW_START = "{NEW-START}";
    
    /** Marker for the end of code after a change. */
    private static final String NEW_END = "{NEW-END}";
    
    /**
     * Sets the contents of the given file.
     * @param sourceFile the source-code file
     * @param contents the new contents of the file
     */
    public void setContents(SourceFile sourceFile, String contents) {
        contents = removeSelectionMarkers(contents);
    }

    /**
     * Sets the contents of the given file in a way that can be previewed.
     * {OLD-START}...{OLD-END}{NEW-START}...{NEW-END} markers are used to
     * indicate changes in the file.
     * @param sourceFile the source-code file
     * @param contents the new contents of the file, with change markers
     */
    public void setContentsWithMarkers(SourceFile sourceFile, String contentsWithMarkers) {
        contentsWithMarkers = removeSelectionMarkers(contentsWithMarkers);
    }
    
    /**
     * Removes the {SELECTION-START} and {SELECTION-END} markers
     * that plugins may have left in the contents.
     * @param contents the contents of the file
     * @return the contents of the file with markers removed
     */
    protected String removeSelectionMarkers(String contents) {
        return StringUtils.replace(StringUtils.replace(contents, SourceFile.SELECTION_START, ""), SourceFile.SELECTION_END, "");
    }
}
