import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.cyberneko.html.parsers.DOMParser;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ItemSearchTest extends TestCase {

	DOMParser parser;
	List<Item> results;
	WebClient webClient;
	String workdir;

	@Override
	protected void setUp() throws Exception {
		parser = new DOMParser();
		results = new ArrayList<Item>();
		webClient = new WebClient(BrowserVersion.FIREFOX_3);
		webClient.setThrowExceptionOnScriptError(false);
		webClient.setJavaScriptEnabled(false);
		workdir = System.getProperty("user.dir");

	}

	@Override
	protected void tearDown() throws Exception {
		results.clear();
		webClient.closeAllWindows();
	}

	public void testBrothers() throws SAXException, IOException,
			URISyntaxException {
		HtmlPage page = webClient.getPage("file:/" + workdir
				+ "/test/data/results/brothers.html");
		MovieRater.search(page, "brothers", "test", results);
		Item it = MovieRater.findBestMatch("brothers", results);
		assertNotNull(it);
		assertEquals("Bracia / Brothers", it.title);
	}

	public void testWatchmenMotionComics() throws SAXException, IOException {
		HtmlPage page = webClient.getPage("file:/" + workdir
				+ "/test/data/results/watchmen-motion-comics.html");
		MovieRater.search(page, "watchmen motion comics", "test", results);
		Item it = MovieRater.findBestMatch("watchmen motion comics", results);
		assertNotNull(it);
		assertEquals("Watchmen", it.title);
	}

	public void testCash() throws SAXException, IOException {
		HtmlPage page = webClient.getPage("file:/" + workdir
				+ "/test/data/results/cash.html");
		MovieRater.search(page, "cash", "test", results);
		Item it = MovieRater.findBestMatch("cash", results);
		assertNotNull(it);
		assertEquals("Cash", it.title);
	}

	public void testControl() throws SAXException, IOException {
		HtmlPage page = webClient.getPage("file:/" + workdir
				+ "/test/data/results/control.html");
		MovieRater.search(page, "control", "test", results);
		Item it = MovieRater.findBestMatch("control", results);
		assertNotNull(it);
		assertEquals("Control", it.title);
	}

	public void testDrogaBezPowrotu2() throws SAXException, IOException {
		HtmlPage page = webClient.getPage("file:/" + workdir
				+ "/test/data/results/droga-bez-powrotu-2.html");
		MovieRater.search(page, "droga bez powrotu 2", "test", results);
		Item it = MovieRater.findBestMatch("droga bez powrotu 2", results);
		assertNotNull(it);
		assertEquals("Droga bez powrotu 2 / Wrong Turn 2: Dead End", it.title);
	}

	public void testDrogaBezPowrotu3() throws SAXException, IOException {
		HtmlPage page = webClient.getPage("file:/" + workdir
				+ "/test/data/results/droga-bez-powrotu-3.html");
		MovieRater.search(page, "droga bez powrotu 3", "test", results);
		Item it = MovieRater.findBestMatch("droga bez powrotu 3", results);
		assertNotNull(it);
		assertEquals(
				"Droga bez powrotu 3: Zostawieni na śmierć / Wrong Turn 3: Left for Dead",
				it.title);
	}

	public void testRoom6() throws SAXException, IOException {
		HtmlPage page = webClient.getPage("file:/" + workdir
				+ "/test/data/results/room-6.html");
		MovieRater.search(page, "room 6", "test", results);
		Item it = MovieRater.findBestMatch("room 6", results);
		assertNotNull(it);
		assertEquals("Room 6", it.title);
	}

	public void testGodsMustBeCrazy() throws SAXException, IOException {
		HtmlPage page = webClient.getPage("file:/" + workdir
				+ "/test/data/results/gods-must-be-crazy.html");
		MovieRater.search(page, "gods must be crazy", "test", results);
		Item it = MovieRater.findBestMatch("gods must be crazy", results);
		assertNotNull(it);
		assertEquals("Bogowie muszą być szaleni / Gods Must Be Crazy, The",
				it.title);
	}

	// TODO:
	// couldn't find:
	// wsciekłe pięści węża >
	// http://www.filmweb.pl/film/W%C5%9Bciek%C5%82e+pi%C4%99%C5%9Bci+W%C4%99%C5%BCa-2006-311861
	// the notebook pamietnik > http://www.filmweb.pl/Pamietnik
	// za chwile dalszy ciag programu
	// legacy > http://www.filmweb.pl/film/Dziedzictwo-1978-33474
	// happy tree friends christmas special

	// wrong match:
	// 'snl'... 7.75 comedy SNL{@dvd} -> SNL Fanatic
}
