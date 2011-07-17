import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ItemSearchTest extends TestCase {

	List<Item> results;
	WebClient webClient;
	String workdir;

	@Override
	protected void setUp() throws Exception {
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
	
	public void testWscieklePiesciWeza() throws SAXException, IOException {
		HtmlPage page = webClient.getPage("file:/" + workdir
				+ "/test/data/results/wsciekle-piesci-weza.html");
		MovieRater.search(page, "wsciekłe pięści węża", "test", results);
		Item it = MovieRater.findBestMatch("gods must be crazy", results);
		assertNotNull(it);
		assertEquals("Wściekłe pięści Węża", it.title);
	}

	public void testKsiazePersiPiaskiCzasu() throws SAXException, IOException {
		HtmlPage page = webClient.getPage("file:/" + workdir
				+ "/test/data/results/ksiaze-persji-piaski-czasu.html");
		MovieRater.search(page, "ksiaze persji piaski czasu", "test", results);
		Item it = MovieRater.findBestMatch("ksiaze persji piaski czasu", results);
		assertNotNull(it);
		assertEquals("Książę Persji: Piaski czasu / Prince of Persia: The Sands of Time", it.title);
	}

	public void testBrothersBloom() throws SAXException, IOException {
		HtmlPage page = webClient.getPage("file:/" + workdir
				+ "/test/data/results/brothers-bloom.html");
		MovieRater.search(page, "brothers bloom", "test", results);
		Item it = MovieRater.findBestMatch("brothers bloom", results);
		assertNotNull(it);
		assertEquals("Niesamowici bracia Bloom / Brothers Bloom, The", it.title);
	}

	public void testLifeOfBrian() throws SAXException, IOException {
		HtmlPage page = webClient.getPage("file:/" + workdir
				+ "/test/data/results/life-of-brian.html");
		MovieRater.search(page, "life of brian", "test", results);
		Item it = MovieRater.findBestMatch("life of brian", results);
		assertNotNull(it);
		assertEquals("Żywot Briana / Life of Brian", it.title);
	}
}
