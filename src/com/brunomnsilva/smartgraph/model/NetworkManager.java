package com.brunomnsilva.smartgraph.model;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;

import java.util.List;
import java.util.Map;

public class NetworkManager {

    private List<Hub> hubs;
    private List<Route> routes;

    public NetworkManager(String folder) {
        readFolder(folder);
    }

    private void readFolder(String folder) {
        FileReader fileReader = new FileReader(folder);
        hubs = fileReader.readHubs();
        routes = fileReader.readRoutes(hubs);
    }

    public List<Hub> getHubs() {
        return this.hubs;
    }

    public List<Route> getRoutes() {
        return this.routes;
    }

    public int countHubs() {
        return getHubs().size();
    }

    public int countRoutes() {
        return getRoutes().size();
    }

    // NOT IMPLEMENTED
    // Add a given Hub to the graph
    public Vertex<Hub> insertHub(Hub hub) {
        return null;
    }

    // NOT IMPLEMENTED
    // Add a given Route to the graph
    public Edge insertRoute(Route route) {
        return null;
    }

    // NOT IMPLEMENTED
    // Return a Map of all the Hubs (Key) and their corresponding adjacency (Value)
    public Map<Hub,Integer> hubCentrality() {
        return null;
    }

    // NOT IMPLEMENTED
    // Returns the 5 top Hubs with the most adjacency (from method hubCentrality()), on descending order
    public List<Hub> top5CentralHubs() {
        return null;
    }

    // NOT IMPLEMENTED
    // Returns a list of Hubs that are isolated (don't appear in any Route)
    public List<Hub> getIsolatedHubs() {
        return null;
    }

    // NOT IMPLEMENTED
    // Returns the number of total isolated Hubs
    public int countIsolatedHubs() {
        return getIsolatedHubs().size();
    }

    // NOT IMPLEMENTED
    // Returns the shortest path between any 2 Hubs
    public List<Hub> shortestPath(Hub origin, Hub destination) {
        return null;
    }

    // NOT IMPLEMENTED
    // Returns the total distance of the shortest path between any 2 Hubs
    public int shortestPathTotalDistance(Hub origin, Hub destination) {
        return 0;
    }

    // NOT IMPLEMENTED
    // Returns a list of the farthest 2 Hubs
    public List<Hub> farthestHubs() {
        return null;
    }

    // NOT IMPLEMENTED
    // Returns a list of Hubs which their path goes through less or equal to maxHubs from a certain Hub
    public List<Hub> closeHubs(Hub hub, int maxHubs) {
        return null;
    }

}
