package com.qrsynergy;

/**
 * Helper enumerator
 *
 */
public enum ProductionMode {
    PRODUCTION("TODO2"),
    TEST("142.93.230.142:8080/"),
    LOCAL("http:localhost:8080/");

    private final String root;

    ProductionMode(final String root){
        this.root = root;
    }

    public String getRoot(){
        return this.root;
    }
}
