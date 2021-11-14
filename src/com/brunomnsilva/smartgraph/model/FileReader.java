package com.brunomnsilva.smartgraph.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class FileReader {

    private String folder;
    private String routes_file;

    public FileReader(String folder, String routes_file) {
        this.folder = folder;
        this.routes_file = routes_file;
    }

    // Returns a list of Hubs with all the information
    public List<Hub> readHubs() {
        List<Hub> hubs = new ArrayList<>();
        hubs = readName(hubs);
        hubs = readWeight(hubs);
        hubs = readXY(hubs);
        return hubs;
    }

    // Reads a generic file and returns a list of all the rows of the file
    private Collection<String> readFile(String fileName) {
        Collection<String> collection = new ArrayList<>();
        File file = new File(this.folder + "/" + fileName);
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.trim().isEmpty() && !line.startsWith("#"))
                    collection.add(line);
            };
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return collection;
    }

    // Reads name.txt file, adds attribute "code" and "name" to Hub, returns new modified list of Hubs
    private List<Hub> readName(List<Hub> hubs) {
        for (String line : readFile("/name.txt")) {
            String code = line.split(",")[1].trim();
            String name = line.split(",")[0].trim();
            hubs.add(new Hub(code,name));
        }
        return hubs;
    }

    // Reads weight.txt file, adds attribute "weight" to Hub, returns new modified list of Hubs
    private List<Hub> readWeight(List<Hub> hubs) {
        int i = 0;
        for (String line : readFile("/weight.txt")) {
            hubs.get(i).setPopulation(Integer.valueOf(line.trim()));
            i++;
        }
        return hubs;
    }

    // Reads xy.txt file, adds attribute "x" and "y" to Hub, returns new modified list of Hubs
    private List<Hub> readXY(List<Hub> hubs) {
        int i = 0;
        for (String line : readFile("/xy.txt")) {
            double x = Double.valueOf(line.split(" ")[0].trim());
            double y = Double.valueOf(line.split(" ")[1].trim());
            hubs.get(i).setCoordinates(x,y);
            i++;
        }
        return hubs;
    }

    // Reads routes_*.txt file, creates new Routes, returns new list of Routes
    public List<Route> readRoutes(List<Hub> hubs) throws NonEqualHubsException {
        if (readFile(this.routes_file).size() != hubs.size()) throw new NonEqualHubsException();
        List<Route> routes = new ArrayList<>();
        int row_index = 0;
        int column_index = 0;
        for (String row : readFile(this.routes_file)) {
            column_index = 0;
            for (String value : row.split(" ")) {
                if (row_index < column_index && Integer.valueOf(value) != 0)
                    routes.add(new Route(hubs.get(row_index), hubs.get(column_index), Integer.valueOf(value)));
                column_index++;
            }
            row_index++;
        }
        return routes;
    }

}
