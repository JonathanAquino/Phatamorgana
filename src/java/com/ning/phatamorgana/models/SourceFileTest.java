package com.ning.phatamorgana.models;

import static org.junit.Assert.*;

public class SourceFileTest {
    
    private SourceFile sourceFile = new TestSourceFile();
    
    @org.junit.Test
    public void testGetContentsWithSelectionMakers_PutsMarkersAtStart_WhenSelectionEmpty() {
        sourceFile.contents = "ABCDE";
        sourceFile.selectionStart = 0;
        sourceFile.selectionEnd = 0;
        assertEquals("{SELECTION-START}{SELECTION-END}ABCDE", sourceFile.getContentsWithSelectionMarkers());
    }
    
    @org.junit.Test
    public void testGetContentsWithSelectionMakers_PutsMarkersAtStart_WhenSelectionHasMultipleCharacters() {
        sourceFile.contents = "ABCDE";
        sourceFile.selectionStart = 1;
        sourceFile.selectionEnd = 3;
        assertEquals("A{SELECTION-START}BC{SELECTION-END}DE", sourceFile.getContentsWithSelectionMarkers());
    }
    
    private static class TestSourceFile extends SourceFile {
        public TestSourceFile() {
            super();
        }
        public String contents;
        public int selectionStart;
        public int selectionEnd;
    }
}


