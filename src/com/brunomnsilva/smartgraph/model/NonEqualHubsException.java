package com.brunomnsilva.smartgraph.model;

public class NonEqualHubsException extends RuntimeException {

    public NonEqualHubsException() {
        super("The number of Hubs on the matrix doesn't match the current Hubs number!");
    }

}
