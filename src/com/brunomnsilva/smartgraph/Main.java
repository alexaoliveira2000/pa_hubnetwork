/* 
 * The MIT License
 *
 * Copyright 2019 brunomnsilva@gmail.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.brunomnsilva.smartgraph;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.*;
import com.brunomnsilva.smartgraph.model.*;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author brunomnsilva
 */
public class Main extends Application {

    private volatile boolean running;

    @Override
    public void start(Stage ignored) {

        NetworkController controller = new NetworkController("dataset/sgb128", "routes_2.txt");
        controller.createStage();

        // ------------------------ OLD -------------------------------------------------------------
        // Ler ficheiros
        //FileReader fileReader = new FileReader("dataset/sgb128");
        // Guardar informação
        //List<Hub> hubs = fileReader.get_hub_information();
        //int [][] routes = fileReader.get_routes();

        // Mostrar hubs e routes
        /*
        for (Hub hub : hubs)
            System.out.println(hub);
        for (int i = 1; i < routes.length; i++) {
            for (int j = 0; j < i; j++)
                System.out.print(routes[i][j] + "\t");
            System.out.println();
        }
        */

        //Graph<String, String> g = build_graph(fileReader.get_hub_information(), fileReader.get_routes());
        //Graph<String, String> g = build_sample_digraph();
        //Graph<String, String> g = build_flower_graph();
        //System.out.println(g);
        /*
        Graph<String, String> g = new GraphEdgeList<>();

        // Create Vertices
        Collection<Vertex<String>> vertices = new ArrayList<>();
        for (Hub hub : hubs)
            vertices.add(g.insertVertex(hub.getIdentifier()));

        // Create Edges
        for (int i = 1; i < routes.length; i++)
            for (int j = 0; j < i; j++)
                if (routes[i][j] != 0)
                    g.insertEdge(hubs.get(i).getIdentifier(), hubs.get(j).getIdentifier(), String.valueOf(routes[i][j]));

        /* Only Java 15 allows for multi-line strings */
        //String customProps = "edge.label = true" + "\n" + "edge.arrow = false";
        //SmartGraphProperties properties = new SmartGraphProperties(customProps);
        //SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
        //SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g,properties);
        //SmartPlacementStrategy strategy = new SmartRandomPlacementStrategy();
        //SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, properties, strategy);
        /*
        Scene scene = new Scene(new SmartGraphDemoContainer(graphView), 1024, 768);

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("JavaFX SmartGraph Visualization");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();
        */
        /*
        IMPORTANT: Must call init() after scene is displayed so we can have width and height values
        to initially place the vertices according to the placement strategy
        */
        /*
        graphView.init();

        // Vertices coordinates
        int k = 0;
        for (Vertex<String> vertex : vertices) {
            graphView.setVertexPosition(vertex, hubs.get(k).getX(), hubs.get(k).getY());
            k++;
        }

        */

        /*
        After creating, you can change the styling of some element.
        This can be done at any time afterwards.
        */

        //graphView.getStylableEdge("0").setStyle("-fx-fill: purple; -fx-stroke: purple;");

        //if (g.numVertices() > 0) {
        //    graphView.getStylableVertex("A").setStyle("-fx-fill: gold; -fx-stroke: brown;");
        //}

        /*
        Basic usage:            
        Use SmartGraphDemoContainer if you want zoom capabilities and automatic layout toggling
        */
        //Scene scene = new Scene(graphView, 1024, 768);


        /*
        Bellow you can see how to attach actions for when vertices and edges are double clicked
         */
        /*
        graphView.setVertexDoubleClickAction((SmartGraphVertex<String> graphVertex) -> {
            System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());
                      
            //toggle different styling
            if( !graphVertex.removeStyleClass("myVertex") ) {
                /* for the golden vertex, this is necessary to clear the inline
                css class. Otherwise, it has priority. Test and uncomment. */
        //graphVertex.setStyle(null);

        //graphVertex.addStyleClass("myVertex");
            //}

    //want fun? uncomment below with automatic layout
    //g.removeVertex(graphVertex.getUnderlyingVertex());
    //graphView.update();
    //});


        /*
        graphView.setEdgeDoubleClickAction(graphEdge -> {
            System.out.println("Edge contains element: " + graphEdge.getUnderlyingEdge().element());
            //dynamically change the style when clicked
            graphEdge.setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
            
            graphEdge.getStylableArrow().setStyle("-fx-stroke: black; -fx-stroke-width: 3;");
            
            //uncomment to see edges being removed after click
            //Edge<String, String> underlyingEdge = graphEdge.getUnderlyingEdge();
            //g.removeEdge(underlyingEdge);
            //graphView.update();
        });
        */


        /*
        Should proceed with automatic layout or keep original placement?
        If using SmartGraphDemoContainer you can toggle this in the UI 
         */
    //graphView.setAutomaticLayout(true);

        /* 
        Uncomment lines to test adding of new elements
         */
    //continuously_test_adding_elements(g, graphView);
    //stage.setOnCloseRequest(event -> {
    //    running = false;
    //});
}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private Graph<String, String> build_sample_digraph() {

        Digraph<String, String> g = new DigraphEdgeList<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");

        g.insertEdge("A", "B", "AB");
        g.insertEdge("B", "A", "AB2");
        g.insertEdge("A", "C", "AC");
        g.insertEdge("A", "D", "AD");
        g.insertEdge("B", "C", "BC");
        g.insertEdge("C", "D", "CD");
        g.insertEdge("B", "E", "BE");
        g.insertEdge("F", "D", "DF");
        g.insertEdge("F", "D", "DF2");

        //yep, its a loop!
        g.insertEdge("A", "A", "Loop");

        return g;
    }

