package com.brunomnsilva.smartgraph.model;

import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;

import java.util.*;

public class NetworkManager {

    FileReader fileReader;

    public NetworkManager(String folder, String routes_file) {
        this.fileReader = new FileReader(folder, routes_file);
    }

    public NetworkManager(String routes_file) throws NonEqualHubsException {
        String folder = routes_file.substring(0, routes_file.lastIndexOf("/"));
        String route_file = routes_file.substring(routes_file.lastIndexOf("/") + 1);
        this.fileReader = new FileReader(folder, route_file);
    }

    // Removes all the Vertices in a given Graph
    private void clearVertices(Graph<Hub, Route> graph) {
        for (Vertex<Hub> vertex : graph.vertices())
            graph.removeVertex(vertex);
    }

    // Removes all the Edges in a given Graph
    private void clearEdges(Graph<Hub, Route> graph) {
        for (Edge<Route, Hub> edge : graph.edges())
            graph.removeEdge(edge);
    }

    // Creates all initial Vertices (from file), returns a list of all created Vertices
    public List<Vertex<Hub>> createVertices(Graph<Hub, Route> graph) throws ExistingHubException {
        // Validation: remove existing edges and vertices
        clearEdges(graph);
        clearVertices(graph);
        List<Vertex<Hub>> vertices = new ArrayList<>();
        for (Hub hub : fileReader.readHubs())
            vertices.add(insertHub(graph, hub));
        return vertices;
    }

    // Creates all initial Edges (from matrix file), returns a list of all created Edges
    public List<Edge<Route, Hub>> createEdges(Graph<Hub, Route> graph, List<Vertex<Hub>> vertices) throws NonEqualHubsException {
        // Validation: remove existing edges
        clearEdges(graph);
        List<Edge<Route, Hub>> edges = new ArrayList<>();
        List<Route> routes = fileReader.readRoutes(vertices);
        for (Route route : routes)
            edges.add(insertRoute(graph, route));
        return edges;
    }

    // Returns a list of all the hubs, given a list of Vertex<Hub>
    public List<Hub> getHubs(List<Vertex<Hub>> vertices) {
        List<Hub> hubs = new ArrayList<>();
        for (Vertex<Hub> vertex : vertices)
            hubs.add(vertex.element());
        return hubs;
    }

    // Returns a list of all the routes, given a list of Edge<Route,Hub>
    public List<Route> getRoutes(List<Edge<Route, Hub>> edges) {
        List<Route> routes = new ArrayList<>();
        for (Edge<Route, Hub> edge : edges)
            routes.add(edge.element());
        return routes;
    }

    // Add a given Hub to the graph, returns the created Vertex
    public Vertex<Hub> insertHub(Graph graph, Hub hub) throws ExistingHubException {
        List<Vertex> vertices = new ArrayList<>(graph.vertices());
        // Validation : this Hub already exists
        for (Vertex<Hub> vertex : vertices)
            if (vertex.element().getIdentifier().equals(hub.getIdentifier()))
                throw new ExistingHubException();
        return graph.insertVertex(hub);
    }

    // Remove a given hub from the graph, returns the removed vertex
    public Vertex<Hub> removeHub(Graph graph, Hub hub) throws NonExistingHubException {
        // Validation: this Hub doesn't exist
        if (hub == null) throw new NonExistingHubException();
        List<Vertex> vertices = new ArrayList<>(graph.vertices());
        Vertex<Hub> removedVertex = null;
        for (Vertex<Hub> vertex : vertices)
            if (vertex.element().getIdentifier().equals(hub.getIdentifier())) {
                removedVertex = vertex;
                graph.removeVertex(vertex);
            }
        return removedVertex;
    }

    // Add a given Route to the graph, returns the created Route
    public Edge<Route, Hub> insertRoute(Graph<Hub, Route> graph, Route newRoute) throws ExistingRouteException {
        List<Edge> edges = new ArrayList<>(graph.edges());
        // Validation: this Route already exists
        for (Edge<Route, Hub> edge : edges)
            if (edge.element().hasSameHubs(newRoute))
                throw new ExistingRouteException();
        return graph.insertEdge(newRoute.getHubOrigin(), newRoute.getHubDestination(), newRoute);
    }

