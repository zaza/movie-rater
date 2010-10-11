import junit.framework.TestCase;

public class MovieRaterTest extends TestCase {
	public void testExtractMovieName() {
		assertEquals("atramentowe serce", MovieRater.extractMovieName("AtramentoweSerceDVDRipLektorPL"));
		assertEquals("lody na patyku", MovieRater.extractMovieName("LodyNaPatyku"));
		assertEquals("the notebook pamietnik", MovieRater.extractMovieName("The_Notebook_-_Pamietnik_2004_DVDRip_LektorPL"));
		assertEquals("kto nigdy nie", MovieRater.extractMovieName("Kto_nigdy_nie_2006_DVDRip_RMVB_PL_audioksiazki.org"));
	}
}
