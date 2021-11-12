package com.brunomnsilva.smartgraph.model;

public class Route {

    private int distance;
    private Hub origin;
    private Hub destination;

    public Route(Hub origin, Hub destination) {
        this.distance = 0;
        this.origin = origin;
        this.destination = destination;
    }

    public Route(Hub origin, Hub destination, int distance) {
        this.distance = distance;
        this.origin = origin;
        this.destination = destination;
    }

    public Hub getHubOrigin() {
        return this.origin;
    }

    public Hub getHubDestination() {
        return this.destination;
    }

    public int getDistance() {
        return this.distance;
    }

}
