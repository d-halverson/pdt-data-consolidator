package com.pdt.dataconsolidator;

import java.io.*;

/**
 * Main entry point for the DataConsolidator project. Call the main method in the following fashion:
 * Usage: java DataConsolidator <input_dir> <output_file>
 */
public class DataConsolidator {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Error, must supply exactly two arguments. Usage: java DataConsolidator <input_dir> <output_file>");
            return;
        }

        // auto closes reader and writers
        try (MultipleFileReader reader = new MultipleFileReader(args[0]);
             BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]))) {
            LineMerger lineMerger = new LineMerger(reader, writer);
            lineMerger.merge();
        } catch (IllegalArgumentException | IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
