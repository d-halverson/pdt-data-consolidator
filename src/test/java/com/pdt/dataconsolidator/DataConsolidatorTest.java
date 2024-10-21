package com.pdt.dataconsolidator;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DataConsolidatorTest {

    /**
     * Tests smaller cases that are easy to visualize by the reader.
     */
    @Test
    public void testMain_SmallCases() throws IOException {
        String[] inputDirs = {
                getClass().getClassLoader().getResource("given-test-case").getPath(),
                getClass().getClassLoader().getResource("case-sensitivity").getPath(),
                getClass().getClassLoader().getResource("dir-with-blank-file").getPath(),
                getClass().getClassLoader().getResource("dir-with-empty-file").getPath(),
                getClass().getClassLoader().getResource("dir-with-one-file").getPath(),
                "non-existent-dir", // making sure there is no crash
        };
        String[][] expectedOutput = {
                {"apple", "apple banana", "banana", "cactus", "dinosaur", "frankfurter"},
                {"APPLE", "apple", "banana", "BLUEberry", "blueberry", "cactus", "fruit", "fruits", "melon", "ORANge", "pineAPPLE", "pineapple", "watermelon", "zebra?"},
                {},
                {},
                {"apple", "banana", "cactus", "line with spaces"},
                {}
        };

        for (int i=0; i<inputDirs.length; i++) {
            File tempOutput = File.createTempFile("output", ".txt");
            DataConsolidator.main(new String[]{inputDirs[i], tempOutput.getPath()});

            String[] expectedLines = expectedOutput[i];
            try (BufferedReader br = new BufferedReader(new FileReader(tempOutput))) {
                String line;
                int tempFileLineIndex = 0;
                while ((line = br.readLine()) != null) {
                    assertEquals(expectedLines[tempFileLineIndex], line);
                    tempFileLineIndex++;
                }
                assertEquals(expectedLines.length, tempFileLineIndex); // make sure all expectedLines were checked
            } catch (IOException e) {
                fail(e.getMessage());
            } finally {
                tempOutput.delete();
            }
        }
    }

    /**
     * Tests large amounts of data and checks the output file's length for accuracy.
     */
    @Test
    public void testMain_LargeData() throws IOException {
        String[] inputDirs = {
                getClass().getClassLoader().getResource("many-random-lines").getPath(),
                getClass().getClassLoader().getResource("many-random-lines-all-unique").getPath(),
        };
        int[] expectedOutputLength = {
                10000,
                100000
        };

        for (int i=0; i<inputDirs.length; i++) {
            File tempOutput = File.createTempFile("output", ".txt");
            DataConsolidator.main(new String[]{inputDirs[i], tempOutput.getPath()});

            try (BufferedReader br = new BufferedReader(new FileReader(tempOutput))) {
                String line;
                int tempFileLineCount = 0;
                while ((line = br.readLine()) != null) {
                    tempFileLineCount++;
                }
                assertEquals(expectedOutputLength[i], tempFileLineCount); // make sure all expectedLines were checked
            } catch (IOException e) {
                fail(e.getMessage());
            } finally {
                tempOutput.delete();
            }
        }
    }
}