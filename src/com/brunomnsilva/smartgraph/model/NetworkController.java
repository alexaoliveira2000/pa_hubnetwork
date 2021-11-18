package com.brunomnsilva.smartgraph.model;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.*;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;

public class NetworkController {

    NetworkManager manager;
    Graph<Hub, Route> graph;
    List<Vertex<Hub>> vertices;
    List<Edge<Route,Hub>> edges;
    SmartGraphPanel<Hub, Route> graphView;
    Stage stage;

    // Initialize the Controller giving it a folder (dataset) and the specific routes_file
    public NetworkController(String folder, String routes_file) {
        this.stage = new Stage(StageStyle.DECORATED);
        this.graph = new GraphEdgeList<>();
        initializeController(folder + "/" + routes_file);
    }

    // Initialize the Controller giving it the specific routes_file path
    public NetworkController(String path) {
        this.stage = new Stage(StageStyle.DECORATED);
        this.graph = new GraphEdgeList<>();
        initializeController(path);
    }

    // Method called on starting the application, or when a file is imported
    private void initializeController(String path) throws NonEqualHubsException {
        this.manager = new NetworkManager(path);
        if (this.vertices == null)
            this.vertices = manager.createVertices(graph);
        this.edges = manager.createEdges(graph,this.vertices);
        this.graphView = new SmartGraphPanel<>(this.graph);
    }

    // Create all the elements of the stage and run program
    public void start() {

        //System.out.println(manager.top5CentralHubs(graph));

        // Horizontal tab with the buttons: "Add Hub" | "Remove Hub | "Add Route" | "Remove Route" | "Save Routes" | "Import Routes"
        Pane actionsTab = new HBox();
        Button button_addHub = new Button("Add Hub");
        Button button_removeHub = new Button("Remove Hub");
        Button button_addRoute = new Button("Add Route");
        Button button_removeRoute = new Button("Remove Route");
        Button button_saveRoutes = new Button("Save Routes");
        Button button_importRoutes = new Button("Import Routes");
        actionsTab.getChildren().addAll(
                button_addHub,
                button_removeHub,
                button_addRoute,
                button_removeRoute,
                button_saveRoutes,
                button_importRoutes);
        this.graphView.getChildren().add(actionsTab);

        // Horizontal tab with the metrics
        Pane metricsTab = new HBox();
        Button button_test = new Button("Test");
        metricsTab.getChildren().add(button_test);

        //vbox.getChildren().addAll(actionsTab, this.graphView, metricsTab);


        // Create scene and stage
        Scene scene = new Scene(new SmartGraphDemoContainer(this.graphView), 1024, 768);
        stage.setTitle("JavaFX SmartGraph Visualization");
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.show();

        // Initialize graphView and set initial coordinates
        this.graphView.init();
        setCoordinates();
        for (Edge<Route,Hub> edge : edges)
            graphView.getStylableEdge(edge).setStyle("-fx-stroke: red; -fx-stroke-width: 2;");

        // Add all button actions
        addHubButtonAction(stage, scene, button_addHub);
        removeHubButtonAction(stage, button_removeHub);
        addRouteButtonAction(stage, button_addRoute);
        addRemoveRouteButtonAction(stage, button_removeRoute);
        addSaveRoutesButtonAction(stage, button_saveRoutes);
        addImportRoutesAction(stage, button_importRoutes);
    }

    // Sets the vertices coordinates
    private void setCoordinates() {
        for (Vertex<Hub> vertex : this.vertices)
            this.graphView.setVertexPosition(vertex, vertex.element().getX(), vertex.element().getY());
    }

    // Generic function that creates an input field (horizontal box), and returns the text field
    private TextField createField(VBox vbox, String field) {
        HBox hbox = new HBox();
        Label label = new Label(field);
        TextField textField = new TextField();
        hbox.getChildren().addAll(label, textField);
        hbox.setSpacing(10);
        vbox.getChildren().add(hbox);
        return textField;
    }

