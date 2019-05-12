package com.qrsynergy;

/**
 * Helper enumerator
 *
 */
public enum ProductionMode {
    PRODUCTION("TODO2"),
    TEST("40.68.206.64:8080/"),
    LOCAL("http:localhost:8080/");

    private final String root;

    ProductionMode(final String root){
        this.root = root;
    }

    public String getRoot(){
        return this.root;
    }
}
