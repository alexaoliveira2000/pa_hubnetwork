package com.brunomnsilva.smartgraph.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class FileWriter {

    List<String> file;

    public FileWriter() {
        this.file = new ArrayList<>();
    }

    // Returns a list of rows, based on a received squared Integer matrix
    public void matrixToList(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            String row = "";
            for (int j = 0; j < matrix.length; j++)
                row = row + " " + matrix[i][j];
            file.add(row);
        }
    }

    // Saves a file in a given folder
    public void saveFile(String folderName) {
        File folder = new File("dataset/" + folderName);
        File[] listOfFiles = folder.listFiles();
        int lastSave;
        if (listOfFiles == null || listOfFiles.length == 0)
            lastSave = -1;
        else {
            List<Integer> files = new ArrayList<>();
            for (File file : listOfFiles)
                files.add(Integer.valueOf(file.getName().substring(folderName.length() + 1, file.getName().indexOf(".txt"))));
            Collections.reverse(files);
            lastSave = files.get(0);
        }
        try {
            PrintWriter out = new PrintWriter("dataset/" + folderName + "/" + folderName + "_" + String.valueOf(lastSave + 1) + ".txt");
            for (String line : this.file)
                out.println(line);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
