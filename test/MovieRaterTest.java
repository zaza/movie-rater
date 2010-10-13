import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyberneko.html.parsers.DOMParser;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

public class MovieRaterTest extends TestCase {
	public void testExtractMovieName() {
		assertEquals("atramentowe serce", MovieRater.extractMovieName("AtramentoweSerceDVDRipLektorPL"));
		assertEquals("lody na patyku", MovieRater.extractMovieName("LodyNaPatyku"));
		assertEquals("notebook pamietnik", MovieRater.extractMovieName("The_Notebook_-_Pamietnik_2004_DVDRip_LektorPL"));
		assertEquals("kto nigdy nie", MovieRater.extractMovieName("Kto_nigdy_nie_2006_DVDRip_RMVB_PL_audioksiazki.org"));
		assertEquals("ed wood", MovieRater.extractMovieName("Ed.Wood.1994.DVDRip.DivX-MDX"));
		
		assertEquals("futurama into wild green yonder", MovieRater.extractMovieName("Futurama.Into.The.Wild.Green.Yonder.2009.PROPER.DVDRiP.XViD-UNTOUCHED"));
		assertEquals("after sunset", MovieRater.extractMovieName("After.the.Sunset.DVDrip.XviD-KJS"));
	}
	
	public void testItemSearch() throws SAXException, IOException {
		DOMParser parser = new DOMParser();
		Item it = null;

		parser.parse("test/data/results/brothers.html");
		it = MovieRater.search(parser.getDocument(), "brothers", "test");
		assertNotNull(it);
		assertEquals("Bracia / Brothers", it.title);

		parser.parse("test/data/results/watchmen-motion-comics.html");
		it = MovieRater.search(parser.getDocument(), "watchmen motion comics", "test");
		assertNotNull(it);
		assertEquals("Watchmen", it.title);

		parser.parse("test/data/results/cash.html");
		it = MovieRater.search(parser.getDocument(), "cash", "test");
		assertNotNull(it);
		assertEquals("Cash", it.title);
		
		parser.parse("test/data/results/control.html");
		it = MovieRater.search(parser.getDocument(), "control", "test");
		assertNotNull(it);
		assertEquals("Control", it.title);
		
		parser.parse("test/data/results/droga-bez-powrotu-3.html");
		it = MovieRater.search(parser.getDocument(), "droga bez powrotu 3", "test");
		assertNotNull(it);
		assertEquals("Droga bez powrotu 3", it.title);
	}
	
	// couldn't find:
	// za chwile dalszy ciag programu
	// the notebook pamietnik > http://www.filmweb.pl/Pamietnik
	// legacy > http://www.filmweb.pl/film/Dziedzictwo-1978-33474
	// wsciekłe pięści węża
	// happy tree friends christmas special
	
	// wrong match:
	// 'snl'... 7.75	comedy	SNL{@dvd} -> SNL Fanatic
	// Looking for 'room 6'... 7.38	horror	Room 6 -> Azyl / 	Panic Room
	
	public void testScanDirs() {
		Map<String, String[]> dirs = MovieRater.scanDirs("test/data", "results");
		assertEquals(1, dirs.size());
		String[] subdirs = dirs.get("category");
		List<String> subdirsList = Arrays.asList(subdirs);
		assertEquals(4, subdirsList.size());
		assertTrue(subdirsList.contains("GĘŚLĄ JAŹŃ"));
		assertTrue(subdirsList.contains("jaźń"));
		assertTrue(subdirsList.contains("ZAŻÓŁĆ"));
		assertTrue(subdirsList.contains("zażółć gęślą"));
	}

	public void testCacheWriteRead() throws IOException {
		Map<String, String[]> dirs = MovieRater.scanDirs("test/data", "results");
		String[] subdirs = dirs.get("category");
		Map map = new HashMap();
		map.put(subdirs[0], new Item(subdirs[0], "a title", 9, "category"));
		map.put(subdirs[1], new Item(subdirs[1], "b title ", 8.3f, "category"));
		map.put(subdirs[2], new Item(subdirs[2], "c title", 7.5f, "category"));
		map.put(subdirs[3], new Item(subdirs[3], "d title", 6.6f, "category"));
		map.put("E", new Item("E", "", 0.0f, "category")); // not found
		
		MovieRater.writeSortedItemsToFile(map /*not sorted*/, "test/data/test.txt");
		
		Map cachedMap = MovieRater.readCache("test/data/");
		
		assertEquals(4, cachedMap.size());
		assertNotNull(cachedMap.get("GĘŚLĄ JAŹŃ"));
		assertNotNull(cachedMap.get("jaźń"));
		assertNotNull(cachedMap.get("ZAŻÓŁĆ"));
		assertNotNull(cachedMap.get("zażółć gęślą"));
		assertNull(cachedMap.get("E"));
	}
	
	public void testCacheReadDoesntExist() {
		Map<String, String[]> dirs = MovieRater.scanDirs("test/data/category/doesntExist", "");
		assertTrue(dirs.isEmpty());
	}
	
	public void testDontSaveNonExisting() throws IOException, SAXException {
		Map<String, String[]> dirs = MovieRater.scanDirs("test/data", "results");
		String[] subdirs = dirs.get("category");
		Map map = new HashMap();
		map.put(subdirs[0], new Item(subdirs[0], "a title", 9, "category"));
		map.put(subdirs[1], new Item(subdirs[1], "b title ", 8.3f, "category"));
		map.put(subdirs[2], new Item(subdirs[2], "c title", 7.5f, "category"));
		map.put(subdirs[3], new Item(subdirs[3], "d title", 6.6f, "category"));
		
		MovieRater.writeSortedItemsToFile(map /*not sorted*/, "test/data/test.txt");
		
		Map<String, Item> cachedMap = MovieRater.readCache("test/data");
		cachedMap.put("E", new Item("E", "e movie", 5.1f, "category")); // no longer exists in FS
		
		Map<String, Item> movieRatings = MovieRater.getMovieRatings(dirs, cachedMap);
		assertNotNull(movieRatings.get("GĘŚLĄ JAŹŃ"));
		assertNotNull(movieRatings.get("jaźń"));
		assertNotNull(movieRatings.get("ZAŻÓŁĆ"));
		assertNotNull(movieRatings.get("zażółć gęślą"));
		assertNull(movieRatings.get("E"));
	}
	
	public void testFindLatestCacheFile() {
		String file = MovieRater.findLatestCacheFile("test/data");
		assertEquals("test/data/20101010.txt", file);
	}
	
	@Override
	protected void tearDown() throws Exception {
		new File("test/data/test.txt").delete();
	}
	
}
