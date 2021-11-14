package com.brunomnsilva.smartgraph.model;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.InvalidEdgeException;
import com.brunomnsilva.smartgraph.graph.Vertex;

import java.util.*;

public class NetworkManager {

    private List<Hub> hubs;
    private List<Route> routes;

    // If two arguments are received, read hubs and routes files
    public NetworkManager(String folder, String routes_file) {
        readFolder(folder, routes_file);
    }

    // If hubs information is already received, read only routes file (used for routes file imports)
    public NetworkManager(String routes_file, List<Hub> hubs) throws NonEqualHubsException {
        String folder = routes_file.substring(0,routes_file.lastIndexOf("/"));
        String route_file = routes_file.substring(routes_file.lastIndexOf("/") + 1);
        FileReader fileReader = new FileReader(folder, route_file);
        this.hubs = hubs;
        this.routes = fileReader.readRoutes(this.hubs);
    }

    // Populate list of hubs and routes
    private void readFolder(String folder, String routes_file) {
        FileReader fileReader = new FileReader(folder, routes_file);
        this.hubs = fileReader.readHubs();
        this.routes = fileReader.readRoutes(this.hubs);
    }

    // Returns a list of all the hubs
    public List<Hub> getHubs() {
        return this.hubs;
    }

    // Returns a list of all the routes
    public List<Route> getRoutes() {
        return this.routes;
    }

    // Returns a count of all the hubs
    public int countHubs() {
        return getHubs().size();
    }

    // Returns a count of all the routes
    public int countRoutes() {
        return getRoutes().size();
    }

    // Clears the hubs list
    public void clearHubs() {
        this.hubs.clear();
    }

    // Clears the routes list
    public void clearRoutes() {
        this.routes.clear();
    }

    // NOT IMPLEMENTED
    // Add a given Hub to the graph
    public Vertex<Hub> insertHub(Graph graph, List<Vertex<Hub>> vertices, Hub newHub) throws ExistingHubException {
        return null;
    }

    // NOT IMPLEMENTED
    // Remove a given hub from the graph
    public Vertex<Hub> removeHub(Graph graph, List<Vertex<Hub>> vertices, Hub hub) throws NonExistingHubException {
        return null;
    }

    // Add a given Route to the graph, returns the created Route
    public Edge<Route,Hub> insertRoute(Graph graph, List<Edge<Route,Hub>> edges, Route newRoute) throws ExistingRouteException {
        // Validation: this Route already exists
        for (Route route : getRoutes())
            if (route.containsHub(newRoute.getHubOrigin()) && route.containsHub(newRoute.getHubDestination()))
                throw new ExistingRouteException();
            this.routes.add(newRoute);
        Edge<Route,Hub> edge = graph.insertEdge(newRoute.getHubOrigin(),newRoute.getHubDestination(),newRoute);
        edges.add(edge);
        return edge;
    }

    // Remove a given Route from the graph, returns the removed Route
    public Edge<Route,Hub> removeRoute(Graph graph, List<Edge<Route,Hub>> edges, Route route) throws NonExistingRouteException {
        // Validation: this Route doesn't exists
        if (route == null) throw new NonExistingRouteException();
        int routeIndex = this.routes.indexOf(route);
        this.routes.remove(route);
        graph.removeEdge(edges.get(routeIndex));
        return edges.remove(routeIndex);
    }

    // Returns a Map of all the Hubs (Key) and their corresponding adjacency (Value)
    public Map<Hub,Integer> hubCentrality() {
        Map<Hub,Integer> map = new HashMap<>();
        for(Hub elem: getHubs())
            map.put(elem,countNeighbors(elem));
        return map;
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

    // Returns the 5 top Hubs with the most adjacency (from method hubCentrality()), on descending order
    public List<Hub> top5CentralHubs() {
        List<Hub> list = new ArrayList<>();
        List<Map.Entry<Hub,Integer>> entriesList = new ArrayList<>(hubCentrality().entrySet());
        entriesList.sort(Map.Entry.comparingByValue());
        for (int i = entriesList.size() - 1; i > entriesList.size() - 6; i--)
            list.add(entriesList.get(i).getKey());
        return list;
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

    // Returns a collection of all the visited Hubs, by breadth first order, starting at a root Hub
    public List<Hub> breadthFirstSearch(Hub root) {
        Set<Hub> visited = new HashSet<>();
        Queue<Hub> queue = new LinkedList<>();
        List<Hub> list = new ArrayList<>();
        visited.add(root);
        queue.add(root);
        while(!queue.isEmpty()){
            Hub v = queue.remove();
            list.add(v);
            for(Hub elem : getNeighbors(v))
                if(!visited.contains(elem)){
                    queue.add(elem);
                    visited.add(elem);
                }
        }
        return list;
    }

    // Returns a collection of all the visited Hubs, by depth first order, starting at a root Hub
    public List<Hub> depthFirstSearch(Hub root) {
        Set<Hub> visited = new HashSet<>();
        Stack<Hub> stack = new Stack<>();
        List<Hub> list = new ArrayList<>();
        visited.add(root);
        stack.add(root);
        while(!stack.isEmpty()){
            Hub v = stack.pop();
            list.add(v);
            for(Hub elem : getNeighbors(v))
                if(!visited.contains(elem)){
                    stack.add(elem);
                    visited.add(elem);
                }
        }
        return list;
    }

    // Saves a file in a specified folder with all the current routes, returns the generated file name
    public String saveRoutes(String folderName) {
        FileWriter fileWriter = new FileWriter();
        fileWriter.matrixToList(getRoutesMatrix());
        return fileWriter.saveFile(folderName);
    }

    // Returns the number of the graph components
    public int components() {
        int components = 0;
        List<Hub> visitedHubs = new ArrayList<>();
        for (Hub hub : getHubs())
            if (!visitedHubs.contains(hub)) {
                components++;
                visitedHubs.addAll(breadthFirstSearch(hub));
            }
        return components;
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
