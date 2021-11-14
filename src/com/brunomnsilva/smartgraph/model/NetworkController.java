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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.List;

public class NetworkController {

    NetworkManager manager;
    List<Vertex<Hub>> vertices;
    List<Edge<Route,Hub>> edges;
    Graph<Hub, Route> graph;
    SmartGraphPanel<Hub, Route> graphView;
    Stage stage;

    // Initialize the Controller giving it a folder (dataset) and the specific routes_file
    public NetworkController(String folder, String routes_file) {
        this.stage = new Stage(StageStyle.DECORATED);
        initializeController(folder + "/" + routes_file);
    }

    // Initialize the Controller giving it the specific routes_file path
    public NetworkController(String path) {
        this.stage = new Stage(StageStyle.DECORATED);
        initializeController(path);
    }

    // Method called on starting the application, or when a file is imported
    private void initializeController(String path) throws NonEqualHubsException {
        String folder = path.substring(0,path.lastIndexOf("/"));
        String routes_file = path.substring(path.lastIndexOf("/") + 1);
        if (this.manager != null) {
            this.manager = new NetworkManager(path, this.manager.getHubs());
            if (this.vertices != null && this.manager.countHubs() != this.vertices.size())
                throw new NonEqualHubsException();
        } else {
            this.manager = new NetworkManager(folder, routes_file);
        }
        this.graph = new GraphEdgeList<>();
        this.vertices = createVertices(graph);
        this.edges = createEdges(graph);
        this.graphView = new SmartGraphPanel<>(graph);
    }

    // Create all the elements of the stage and run program
    public void start() {

        // Horizontal tab with the buttons: "Add Hub" | "Add Route" | "Save Routes"
        Pane tab = new HBox();
        Button button_addHub = new Button("Add Hub");
        Button button_removeHub = new Button("Remove Hub");
        Button button_addRoute = new Button("Add Route");
        Button button_removeRoute = new Button("Remove Route");
        Button button_saveRoutes = new Button("Save Routes");
        Button button_importRoutes = new Button("Import Routes");
        tab.getChildren().addAll(
                button_addHub,
                button_removeHub,
                button_addRoute,
                button_removeRoute,
                button_saveRoutes,
                button_importRoutes);
        this.graphView.getChildren().add(tab);

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

        // Add all button actions
        addHubButtonAction(stage, button_addHub);
        removeHubButtonAction(stage, button_removeHub);
        addRouteButtonAction(stage, button_addRoute);
        addRemoveRouteButtonAction(stage, button_removeRoute);
        addSaveRoutesButtonAction(stage, button_saveRoutes);
        addImportRoutesAction(stage, button_importRoutes);
    }

    // Add vertices to the graph and returns a list of all the created vertices (one for each Hub)
    private List<Vertex<Hub>> createVertices(Graph graph) {
        List<Vertex<Hub>> list = new ArrayList<>();
        for (Hub hub : manager.getHubs())
            list.add(graph.insertVertex(hub));
        return list;
    }

    // Add edges to the graph and returns a list of all the created edges (one for each Route)
    private List<Edge<Route,Hub>> createEdges(Graph graph) {
        List<Edge<Route,Hub>> list = new ArrayList<>();
        for (Route route : manager.getRoutes())
            list.add(graph.insertEdge(route.getHubOrigin(), route.getHubDestination(), route));
        return list;
    }

    // Sets the vertices coordinates
    private void setCoordinates() {
        for (int i = 0; i < this.vertices.size(); i++)
            this.graphView.setVertexPosition(this.vertices.get(i), this.manager.getHubs().get(i).getX(), this.manager.getHubs().get(i).getY());
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
    private void addHubButtonAction(Stage stage, Button button) {

    }

    // Action of the "Remove Hub" button
    private void removeHubButtonAction(Stage stage, Button button) {

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
                                else if (manager.getHub(originHubTextField.getText()) == null)
                                    System.out.println("\"Origin Hub\" doesn't exist!");
                                else if (manager.getHub(destinationHubTextField.getText()) == null)
                                    System.out.println("\"Destination Hub\" doesn't exist!");
                                else if (Integer.valueOf(distanceTextField.getText()) <= 0)
                                    System.out.println("\"Distance\" should be greater than 0!");
                                else {
                                    try {
                                        manager.insertRoute(
                                                graph,
                                                edges,
                                                new Route(
                                                        manager.getHub(originHubTextField.getText()),
                                                        manager.getHub(destinationHubTextField.getText()),
                                                        Integer.valueOf(distanceTextField.getText()))
                                        );
                                        graphView.updateAndWait();
                                        graphView.getStylableEdge(edges.get(edges.size() - 1)).setStyle("-fx-fill: green; -fx-stroke: green;");
                                        dialog.close();
                                        System.out.println("Route created!");
                                    } catch (ExistingRouteException exception) {
                                        // Mostrar texto a dizer que já existe
                                        System.out.println(exception.getMessage());
                                    }
                                }
                            }
                        });
                    }
                });
    }

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
                                else if (manager.getHub(originHubTextField.getText()) == null)
                                    System.out.println("\"Origin Hub\" doesn't exist!");
                                else if (manager.getHub(destinationHubTextField.getText()) == null)
                                    System.out.println("\"Destination Hub\" doesn't exist!");
                                else {
                                    try {
                                        manager.removeRoute(
                                                graph,
                                                edges,
                                                manager.getRoute(
                                                        manager.getHub(originHubTextField.getText()),
                                                        manager.getHub(destinationHubTextField.getText()))
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

    // Action of the "Save Routes" button
    private void addSaveRoutesButtonAction(Stage stage, Button button) {
        button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {;
                        System.out.println("Routes file saved in: " + manager.saveRoutes("saved_routes"));
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