    // Action of the "Add Hub" button
    private void addHubButtonAction(Stage stage, Scene scene, Button button) {
        button.setOnMouseClicked(
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        Vertex<Hub> vertex = graph.insertVertex(new Hub());
                        // Event Moves Mouse
                        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent1) {
                                    graphView.setVertexPosition(vertex, mouseEvent1.getSceneX(), mouseEvent1.getSceneY());
                                    graphView.update();
                            }
                        });
                        // Event Click
                        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent2) {
                                double currentX = mouseEvent2.getSceneX();
                                double currentY = mouseEvent2.getSceneY();
                                scene.setOnMouseClicked(null);
                                scene.setOnMouseMoved(null);
                                graph.removeVertex(vertex);
                                //graphView.updateAndWait();

                                final Stage dialog = new Stage();
                                dialog.setTitle("New Hub");
                                dialog.initModality(Modality.APPLICATION_MODAL);
                                dialog.initOwner(stage);
                                VBox dialogVbox = new VBox(20);

                                TextField hubCityNameTextField = createField(dialogVbox, "City Name:");
                                TextField hubStateCodeTextField = createField(dialogVbox, "State Code:");
                                TextField hubPopulationTextField = createField(dialogVbox, "Population:");
                                Button createButton = new Button("Create Hub");

                                dialogVbox.getChildren().add(createButton);

                                Scene dialogScene = new Scene(dialogVbox, 300, 200);
                                dialog.setScene(dialogScene);
                                dialog.show();

                                dialog.setOnCloseRequest(e -> {graphView.update();});

                                createButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent e) {
                                        // Validations
                                        if (hubCityNameTextField.getText().trim().isEmpty())
                                            System.out.println("\"City Name\" field is empty!");
                                        else if (hubStateCodeTextField.getText().trim().isEmpty())
                                            System.out.println("\"State Code\" field is empty!");
                                        else if (hubPopulationTextField.getText().trim().isEmpty())
                                            System.out.println("\"Population\" field is empty!");
                                        else if (manager.getHub(graph,hubCityNameTextField.getText() + ", " + hubStateCodeTextField.getText()) != null)
                                            System.out.println("This Hub already exists!");
                                        else if (Integer.valueOf(hubPopulationTextField.getText()) <= 0)
                                            System.out.println("\"Population\" should be greater than 0!");
                                        else {
                                            try {
                                                Vertex<Hub> vertex = manager.insertHub(graph, new Hub(hubStateCodeTextField.getText(),hubCityNameTextField.getText()));
                                                vertices.add(vertex);
                                                vertex.element().setCoordinates(currentX,currentY);
                                                vertex.element().setPopulation(Integer.valueOf(hubPopulationTextField.getText()));
                                                graphView.updateAndWait();
                                                graphView.setVertexPosition(vertex,currentX,currentY);
                                                //graphView.getStylableEdge(edge).setStyle("-fx-fill: green; -fx-stroke: green;");
                                                dialog.close();
                                                System.out.println("Hub created!");
                                            } catch (ExistingHubException exception) {
                                                // Mostrar texto a dizer que Hub já existe
                                                System.out.println(exception.getMessage());
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
    }

    // Action of the "Remove Hub" button
    private void removeHubButtonAction(Stage stage, Button button) {
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        graphView.setVertexDoubleClickAction(graphVertex -> {
                            Vertex<Hub> removedVertex = manager.removeHub(graph, manager.getHub(graph, graphVertex.getUnderlyingVertex().element().getIdentifier()));
                            vertices.remove(removedVertex);
                            graphView.updateAndWait();
                            //for (Vertex<Hub> vertex : vertices)
                            //graphView.getStylableVertex(vertex).setStyle("-fx-fill: blue; -fx-stroke: blue;");
                            System.out.println("Hub \"" + graphVertex.getUnderlyingVertex().element() + "\" was removed!");
                        });
                    }
                });
    }

    // Action of the "Add Route" button
    private void addRouteButtonAction(Stage stage, Button button) {
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        final Stage dialog = new Stage();
                        dialog.setTitle("New Route");
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(stage);
                        VBox dialogVbox = new VBox(20);

                        TextField originHubTextField = createField(dialogVbox, "Origin Hub:");
                        TextField destinationHubTextField = createField(dialogVbox, "Destination Hub:");
                        TextField distanceTextField = createField(dialogVbox, "Distance:");
                        Button createButton = new Button("Create Route");

                        dialogVbox.getChildren().add(createButton);

                        Scene dialogScene = new Scene(dialogVbox, 300, 200);
                        dialog.setScene(dialogScene);
                        dialog.show();

                        createButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                // Validations
                                if (originHubTextField.getText().trim().isEmpty())
                                    System.out.println("\"Origin Hub\" field is empty!");
                                else if (destinationHubTextField.getText().trim().isEmpty())
                                    System.out.println("\"Destination Hub\" field is empty!");
                                else if (distanceTextField.getText().trim().isEmpty())
                                    System.out.println("\"Distance\" field is empty!");
                                else if (manager.getHub(graph,originHubTextField.getText()) == null)
                                    System.out.println("\"Origin Hub\" doesn't exist!");
                                else if (manager.getHub(graph,destinationHubTextField.getText()) == null)
                                    System.out.println("\"Destination Hub\" doesn't exist!");
                                else if (Integer.valueOf(distanceTextField.getText()) <= 0)
                                    System.out.println("\"Distance\" should be greater than 0!");
                                else {
                                    try {
                                        Edge edge = manager.insertRoute(graph, new Route(
                                                        manager.getHub(graph,originHubTextField.getText()),
                                                        manager.getHub(graph,destinationHubTextField.getText()),
                                                        Integer.valueOf(distanceTextField.getText()))
                                        );
                                        edges.add(edge);
                                        graphView.updateAndWait();
                                        graphView.getStylableEdge(edge).setStyle("-fx-fill: green; -fx-stroke: green;");
                                        dialog.close();
                                        System.out.println("Route created!");
                                    } catch (ExistingRouteException exception) {
                                        // Mostrar texto a dizer que Route já existe
                                        System.out.println(exception.getMessage());
                                    }
                                }
                            }
                        });
                    }
                });
    }
    /*
    // Action of the "Remove Route" button
    private void addRemoveRouteButtonAction(Stage stage, Button button) {
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        final Stage dialog = new Stage();
                        dialog.setTitle("Remove Route");
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(stage);
                        VBox dialogVbox = new VBox(20);

                        TextField originHubTextField = createField(dialogVbox, "Origin Hub:");
                        TextField destinationHubTextField = createField(dialogVbox, "Destination Hub:");
                        Button removeButton = new Button("Remove Route");

                        dialogVbox.getChildren().add(removeButton);

                        Scene dialogScene = new Scene(dialogVbox, 300, 200);
                        dialog.setScene(dialogScene);
                        dialog.show();

                        removeButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                // Validations
                                if (originHubTextField.getText().trim().isEmpty())
                                    System.out.println("\"Origin Hub\" field is empty!");
                                else if (destinationHubTextField.getText().trim().isEmpty())
                                    System.out.println("\"Destination Hub\" field is empty!");
                                else if (manager.getHub(graph,originHubTextField.getText()) == null)
                                    System.out.println("\"Origin Hub\" doesn't exist!");
                                else if (manager.getHub(graph,destinationHubTextField.getText()) == null)
                                    System.out.println("\"Destination Hub\" doesn't exist!");
                                else {
                                    try {
                                        manager.removeRoute(graph, manager.getRoute(graph,
                                                        manager.getHub(graph,originHubTextField.getText()),
                                                        manager.getHub(graph,destinationHubTextField.getText()))
                                        );
                                        graphView.updateAndWait();
                                        dialog.close();
                                    } catch (NonExistingRouteException exception) {
                                        // Mostrar texto a dizer que não existe
                                        System.out.println(exception.getMessage());
                                    }
                                }
                            }
                        });
                    }
                });
    }
    */
    // Action of the "Remove Route" button
    private void addRemoveRouteButtonAction(Stage stage, Button button) {
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        // Change all styles
                        for (Edge<Route,Hub> edge : edges)
                            graphView.getStylableEdge(edge).setStyle("-fx-stroke: black; -fx-stroke-width: 5;");
                        //for (Vertex<Hub> vertex : vertices)
                            //graphView.getStylableVertex(vertex).setStyle("-fx-fill: gray; -fx-stroke: gray");

                        graphView.setEdgeDoubleClickAction(graphEdge -> {
                            Edge<Route,Hub> removedEdge = manager.removeRoute(graph, manager.getRoute(graph,
                                    graphEdge.getUnderlyingEdge().element().getHubOrigin(),
                                    graphEdge.getUnderlyingEdge().element().getHubDestination()));
                            edges.remove(removedEdge);
                            graphView.updateAndWait();
                            for (Edge<Route,Hub> edge : edges)
                                graphView.getStylableEdge(edge).setStyle("-fx-stroke: red; -fx-stroke-width: 2;");
                            //for (Vertex<Hub> vertex : vertices)
                                //graphView.getStylableVertex(vertex).setStyle("-fx-fill: blue; -fx-stroke: blue;");
                            System.out.println("Route \"" + graphEdge.getUnderlyingEdge().element().getHubOrigin() + "\" --> \"" + graphEdge.getUnderlyingEdge().element().getHubDestination() + "\" was removed!");
                        });
                    }
                });
    }

    // Action of the "Save Routes" button
    private void addSaveRoutesButtonAction(Stage stage, Button button) {
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {;
                        System.out.println("Routes file saved in: " + manager.saveRoutes(graph,vertices,edges,"saved_routes"));
                    }
                });
    }

    // Action of the "Import Routes" button
    private void addImportRoutesAction(Stage stage, Button button) {
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        final Stage dialog = new Stage();
                        dialog.setTitle("Import Routes");
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(stage);
                        VBox dialogVbox = new VBox(20);

                        TextField routesFileTextField = createField(dialogVbox, "Routes File:");
                        Button importButton = new Button("Import File");

                        dialogVbox.getChildren().add(importButton);

                        Scene dialogScene = new Scene(dialogVbox, 300, 200);
                        dialog.setScene(dialogScene);
                        dialog.show();

                        importButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                if (routesFileTextField.getText().trim().isEmpty())
                                    System.out.println("\"Routes File\" field is empty!");
                                else {
                                    try {
                                        initializeController(routesFileTextField.getText());
                                        start();
                                        graphView.update();
                                        dialog.close();
                                    } catch(NonEqualHubsException exception) {
                                        // Mostrar texto a dizer que não existe
                                        System.out.println(exception.getMessage());
                                    }
                                }
                            }
                        });
                    }
                });
    }

}
