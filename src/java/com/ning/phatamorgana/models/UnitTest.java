package com.ning.phatamorgana.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Unit test for scripts.
 */
public abstract class UnitTest {

    /**
     * An assertion made by the test.
     */
    public static class Assertion {
        private String description;
        private boolean value;;
        public Assertion(String description, boolean value) {
            this.description = description;
            this.value = value;
        }
        public String getDescription() {
            return description;
        }
        public boolean getValue() {
            return value;
        }
    }
    
    /** The assertions made by the test. */
    private List<Assertion> assertions = new ArrayList<Assertion>();
    
    /**
     * Makes assertions.
     */
    public abstract void run();
    
    /**
     * Asserts that the given condition is true.
     * @param description a brief description of the test
     * @param value the condition to test
     */
    protected void assertTrue(String description, boolean value) {
        assertions.add(new Assertion(description, value));
    }
    
    /**
     * Returns the assertions made by the test
     * @return the assertion objects
     */
    public List<Assertion> getAssertions() {
        return Collections.unmodifiableList(assertions);
    }
    
}
