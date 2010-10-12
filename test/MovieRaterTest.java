import java.io.IOException;

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
		System.out.println(it.toString());
		
		parser.parse("test/control.html");
		it = MovieRater.search(parser.getDocument(), "control", "test");
		assertNotNull(it);
		System.out.println(it.toString());
		
		parser.parse("test/droga-bez-powrotu-3.html");
		it = MovieRater.search(parser.getDocument(), "droga bez powrotu 3", "test");
		assertNotNull(it);
		System.out.println(it.toString());
	}
	
	// couldn't find:
	// za chwile dalszy ciag programu
	// the notebook pamietnik > http://www.filmweb.pl/Pamietnik
	// legacy > http://www.filmweb.pl/film/Dziedzictwo-1978-33474
}
