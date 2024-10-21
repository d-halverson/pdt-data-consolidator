package com.pdt.dataconsolidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MultipleFileReaderTest {
    private MultipleFileReader reader;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testNextNonBlankLine_ReturnsNonBlankLine() throws IOException {
        reader = new MultipleFileReader(getClass().getClassLoader().getResource("dir-with-one-file").getPath());
        assertEquals("apple", reader.nextNonBlankLine(0));
        assertEquals("banana", reader.nextNonBlankLine(0));
        assertEquals("line with spaces", reader.nextNonBlankLine(0));
        assertEquals("cactus", reader.nextNonBlankLine(0));
        assertNull(reader.nextNonBlankLine(0));
    }

    @Test
    public void testNextNonBlankLine_AllBlankLines() throws IOException {
        reader = new MultipleFileReader(getClass().getClassLoader().getResource("dir-with-blank-file").getPath());
        assertNull(reader.nextNonBlankLine(0));
    }


    @Test
    public void testNextNonBlankLine_EndOfFile() throws IOException {
        reader = new MultipleFileReader(getClass().getClassLoader().getResource("dir-with-empty-file").getPath());
        assertNull(reader.nextNonBlankLine(0));
    }
}
