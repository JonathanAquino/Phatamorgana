package com.ning.phatamorgana.models;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * A set of changes to a set of files.
 */
public class ChangeSet {

    /** The changes in the change set. */
    private List<Change> changes = new ArrayList<Change>();
    
    /**
     * Sets the contents of the given file.
     * @param sourceFile the source-code file
     * @param contents the new contents of the file
     */
    public void setContents(SourceFile sourceFile, String contents) {
        contents = removeSelectionMarkers(contents);
        changes.add(new Change(sourceFile.getFile(), sourceFile.getContents(), contents));
    }

    /**
     * Sets the contents of the given file in a way that can be previewed.
     * {OLD-START-1}...{OLD-END-1}{NEW-START-1}...{NEW-END-1} markers are used to
     * indicate changes in the file. The "1" is a number used to group markers 
     * that are considered to be part of the same change in the file.
     * When the user deletes a change in the preview, all changes with this number
     * are deleted.
     * @param sourceFile the source-code file
     * @param contents the new contents of the file, with change markers
     */
    public void setContentsWithMarkers(SourceFile sourceFile, String contentsWithMarkers) {
        contentsWithMarkers = removeSelectionMarkers(contentsWithMarkers);
        throw new RuntimeException("Not yet implemented");
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
    

    /**
     * Writes the new contents to disk
     * @param changeSet
     */
    public void execute() {
        for (Change change : changes) {
            change.execute();
        }
    }
    
    /**
     * Writes the old contents to disk
     * @param changeSet
     */
    public void unexecute() {
        for (Change change : changes) {
            change.unexecute();
        }
    }
}
