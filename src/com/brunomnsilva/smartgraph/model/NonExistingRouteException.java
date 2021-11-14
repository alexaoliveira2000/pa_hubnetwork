package com.brunomnsilva.smartgraph.model;

import com.brunomnsilva.smartgraph.graph.InvalidEdgeException;

public class NonExistingRouteException extends InvalidEdgeException {

    public NonExistingRouteException() {
        super("This Route doesn't exist!");
    }

}
