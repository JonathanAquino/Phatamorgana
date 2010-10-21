package com.ning.phatamorgana.models;

import static org.junit.Assert.assertEquals;

public class ChangeSetTest {

    private ChangeSet changeSet = new TestChangeSet();
    
    @org.junit.Test
    public void testRemoveSelectionMarkers_RemovesAllSelectionMarkers() {
        assertEquals("ABCDE", changeSet.removeSelectionMarkers("A{SELECTION-START}BC{SELECTION-END}DE"));
    }
    
    private static class TestChangeSet extends ChangeSet {
        @Override
        public String removeSelectionMarkers(String contents) {
            return super.removeSelectionMarkers(contents);
        }
    }
    
}