    // Remove a given Route from the graph, returns the removed Edge
    public Edge<Route, Hub> removeRoute(Graph<Hub, Route> graph, Route route) throws NonExistingRouteException {
        // Validation: this Route doesn't exists
        if (route == null) throw new NonExistingRouteException();
        Edge<Route, Hub> edge = getEdge(graph, route);
        graph.removeEdge(edge);
        return edge;
    }

    // Get the Vertex corresponding to the given Hub, returns null if it doesn't find
    private Vertex<Hub> getVertex(Graph<Hub, Route> graph, Hub hub) {
        List<Vertex> vertices = new ArrayList<>(graph.vertices());
        for (Vertex<Hub> vertex : vertices)
            if (vertex.element().getIdentifier().equals(hub.getIdentifier()))
                return vertex;
        return null;
    }

    // Get the Edge corresponding to the given Route, returns null if it doesn't find
    private Edge<Route, Hub> getEdge(Graph<Hub, Route> graph, Route route) {
        List<Edge> edges = new ArrayList<>(graph.edges());
        for (Edge<Route, Hub> edge : edges)
            if (edge.element().hasSameHubs(route))
                return edge;
        return null;
    }

    // Returns a Hub, given the hub identifier
    public Hub getHub(Graph<Hub, Route> graph, String identifier) {
        List<Vertex> vertices = new ArrayList<>(graph.vertices());
        for (Vertex<Hub> vertex : vertices)
            if (vertex.element().getIdentifier().equals(identifier))
                return vertex.element();
        return null;
    }

    // Returns the Route that connects two given Hubs, null if they are not connected
    public Route getRoute(Graph<Hub, Route> graph, Hub origin, Hub destination) {
        List<Edge> edges = new ArrayList<>(graph.edges());
        for (Edge<Route, Hub> edge : edges)
            if (edge.element().containsHub(origin) && edge.element().opposite(origin).equals(destination))
                return edge.element();
        return null;
    }

    // Returns a Map of all the Hubs (Key) and their corresponding adjacency (Value)
    public Map<Hub, Integer> hubCentrality(Graph<Hub, Route> graph) {
        List<Vertex> vertices = new ArrayList<>(graph.vertices());
        Map<Hub, Integer> map = new HashMap<>();
        for (Vertex<Hub> vertex : vertices)
            map.put(vertex.element(), countNeighbors(graph, vertex.element()));
        return map;
    }

    // Returns a list of all the neighboring hubs from a given Hub
    public List<Hub> getNeighbors(Graph<Hub, Route> graph, Hub hub) {
        List<Edge> edges = new ArrayList<>(graph.edges());
        List<Hub> list = new ArrayList<>();
        for (Edge<Route, Hub> edge : edges)
            if (edge.element().containsHub(hub))
                list.add(edge.element().opposite(hub));
        return list;
    }

    // Returns the number of neighbor hubs from a given Hub
    public int countNeighbors(Graph<Hub, Route> graph, Hub hub) {
        return getNeighbors(graph, hub).size();
    }

    // Returns the 5 top Hubs with the most adjacency (from method hubCentrality()), on descending order
    public List<Hub> top5CentralHubs(Graph<Hub, Route> graph) {
        List<Hub> list = new ArrayList<>();
        List<Map.Entry<Hub, Integer>> entriesList = new ArrayList<>(hubCentrality(graph).entrySet());
        entriesList.sort(Map.Entry.comparingByValue());
        for (int i = entriesList.size() - 1; i > entriesList.size() - 6; i--)
            list.add(entriesList.get(i).getKey());
        return list;
    }

    // Returns a boolean value if a given Hub is isolated
    public boolean isIsolated(Graph<Hub, Route> graph, Hub hub) {
        List<Edge> edges = new ArrayList<>(graph.edges());
        for (Edge<Route, Hub> edge : edges)
            if (edge.element().containsHub(hub))
                return false;
        return true;
    }

    // Returns a list of Hubs that are isolated
    public List<Hub> getIsolatedHubs(Graph<Hub, Route> graph) {
        List<Vertex> vertices = new ArrayList<>(graph.vertices());
        List<Hub> list = new ArrayList<>();
        for (Vertex<Hub> vertex : vertices)
            if (isIsolated(graph, vertex.element()))
                list.add(vertex.element());
        return list;
    }

