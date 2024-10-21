package com.pdt.dataconsolidator;

import java.io.*;
import java.util.ArrayList;

/**
 * A MultipleFileReader is responsible for managing the reading of multiple files in a given directory.
 * It implements the AutoCloseable interface for usage in try-with-resources statements, closing all file readers.
 */
public class MultipleFileReader implements AutoCloseable {
    private final ArrayList<BufferedReader> readers = new ArrayList<>();

    /**
     * Constructs a MultipleFileReader object and validates the inputDir to be an existing directory with at
     * least one file.
     * @param inputDir String path to the directory to read multiple files from
     * @throws IllegalArgumentException when inputDir is not a directory, or is empty.
     * @throws FileNotFoundException when unexpected I/O error happens reading file (file was moved during constructor)
     */
    public MultipleFileReader(String inputDir) throws IllegalArgumentException, FileNotFoundException {
        File dir = new File(inputDir);
        File[] files = dir.listFiles();

        if (files == null) {
            throw new IllegalArgumentException("Input path: " + inputDir + " is not a valid directory with files.");
        } else {
            for (File file : files) {
                BufferedReader reader = null;
                reader = new BufferedReader(new FileReader(file));
                readers.add(reader);
            }
        }
    }

    public ArrayList<BufferedReader> getReaders() {
        return readers;
    }

    /**
     * Provides a way to read a lines from one of the files, given an index to that file. Reads the file until the next
     * non-blank line (a line with non whitespace characters) is found, or the end of the file is reached.
     * @param readerIndex index in the readers list pointing to the file to read from
     * @return the line read from the file
     * @throws IOException when an unexpected I/O error occurs
     */
    public String nextNonBlankLine(int readerIndex) throws IOException {
        String line = "";
        // keep reading new lines until a non-blank one is found or the entire file is read
        while (line != null && line.trim().isEmpty()) {
            line = readers.get(readerIndex).readLine();
        }
        return line;
    }

    /**
     * Closes the BufferedReaders used to read input from each file.
     */
    @Override
    public void close() {
        for (BufferedReader reader : readers) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // Keep trying to close all readers, don't bail out
                    System.err.println("Failed to close file reader: " + e.getMessage());
                }
            }
        }
    }
}
