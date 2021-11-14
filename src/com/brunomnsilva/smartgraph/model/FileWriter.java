package com.brunomnsilva.smartgraph.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
            file.add(row.trim());
        }
    }

    // Saves a file in a given folder
    public String saveFile(String folderName) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMyyyy_HHmmss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String fileName = "dataset/" + folderName + "/" + folderName + "_" + sdf1.format(timestamp) + ".txt";
        int counter = 0;
        try {
            PrintWriter out = new PrintWriter(fileName);
            for (String line : this.file)
                out.println(line);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileName;
    }

}
