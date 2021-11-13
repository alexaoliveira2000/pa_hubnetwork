package com.brunomnsilva.smartgraph.model;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NetworkManager {

    private List<Hub> hubs;
    private List<Route> routes;

    public NetworkManager(String folder, String routes_file) {
        readFolder(folder, routes_file);
    }

    private void readFolder(String folder, String routes_file) {
        FileReader fileReader = new FileReader(folder, routes_file);
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
    // Returns a Map of all the Hubs (Key) and their corresponding adjacency (Value)
    public Map<Hub,Integer> hubCentrality() {
        return null;
    }

    // Returns a list of all the neighboring hubs from a given Hub
    public List<Hub> getNeighbors(Hub hub) {
        List<Hub> list = new ArrayList<>();
        for (Route route : routes)
            if (route.containsHub(hub))
                list.add(route.opposite(hub));
        return list;
    }

    // Returns the number of neighbor hubs from a given Hub
    public int countNeighbors(Hub hub) {
        return getNeighbors(hub).size();
    }

    // NOT IMPLEMENTED
    // Returns the 5 top Hubs with the most adjacency (from method hubCentrality()), on descending order
    public List<Hub> top5CentralHubs() {
        return null;
    }

    // Returns a boolean value if a given Hub is isolated
    public boolean isIsolated(Hub hub) {
        for (Route route : getRoutes())
            if (route.containsHub(hub))
                return false;
        return true;
    }

    // Returns a list of Hubs that are isolated
    public List<Hub> getIsolatedHubs() {
        List<Hub> list = new ArrayList<>();
        for (Hub hub : getHubs())
            if (isIsolated(hub))
                list.add(hub);
        return list;
    }

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

    // Returns a Hub, given the hub identifier
    public Hub getHub(String identifier) {
        for (Hub hub : getHubs())
            if (hub.getIdentifier().equals(identifier))
                return hub;
        return null;
    }

    // Returns a boolean value, if two given Hubs are neighbors
    public boolean hubsAreNeighbors(Hub origin, Hub destination) {
        return getRoute(origin, destination) != null;
    }

    // Returns the Route that connects two given Hubs, null if they are not connected
    public Route getRoute(Hub origin, Hub destination) {
        for (Route route : getRoutes())
            if (route.containsHub(origin) && route.opposite(origin).equals(destination))
                return route;
        return null;
    }

    // NOT IMPLEMENTED
    // Returns a collection of all the visited Hubs, by breadth first order, starting at a root Hub
    public List<Hub> breadthFirstSearch(Hub root) {
        return null;
    }

    // NOT IMPLEMENTED
    // Returns a collection of all the visited Hubs, by depth first order, starting at a root Hub
    public List<Hub> depthFirstSearch(Hub root) {
        return null;
    }

    public void saveRoutes(String folderName) {
        FileWriter fileWriter = new FileWriter();
        fileWriter.matrixToList(getRoutesMatrix());
        fileWriter.saveFile(folderName);
    }

    // Returns a matrix with all current available routes
    private int[][] getRoutesMatrix() {
        int size = countHubs();
        int[][] matrix = new int[size][size];
        for (int row = 0; row < size; row++)
            for (int column = 0; column < size; column++)
                if (hubsAreNeighbors(this.hubs.get(row), this.hubs.get(column)))
                    matrix[row][column] = getRoute(this.hubs.get(row), this.hubs.get(column)).getDistance();
                else
                    matrix[row][column] = 0;
        return matrix;
    }

}
