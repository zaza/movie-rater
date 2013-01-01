import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

public class MovieRaterTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		new File("test/data/category/GĘŚLĄ JAŹŃ").mkdirs();
		new File("test/data/category/jaźń").mkdirs();
		new File("test/data/category/ZAŻÓŁĆ").mkdirs();
		new File("test/data/category/zażółć gęślą").mkdirs();
	}
	
	public void testExtractMovieName() {
		assertEquals("atramentowe serce", MovieRater.extractMovieName("AtramentoweSerceDVDRipLektorPL"));
		assertEquals("lody na patyku", MovieRater.extractMovieName("LodyNaPatyku"));
		assertEquals("notebook pamietnik", MovieRater.extractMovieName("The_Notebook_-_Pamietnik_2004_DVDRip_LektorPL"));
		assertEquals("kto nigdy nie", MovieRater.extractMovieName("Kto_nigdy_nie_2006_DVDRip_RMVB_PL_audioksiazki.org"));
		assertEquals("ed wood", MovieRater.extractMovieName("Ed.Wood.1994.DVDRip.DivX-MDX"));
		assertEquals("futurama into wild green yonder", MovieRater.extractMovieName("Futurama.Into.The.Wild.Green.Yonder.2009.PROPER.DVDRiP.XViD-UNTOUCHED"));
		assertEquals("after sunset", MovieRater.extractMovieName("After.the.Sunset.DVDrip.XviD-KJS"));
		assertEquals("centurion", MovieRater.extractMovieName("Centurion.480p.BRRip.Napisy.PL"));
		assertEquals("wybuchowa para", MovieRater.extractMovieName("Wybuchowa Para WTOPiONE NAPiSY PL")); 
		assertEquals("nausicaä z doliny wiatru", MovieRater.extractMovieName("1984 [eng]_ Nausicaä z Doliny Wiatru (Kaze no tani no Naushika)"));
		assertEquals("ksiaze persi piaski czasu", MovieRater.extractMovieName("ksiaze persi piaski czasu(2010) DVDRip"));
		assertEquals("blood of heroes", MovieRater.extractMovieName("The Blood of Heroes[1989]-DIVX.x264-RmvBusterS"));
		assertEquals("last king of scotland", MovieRater.extractMovieName("The.Last.King.of.Scotland.2006.PL.PAL.DVD9-BR"));
		assertEquals("hanna", MovieRater.extractMovieName("HANNA napisy wklejone"));
		assertEquals("of gods and men", MovieRater.extractMovieName("Of.Gods.And.Men.LiMiTED.DVDRip.XviD-DoNE"));
		assertEquals("unknown", MovieRater.extractMovieName("Unknown.2011 - skończony ;)"));
		assertEquals("rytual", MovieRater.extractMovieName("Rytual blu-rip 480p pl"));
		assertEquals("gra o tron", MovieRater.extractMovieName("Gra o tron PL.HDTV.XviD (sezon1)"));
		assertEquals("chrzest", MovieRater.extractMovieName("Chrzest film pl"));
		assertEquals("eclipse", MovieRater.extractMovieName("c.Eclipse.DVDRip.XviD-DiAMOND"));
		assertEquals("bitwa o czarnobyl", MovieRater.extractMovieName("Bitwa.O.Czarnobyl.Dokument.TVRip"));
		assertEquals("hustawka", MovieRater.extractMovieName("Hustawka film polski"));
		assertEquals("flash of genius", MovieRater.extractMovieName("Flash.Of.Genius[2008]DvDrip-aXXo"));
		assertEquals("last house on left", MovieRater.extractMovieName("The Last House On The Left[2009][Unrated Edition]DvDrip-aXXo"));
		assertEquals("80 milionow", MovieRater.extractMovieName("psig-80.milionow.2011.pl.dvdrip.xvid.avi"));
		assertEquals("madagaskar 3", MovieRater.extractMovieName("Madagaskar 3  pl  1080 p"));
		assertEquals("trucizna nasza powszednia", MovieRater.extractMovieName("Trucizna nasza powszednia.mp4"));
		assertEquals("contagion", MovieRater.extractMovieName("Contagion.2011.720p.BluRay.X264-AMIABLE"));
		assertEquals("color of money", MovieRater.extractMovieName("The Color Of Money.1986.DVDRip.x264-VGL"));
		assertEquals("intouchables", MovieRater.extractMovieName("Intouchables 2011 720p BDRip x264 ac3 sub (mkv) [greyshadow]-=-{{T.M.R.G}}"));
		assertEquals("soom breath", MovieRater.extractMovieName("Soom.Breath.2007.DVDRIP.Korean.Eng subs.soulcreeper"));
		// assertEquals("ma byc", MovieRater.extractMovieName("jest"));
	}
	
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
		Map<String, Item> map = new HashMap<String, Item>();
		map.put(subdirs[0], new Item(subdirs[0], "a title", 9, "category"));
		map.put(subdirs[1], new Item(subdirs[1], "b title ", 8.3f, "category"));
		map.put(subdirs[2], new Item(subdirs[2], "c title", 7.5f, "category"));
		map.put(subdirs[3], new Item(subdirs[3], "d title", 6.6f, "category"));
		map.put("E", new Item("E", "", 0.0f, "category")); // not found
		
		MovieRater.writeSortedItemsToFile(map /*not sorted*/, "test/data/test.txt");
		
		Map<String, Item> cachedMap = MovieRater.readCache("test/data/");
		
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
		Map<String, Item> map = new HashMap<String, Item>();
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
		delete(new File("test/data/category"));
	}
	
	public static void delete(final File f) throws IOException {
		if (!f.exists())
			return;

		if (f.isDirectory()) {
			final File[] items = f.listFiles();
			if (items != null) {
				for (File c : items)
					delete(c);
			}
		}
		if (f.delete())
			return;
	}
}