    // Returns the number of total isolated Hubs
    public int countIsolatedHubs(Graph<Hub, Route> graph) {
        return getIsolatedHubs(graph).size();
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
    // Returns a list of Hubs which their path goes through less or equal to a threshold value from a certain Hub
    public List<Hub> closeHubs(Hub hub, int threshold) {
        return null;
    }

    // Returns a boolean value, if two given Hubs are neighbors
    public boolean hubsAreNeighbors(Graph<Hub, Route> graph, Hub origin, Hub destination) {
        return getRoute(graph, origin, destination) != null;
    }

    // Returns a collection of all the visited Hubs, by breadth first order, starting at a root Hub
    public List<Hub> breadthFirstSearch(Graph<Hub, Route> graph, Hub root) {
        Set<Hub> visited = new HashSet<>();
        Queue<Hub> queue = new LinkedList<>();
        List<Hub> list = new ArrayList<>();
        visited.add(root);
        queue.add(root);
        while (!queue.isEmpty()) {
            Hub v = queue.remove();
            list.add(v);
            for (Hub elem : getNeighbors(graph, v))
                if (!visited.contains(elem)) {
                    queue.add(elem);
                    visited.add(elem);
                }
        }
        return list;
    }

    // Returns a collection of all the visited Hubs, by depth first order, starting at a root Hub
    public List<Hub> depthFirstSearch(Graph<Hub, Route> graph, Hub root) {
        Set<Hub> visited = new HashSet<>();
        Stack<Hub> stack = new Stack<>();
        List<Hub> list = new ArrayList<>();
        visited.add(root);
        stack.add(root);
        while (!stack.isEmpty()) {
            Hub v = stack.pop();
            list.add(v);
            for (Hub elem : getNeighbors(graph, v))
                if (!visited.contains(elem)) {
                    stack.add(elem);
                    visited.add(elem);
                }
        }
        return list;
    }

    // Saves a file in a specified folder with all the current routes, returns the generated file name
    public String saveRoutes(Graph<Hub, Route> graph, List<Vertex<Hub>> vertices, List<Edge<Route, Hub>> edges, String folderName) {
        FileWriter fileWriter = new FileWriter();
        fileWriter.matrixToList(getRoutesMatrix(graph, vertices, edges));
        return fileWriter.saveFile(folderName);
    }

    // Returns the number of the graph components
    public int components(Graph<Hub, Route> graph) {
        List<Vertex> vertices = new ArrayList<>(graph.vertices());
        int components = 0;
        List<Hub> visitedHubs = new ArrayList<>();
        for (Vertex<Hub> vertex : vertices)
            if (!visitedHubs.contains(vertex.element())) {
                components++;
                visitedHubs.addAll(breadthFirstSearch(graph, vertex.element()));
            }
        return components;
    }

    // Returns a matrix with all current available routes
    private int[][] getRoutesMatrix(Graph<Hub, Route> graph, List<Vertex<Hub>> vertices, List<Edge<Route, Hub>> edges) {
        int size = vertices.size();
        int[][] matrix = new int[size][size];
        for (int row = 0; row < size; row++)
            for (int column = 0; column < size; column++)
                if (hubsAreNeighbors(graph, vertices.get(row).element(), vertices.get(column).element()))
                    matrix[row][column] = getRoute(graph, vertices.get(row).element(), vertices.get(column).element()).getDistance();
                else
                    matrix[row][column] = 0;
        return matrix;
    }

    public Vertex<Hub> getVertex(Graph<Hub, Route> graph,String identifier){

        List<Vertex<Hub>> vertices = new ArrayList<>(graph.vertices());

        for(Vertex<Hub> elem: vertices){

            if(elem.element().getIdentifier().equals(identifier)){

                return elem;

            }

        }
        return null;

    }

    //Vai toma
    /*
    * private static void dijkstra(Vertex<Local> orig,
                      Map<Vertex<Local>, Double> costs,
                      Map<Vertex<Local>, Vertex<Local>> predecessors, Graph<Local,Bridge> graph){

    Set<Vertex<Local>> unvisited = new HashSet<>(graph.vertices());

    for(Vertex<Local> v : unvisited)
        costs.put(v,Double.MAX_VALUE);
    costs.put(orig,0.0);
    Vertex<Local> u;
    while(!unvisited.isEmpty()){

        u = findMinVertex(unvisited,costs);
        if(costs.get(u)==Double.MAX_VALUE) return; //case there is no path
        unvisited.remove(u); //mark as visited
        for(Edge<Bridge,Local> e :graph.incidentEdges(u)){

            Vertex v = graph.opposite(u,e); //adjacent vertex to u
            double costV = e.element().getCost() + costs.get(u);
            if(costV < costs.get(v)){ //I found a cheaper path, so Im going to replace the value

                costs.put(v,costV);
                predecessors.put(v,u);

            }

        }

    }

}

private static Vertex<Local> findMinVertex(Set<Vertex<Local>> unvisited, Map<Vertex<Local>, Double> costs) {

    Vertex<Local> vMin = null;
    double minCost = Double.MAX_VALUE;

    for(Vertex<Local> v :unvisited){

        if(costs.get(v) < minCost)
            vMin=v;
            minCost=costs.get(v);

    }

    return vMin;

}

public static double minimumCostPath(Vertex<Local> orig, Vertex<Local> dst, List<Vertex<Local>> localsPath,Graph<Local,Bridge> graph){

    Map<Vertex<Local>, Double> costs = new HashMap<>();
    Map<Vertex<Local>, Vertex<Local>> predecessors = new HashMap<>();
    dijkstra(orig,costs,predecessors,graph);
    Vertex<Local>  v =dst;
    while(v!=orig){

        localsPath.add(0,v);
        v = predecessors.get(v);
    }
    localsPath.add(0,v);
    return costs.get(dst);

}
    *
    * */
    private Vertex<Hub> findMinVertex(Set<Vertex<Hub>> unvisited, Map<Vertex<Hub>, Double> costs) {

        Vertex<Hub> vMin = null;
        double minCost = Double.MAX_VALUE;
        System.out.println(minCost);
        for (Vertex<Hub> v : unvisited) {
            if (costs.get(v) < minCost){
                vMin = v;
                minCost = costs.get(v);
            }
            System.out.println("Ult cust "+costs.get(v));

        }
        if(vMin == null){
            System.out.println("Eita diacho");
        }
        return vMin;

    }

    public double minimumCostPath(Vertex<Hub> orig, Vertex<Hub> dst, List<Vertex<Hub>> localsPath, Graph<Hub, Route> graph) {

        Map<Vertex<Hub>, Double> costs = new HashMap<>();
        Map<Vertex<Hub>, Vertex<Hub>> predecessors = new HashMap<>();
        dijkstra(orig, costs, predecessors, graph);
        Vertex<Hub> v = dst;
        while (v != orig) {

            localsPath.add(0, v);
            v = predecessors.get(v);
        }
        localsPath.add(0, v);
        return costs.get(dst);


    }

    private void dijkstra(Vertex<Hub> orig,
                                 Map<Vertex<Hub>, Double> costs,
                                 Map<Vertex<Hub>, Vertex<Hub>> predecessors, Graph<Hub,Route> graph){

        Set<Vertex<Hub>> unvisited = new HashSet<>(graph.vertices());

        for(Vertex<Hub> v : unvisited)
            costs.put(v,Double.MAX_VALUE);
        costs.put(orig,0.0);
        Vertex<Hub> u;
        while(!unvisited.isEmpty()){
            System.out.println("PRINT 1 "+unvisited);
            System.out.println("PRINT 2 "+costs);
            u = findMinVertex(unvisited,costs);
            if(costs.get(u)==Double.MAX_VALUE) return; //case there is no path
            unvisited.remove(u); //mark as visited
            for(Edge<Route,Hub> e :graph.incidentEdges(u)){

                Vertex v = graph.opposite(u,e); //adjacent vertex to u
                double costV = e.element().getDistance() + costs.get(u);
                if(costV < costs.get(v)){ //I found a cheaper path, so Im going to replace the value

                    costs.put(v,costV);
                    predecessors.put(v,u);

                }

            }

        }

    }



}
