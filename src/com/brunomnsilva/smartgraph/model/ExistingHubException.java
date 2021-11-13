package com.brunomnsilva.smartgraph.model;

import com.brunomnsilva.smartgraph.graph.InvalidVertexException;

public class ExistingHubException extends InvalidVertexException {

    public ExistingHubException() {
        super("This Hub already exists!");
    }

}