    private Graph<String, String> build_flower_graph() {

        Graph<String, String> g = new GraphEdgeList<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");
        g.insertVertex("G");

        g.insertEdge("A", "B", "1");
        g.insertEdge("A", "C", "2");
        g.insertEdge("A", "D", "3");
        g.insertEdge("A", "E", "4");
        g.insertEdge("A", "F", "5");
        g.insertEdge("A", "G", "6");

        g.insertVertex("H");
        g.insertVertex("I");
        g.insertVertex("J");
        g.insertVertex("K");
        g.insertVertex("L");
        g.insertVertex("M");
        g.insertVertex("N");

        g.insertEdge("H", "I", "7");
        g.insertEdge("H", "J", "8");
        g.insertEdge("H", "K", "9");
        g.insertEdge("H", "L", "10");
        g.insertEdge("H", "M", "11");
        g.insertEdge("H", "N", "12");

        g.insertEdge("A", "H", "0");

        //g.insertVertex("ISOLATED");
        
        return g;
    }

    private static final Random random = new Random(/* seed to reproduce*/);

    private void continuously_test_adding_elements(Graph<String, String> g, SmartGraphPanel<String, String> graphView) {
        //update graph
        running = true;
        final long ITERATION_WAIT = 3000; //milliseconds

        Runnable r;
        r = () -> {
            int count = 0;
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            while (running) {
                try {
                    Thread.sleep(ITERATION_WAIT);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //generate new vertex with 2/3 probability, else connect two
                //existing
                String id = String.format("%02d", ++count);
                if (random.nextInt(3) < 2) {
                    //add a new vertex connected to a random existing vertex
                    Vertex<String> existing = get_random_vertex(g);
                    Vertex<String> vertexId = g.insertVertex(("V" + id));
                    g.insertEdge(existing, vertexId, ("E" + id));
                    
                    //this variant must be called to ensure the view has reflected the
                    //underlying graph before styling a node immediately after.
                    graphView.updateAndWait();
                    
                    //color new vertices
                    SmartStylableNode stylableVertex = graphView.getStylableVertex(vertexId);
                    if(stylableVertex != null) {
                        stylableVertex.setStyle("-fx-fill: orange;");
                    }
                } else {
                    Vertex<String> existing1 = get_random_vertex(g);
                    Vertex<String> existing2 = get_random_vertex(g);
                    g.insertEdge(existing1, existing2, ("E" + id));
                    
                    graphView.update();
                }

                
            }
        };

        new Thread(r).start();
    }

    private static Vertex<String> get_random_vertex(Graph<String, String> g) {

        int size = g.numVertices();
        int rand = random.nextInt(size);
        Vertex<String> existing = null;
        int i = 0;
        for (Vertex<String> v : g.vertices()) {
            existing = v;
            if (i++ == rand) {
                break;
            }
        }
        return existing;
    }
}
