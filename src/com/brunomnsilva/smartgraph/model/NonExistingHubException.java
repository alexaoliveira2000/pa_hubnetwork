package com.brunomnsilva.smartgraph.model;

import com.brunomnsilva.smartgraph.graph.InvalidVertexException;

public class NonExistingHubException extends InvalidVertexException {

    public NonExistingHubException() {
        super("This Hub doesn't exist!");
    }

}
