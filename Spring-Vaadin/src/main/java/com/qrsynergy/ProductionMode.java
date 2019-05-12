package com.qrsynergy;

/**
 * Helper enumerator
 *
 */
public enum ProductionMode {
    PRODUCTION("TODO2"),
    TEST("TODO1"),
    LOCAL("http:localhost:8080/");

    private final String root;

    ProductionMode(final String root){
        this.root = root;
    }

    public String getRoot(){
        return this.root;
    }
}
