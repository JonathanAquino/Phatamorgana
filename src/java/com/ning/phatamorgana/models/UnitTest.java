package com.ning.phatamorgana.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;

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
     * Asserts that the given values are equal.
     * @param description a brief description of the test
     * @param expected the expected value
     * @param actual the actual value
     */
    protected void assertEquals(String description, Object expected, Object actual) {
        Gson gson = new Gson();
        assertTrue(description + "\nExpected:\n" + gson.toJson(expected) + "\nActual:\n" + gson.toJson(actual),
                (expected == null && actual == null) || (expected != null && expected.equals(actual)));
    }
    
    /**
     * Returns the assertions made by the test
     * @return the assertion objects
     */
    public List<Assertion> getAssertions() {
        return Collections.unmodifiableList(assertions);
    }
    
}
