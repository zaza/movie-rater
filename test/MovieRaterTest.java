import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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

		parser.parse("test/brothers.html");
		it = MovieRater.search(parser.getDocument(), "brothers", "test");
		assertNotNull(it);
		assertEquals("Bracia / Brothers", it.title);
		
		parser.parse("test/control.html");
		it = MovieRater.search(parser.getDocument(), "control", "test");
		assertNotNull(it);
		assertEquals("Control", it.title);
		
		parser.parse("test/droga-bez-powrotu-3.html");
		it = MovieRater.search(parser.getDocument(), "droga bez powrotu 3", "test");
		assertNotNull(it);
		assertEquals("Droga bez powrotu 3", it.title);
	}
	
	// couldn't find:
	// za chwile dalszy ciag programu
	// the notebook pamietnik > http://www.filmweb.pl/Pamietnik
	// legacy > http://www.filmweb.pl/film/Dziedzictwo-1978-33474
	
	public void testCache() throws IOException {
		Map map = new HashMap();
		map.put("A", new Item("A", "a title", 9, "a category"));
		map.put("B", new Item("B", "b title ", 0.0f, "")); // not found
		map.put("C", new Item("C", "c title", 7.5f, "c category"));
		
		MovieRater.writeSortedItemsToFile(map, "test.txt");
		
		Map cachedMap = MovieRater.readFromFile("test.txt");
		
		assertNotNull(cachedMap.get("A"));
		assertNull(cachedMap.get("B"));
		assertNotNull(cachedMap.get("C"));
		assertNull(cachedMap.get("D"));
		
		new File("test.txt").delete();
	}
}
