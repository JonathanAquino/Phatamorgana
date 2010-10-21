package com.ning.phatamorgana.models;

import java.io.File;

/**
 * A source-code tree.
 */
public class Codebase {

    // In the future, a codebase may have multiple root directories. [Jon Aquino 2010-10-20]
    
    /** The root of the codebase. */
    private File rootDirectory;

    /**
     * Creates a Codebase
     * @param rootDirectory the root of the codebase
     */
    public Codebase(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }
    
}
