package com.nguyen.andy.kisetsu;

import com.nguyen.andy.kisetsu.parsers.AnimeCatalogParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Andy on 7/20/2016.
 */
public class AnimeCatalogParserTest {
    private static final String TEST_URL = "http://myanimelist.net/anime/season/2017/winter";
    private static final String TEST_HTML =
            "<div class=\"js-anime30485 tile-unit lazyload seasonal-unit js-seasonal-unit\" style=\"\" data-bg=\"http://cdn.myanimelist.net/images/anime/8/72908.jpg\">\n" +
            "            <a class=\"icon-watch-status triangle\" href=\"http://myanimelist.net/login.php?from=%2Fanime%2Fseason%2F2017%2Fwinter\" data-class=\"0\"></a>\n" +
            "            <div class=\"information\">\n" +
            "                <div class=\"title\">ChäoS;Child</div>\n" +
            "                <div class=\"misc\"><span class=\"type di-ib\">TV(?)</span><span class=\"score icon-score di-ib ml4\">N/A</span><span class=\"member icon-member di-ib ml4\">10,420</span></div>\n" +
            "            </div>\n" +
            "            <a href=\"http://myanimelist.net/anime/30485/ChäoS_Child\" class=\"thumb\"></a>\n" +
            "        </div>";
    private static final String FILENAME = "winter2017.html.test";
    AnimeCatalogParser parser;

    @Test
    public void testParseAnimeList() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(FILENAME);
        parser = new AnimeCatalogParser(new File(resource.getPath()), TEST_URL);

        ArrayList<AnimeItem> airingAnime = parser.parseAnimeList();

        assertEquals(11, airingAnime.size());

        for (AnimeItem aItem : airingAnime) {
            assertNotEquals(null, aItem.getImageUrl());
            assertNotEquals(null, aItem.getMalURL());
            assertNotEquals(null, aItem.getTitle());
        }
    }

    @Test
    public void testBadTitle() throws Exception {
        String badHTML = "<div> <div> <span class=\"title\">please don't fail</span> </div> </div>";
        Element animeDiv = Jsoup.parse(badHTML).select("div").first();

        assertEquals("ERROR FETCHING TITLE", AnimeCatalogParser.parseTitle(animeDiv));

    }

    @Test
    public void testParseTitle() throws Exception {
        Element animeDiv = Jsoup.parse(TEST_HTML).select("div").first();

        assertEquals("ChäoS;Child", AnimeCatalogParser.parseTitle(animeDiv));
    }

    @Test
    public void testParseImgUrl() throws Exception {
        Element animeDiv = Jsoup.parse(TEST_HTML).select("div").first();

        assertEquals("http://cdn.myanimelist.net/images/anime/8/72908.jpg", AnimeCatalogParser.parseImgUrl(animeDiv));
    }

    @Test
    public void testBadImgUrl() throws Exception {
        String badHTML = "<div> <div> <span data-bg=\"http://cdn.myanimelist.net/images/anime/8/72908.jpg\"></span></div></div>";
        Element animeDiv = Jsoup.parse(badHTML).select("div").first();

        assertEquals("", AnimeCatalogParser.parseImgUrl(animeDiv));
    }

    @Test
    public void testParseMalUrl() throws Exception {
        Element animeDiv = Jsoup.parse(TEST_HTML).select("div").first();

        assertEquals("http://myanimelist.net/anime/30485/ChäoS_Child", AnimeCatalogParser.parseMalUrl(animeDiv));
    }

    @Test
    public void testBadMalUrl() throws Exception {
        String badHTML = "<div> <div class=\"thumb\"> <a href=\"pleasepass.com\"> </a> </div> </div>";
        Element animeDiv = Jsoup.parse(badHTML).select("div").first();

        assertEquals("ERROR FETCHING MAL URL", AnimeCatalogParser.parseMalUrl(animeDiv));
    }
}