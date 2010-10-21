package com.ning.phatamorgana.models;

/**
 * Documentation for a script.
 */
public class Documentation {

    /** Brief description of the script.*/
    private String description;
    
    /** Full name of the primary or original author, or null to remain anonymous. */
    private String author;
    
    /** Version number, e.g., 0.1 */
    private String version;
    
    /**
     * Creates a documentation object.
     * @param description  brief description of the script
     * @param author  full name of the primary or original author, or null to remain anonymous
     * @param version  version number, e.g., 0.1
     */
    public Documentation(String description, String author, String version) {
        this.description = description;
        this.author = author;
        this.version = version;
    }
}
