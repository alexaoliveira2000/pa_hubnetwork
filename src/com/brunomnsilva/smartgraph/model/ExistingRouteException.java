package com.brunomnsilva.smartgraph.model;

import com.brunomnsilva.smartgraph.graph.InvalidEdgeException;

public class ExistingRouteException extends InvalidEdgeException {

    public ExistingRouteException() {
        super("This Route already exists!");
    }

}
