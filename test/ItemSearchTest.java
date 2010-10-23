import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.cyberneko.html.parsers.DOMParser;
import org.xml.sax.SAXException;

public class ItemSearchTest extends TestCase {
	
	DOMParser parser;
	List<Item> results;
	
	@Override
	protected void setUp() throws Exception {
		parser = new DOMParser();
		results = new ArrayList<Item>();
	}
	
	@Override
	protected void tearDown() throws Exception {
		results.clear();
	}
	
	public void testBrothers() throws SAXException, IOException {
		parser.parse("test/data/results/brothers.html");
		MovieRater.search(parser.getDocument(), "brothers", "test", results);
		Item it = MovieRater.findBestMatch("brothers", results);
		assertNotNull(it);
		assertEquals("Bracia / Brothers", it.title);
	}

	public void testWatchmenMotionComics() throws SAXException, IOException {
		parser.parse("test/data/results/watchmen-motion-comics.html");
		MovieRater.search(parser.getDocument(), "watchmen motion comics", "test", results);
		Item it = MovieRater.findBestMatch("watchmen motion comics", results);
		assertNotNull(it);
		assertEquals("Watchmen", it.title);
	}

	public void testCash() throws SAXException, IOException {
		parser.parse("test/data/results/cash.html");
		MovieRater.search(parser.getDocument(), "cash", "test", results);
		Item it = MovieRater.findBestMatch("cash", results);
		assertNotNull(it);
		assertEquals("Cash", it.title);
	}

	public void testControl() throws SAXException, IOException {
		parser.parse("test/data/results/control.html");
		MovieRater.search(parser.getDocument(), "control", "test", results);
		Item it = MovieRater.findBestMatch("control", results);
		assertNotNull(it);
		assertEquals("Control", it.title);
	}

	public void testDrogaBezPowrotu2() throws SAXException, IOException {
		parser.parse("test/data/results/droga-bez-powrotu-2.html");
		MovieRater.search(parser.getDocument(), "droga bez powrotu 2", "test", results);
		Item it = MovieRater.findBestMatch("droga bez powrotu 2", results);
		assertNotNull(it);
		assertEquals("Droga bez powrotu 2 / Wrong Turn 2: Dead End", it.title);
	}	
	
	public void testDrogaBezPowrotu3() throws SAXException, IOException {
		parser.parse("test/data/results/droga-bez-powrotu-3.html");
		MovieRater.search(parser.getDocument(), "droga bez powrotu 3", "test", results);
		Item it = MovieRater.findBestMatch("droga bez powrotu 3", results);
		assertNotNull(it);
		assertEquals("Droga bez powrotu 3: Zostawieni na śmierć / Wrong Turn 3: Left for Dead", it.title);
	}
	
	public void testRoom6() throws SAXException, IOException {
		parser.parse("test/data/results/room-6.html");
		MovieRater.search(parser.getDocument(), "room 6", "test", results);
		Item it = MovieRater.findBestMatch("room 6", results);
		assertNotNull(it);
		assertEquals("Room 6", it.title);
	}
	
	// TODO:
	// couldn't find:
	// wsciekłe pięści węża > http://www.filmweb.pl/film/W%C5%9Bciek%C5%82e+pi%C4%99%C5%9Bci+W%C4%99%C5%BCa-2006-311861
	// the notebook pamietnik > http://www.filmweb.pl/Pamietnik
	// za chwile dalszy ciag programu
	// legacy > http://www.filmweb.pl/film/Dziedzictwo-1978-33474
	// happy tree friends christmas special
	
	// wrong match:
	// 'snl'... 7.75	comedy	SNL{@dvd} -> SNL Fanatic
}
