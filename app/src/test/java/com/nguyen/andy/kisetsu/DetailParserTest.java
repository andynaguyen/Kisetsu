package com.nguyen.andy.kisetsu;

import com.nguyen.andy.kisetsu.parsers.DetailParser;

import junit.framework.TestCase;

import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * Created by Andy on 7/20/2016.
 */
public class DetailParserTest extends TestCase {
    private static final String TEST_URL = "http://myanimelist.net/anime/32949/Kuzu_no_Honkai";
    private static final String FILENAME = "kuzu_no_honkai.html";
    private static final String TEST_SYNOPSIS =
            "Seventeen-year-old Mugi Awaya and Hanabi Yasuraoka appear to be the ideal couple. "
            + "They are both pretty popular, and they seem to suit each other well. However, "
            + "outsiders don't know of the secret they share. Both Mugi and Hanabi have "
            + "hopeless crushes on someone else, and they are only dating each other to soothe "
            + "their loneliness. Mugi is in love with Akane Minagawa, a young teacher who used to "
            + "be his home tutor. Hanabi is also in love with a teacher, a young man who has been "
            + "a family friend since she was little. In each other, they find a place where they "
            + "can grieve for the ones they cannot have, and they share physical intimacy driven "
            + "by loneliness. Will things stay like this for them forever? (Source: MangaHelpers)";

    DetailParser parser;

    @Override
    protected void setUp() {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(FILENAME);
        parser = new DetailParser(new File(resource.getPath()), TEST_URL);
    }

    @Test
    public void testBadUrl() throws Exception {
        DetailParser invalid = new DetailParser("http://google.com/");
        assertEquals(invalid.parseSynopsis(), "ERROR FETCHING SYNOPSIS");
        assertEquals(invalid.parseScore(), "ERROR FETCHING SCORE");
    }

    @Test
    public void testGetSynopsis() throws Exception {
        assertEquals(TEST_SYNOPSIS, parser.parseSynopsis());
    }

    @Test
    public void testGetScore() throws Exception {
        //DetailParser dp = new DetailParser("http://myanimelist.net/anime/30015/ReLIFE");
        assertEquals("N/A", parser.parseScore());
    }

    @Test
    public void testGetGeneralInfo() throws Exception {

    }
}