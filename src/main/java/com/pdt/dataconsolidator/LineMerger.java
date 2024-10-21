package com.pdt.dataconsolidator;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;

public class LineMerger {
    private final MultipleFileReader reader;
    private final BufferedWriter writer;

    public LineMerger(MultipleFileReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * Assumes files in MultipleFileReader
     * @throws IOException when unexpected I/O error occurs
     */
    public void merge() throws IOException {
        // Create string array with length of number of input files
        String[] curLines = new String[reader.getReaders().size()];

        for (int i=0; i<reader.getReaders().size(); i++) {
            curLines[i] = reader.nextNonBlankLine(i);
        }

        // Creating a hash set to avoid duplicate lines across all files
        HashSet<String> seenLines = new HashSet<String>();
        // breaks when all files are done being read
        while (true) {
            String minLine = null;
            int minReaderIndex = -1;

            // Find the next smallest line
            for (int i = 0; i < curLines.length; i++) {
                if (curLines[i] != null && (minLine == null || curLines[i].compareTo(minLine) < 0)) {
                    minLine = curLines[i];
                    minReaderIndex = i;
                }
            }

            // If all curLines are null, we are done
            if (minReaderIndex == -1) {
                break;
            }

            // If line hasn't been seen already, write to output file
            if (seenLines.add(minLine)) {
                writer.write(minLine);
                writer.newLine();
            }

            // Get next line from the file that just had a line written to output
            curLines[minReaderIndex] = reader.nextNonBlankLine(minReaderIndex); // Update with next line or null if end of file
        }
    }
}
