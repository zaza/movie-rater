import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDivElement;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class MovieRater {

	public static String extractMovieName(String subdir) {
		String result = subdir;
		// /([A-Z][a-z]{2,})([A-Z][a-z]+)/
		Pattern p = Pattern.compile("([A-Z][a-z]+)([A-Z][a-z]+)");
		Matcher m =p.matcher(result);
		while( m.find()) {
			result = result.replace(m.group(1) + "" + m.group(2), m.group(1)
					+ " " + m.group(2));
			m = p.matcher(result);
		}
		
		result = result.toLowerCase();

		result = result.replace("r5.line", "");
		result = result.replace("dd5.1", "");
		result = result.replace("audioksiazki.org", "");
		result = result.replace(".", " ");
		result = result.replace("_", " ");
		result = result.replaceAll("\\[.*\\].*", "");
		result = result.replaceAll("\\{.*\\}.*", "");
		result = result.replaceAll("\\(.*\\).*", "");
		result = result.replaceAll("(19|20)[0-9]{2}", "");
		result = result.replaceAll("[0-9]+-[0-9]+", "");
		result = result.replaceAll(" pl( |$)", "");
		result = result.replace(" 3d", "");
		result = result.replace(" hd", "");
		result = result.replace(" !", "");
		result = result.replace(" cz ", "");
		result = result.replace(" scr", "");
		result = result.replace("480p", "");
		result = result.replace("720p", "");
		result = result.replace("ac3", "");
		result = result.replace("avi", "");
		result = result.replace("bbc", "");
		result = result.replace("bbs", "");
		result = result.replace("bigfags", "");
		result = result.replace("bluray", "");
		result = result.replace("blu ray", "");
		result = result.replace("brrip", "");
		result = result.replace("director cut", "");
		result = result.replace("divx", "");
		result = result.replace("domino", "");
		result = result.replace("drp", "");
		result = result.replace("dub", "");
		result = result.replace("dvbrip", "");
		result = result.replace("dvdscr", "");
		result = result.replace("dvdrip", "");
		result = result.replace("dvd", "");
		result = result.replace("french", "");
		result = result.replace("horror", "");
		result = result.replace("komedia rom", "");
		result = result.replace("koniec", "");
		result = result.replace("lektorpl", "");
		result = result.replace("lektor", "");
		result = result.replace("limited", "");
		result = result.replace("nappl", "");
		result = result.replace("napisy", "");
		result = result.replace("p24", "");
		result = result.replace("po polsku", "");
		result = result.replace("proper", "");
		result = result.replace("super custon", "");
		result = result.replace("r5", "");
		result = result.replace("readnfo", "");
		result = result.replace("rip", "");
		result = result.replace("rmvbhunters", "");
		result = result.replace("rmvb", "");
		result = result.replace("subbed", "");
		result = result.replace("thiller", "");
		result = result.replace("untouched", "");
		result = result.replace("unrated", "");
		result = result.replace("x264", "");
		result = result.replaceAll("xvid-?.*","");
		result = result.replace("-80m", "");
		result = result.replace("-alliance", "");
		result = result.replace("-axxo", "");
		result = result.replace("-bestdivx", "");
		result = result.replace("-cinefile", "");
		result = result.replace("-diamond", "");
		result = result.replace("-done", "");
		result = result.replace("-er", "");
		result = result.replace("-firma", "");
		result = result.replace("-fxg", "");
		result = result.replace("-fxm", "");
		result = result.replace("-hefty", "");
		result = result.replace("-kjs", "");
		result = result.replace("-konik", "");
		result = result.replace("-ktks", "");
		result = result.replace("-m00dy", "");
		result = result.replace("-mdx", "");
		result = result.replace("-psig", "");
		result = result.replace("-ruby", "");
		result = result.replace("-vision", "");
		result = result.replace("-xan-0", "");
		result = result.replace("-", " ");
		result = result.replace("the ", " ");
		result = result.replaceAll("\\s{2,}", " ");
		return result.trim();
	}
	
	public static Map<String, String[]> scanDirs(String rootDir, String subDirsToExclude) {
		Map<String, String[]> result = new HashMap<String, String[]>();
		
		File path = new File(rootDir);
		List<String> excludes = Arrays.asList(subDirsToExclude.split(","));
		if (path.isDirectory()) {
			String[] list = path.list();
			for (int i = 0; i < list.length; i++) {
				File subdir = new File(path.getAbsolutePath() + "\\" + list[i]);
				if (subdir.isDirectory() && !excludes.contains(list[i])) {
					result.put(list[i], subdir.list());
				}
			}
		}
		return result;
	}
	
	public static Map<String, Item> getMovieRatings(Map<String, String[]> dirs_hash, Map<String, Item> movies_cache) throws IOException, SAXException {
		
		Map<String, Item> movies_hash = new HashMap<String, Item>();
		
		for (Iterator<String> iterator = dirs_hash.keySet().iterator(); iterator.hasNext();) {
			String category = iterator.next();
			System.out.println("======================================> Processing category: "+category);
			
			String[] subdirs = dirs_hash.get(category);
			for (int i = 0; i < subdirs.length; i++) {
				//  default
				Item item = new Item(subdirs[i], "", 0.0f, category);
				
				String subdir = extractMovieName(subdirs[i]);
				System.out.print("Looking for '" + subdir + "'... ");
				
				if (movies_cache.containsKey(subdirs[i])) {
					item = movies_cache.get(subdirs[i]);
					System.out.print("[cache] ");
				} else {
					String encoded = URLEncoder.encode(subdir, "UTF-8");
					
//					HttpUnitOptions.setScriptingEnabled(false);
//					HttpUnitOptions.setExceptionsThrownOnScriptError(false);

//					WebConversation wc = new WebConversation();
//					WebResponse   resp = wc.getResponse( "http://www.filmweb.pl/search/film?q=" + encoded);
//					System.out.println(resp.getText());
//					WebLink link = resp.getLinkWith("Przejdź do Filmwebu");
//					if (link != null) {
////						link.click();
//					System.out.println("================ wait! ===========");
//					try {
//						Thread.sleep(17*1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//						resp   = wc.getCurrentPage();
//						System.out.println(resp.getText());
//					}
//					Document document = resp.getDOM();
					
					
					   final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3);
					   //.setsetCssErrorHandler
					   webClient.setThrowExceptionOnScriptError(false);
					   webClient.setJavaScriptEnabled(false);

//					    HtmlPage page = webClient.getPage("http://www.filmweb.pl/search/film?q=" + encoded);
					   HtmlPage page = webClient.getPage("http://www.filmweb.pl");
					   
//					    System.out.println( page.asXml());
					    HtmlAnchor link = null;
					    try {
//					     link = page.getAnchorByHref("http://www.filmweb.pl/search/film?q=" + encoded);
					    	link = page.getAnchorByHref("http://www.filmweb.pl");
					    } catch (ElementNotFoundException e) {
					    	
					    }
					    if (link != null) {
					    	System.err.println("================need to click");
					    	page = link.click();
					    	System.out.println("================ clcik! ===========");
					    }
//					    System.out.println( page.asText());
//
//					  final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_3);
//					  HtmlPage page = webClient.getPage("http://www.filmweb.pl/");
					  HtmlForm searchForm = page.getForms().get(0);
//					  searchForm.setTextContent(subdir);
					  searchForm.getInputByName("q").setValueAttribute(subdir);
//					  page = (HtmlPage) searchForm.getInputByValue("Sign In").click();

					  HtmlSubmitInput submit = (HtmlSubmitInput) searchForm.getByXPath("//input").get(1);
					  page = submit.click();
//					  System.out.println("search="+ page.asText());
					
//					DOMParser parser = new DOMParser();
//					parser.parse("http://www.filmweb.pl/search/film?q=" + encoded);
					List<Item> results = new ArrayList<Item>();
//					search(parser.getDocument(), subdirs[i], category, results);
//					search(document, subdirs[i], category, results);
					search(page, subdirs[i], category, results);
					Item it = findBestMatch(subdirs[i], results);
					if (it != null)
						item = it; // found!
				}
				System.out.println(item.toString());
				movies_hash.put(subdirs[i], item);
			}
		}
		
		return movies_hash;
	}
	
	public static Item findBestMatch(String dir, List<Item> results) {
		Item bestMatch = results.get(0);
		for (Iterator<Item> iterator = results.iterator(); iterator.hasNext();) {
			Item item = iterator.next();
			if (item.title.equalsIgnoreCase(dir)) { // perfect match!
				return item;
			}
			// Polish / English
			String[] split = item.title.split("/");
			for (int i = 0; i < split.length; i++) {
				if (split[i].trim().equalsIgnoreCase(dir)) { // perfect match!
					return item;
				}
				// title : subtitle
				if (split[i].indexOf(":") > 0) {
					if (split[i].substring(0, split[i].indexOf(":"))
							.equalsIgnoreCase(dir))
						return item;
				}
			}
		}
		return bestMatch;
	}
	
	public static void search(HtmlPage page, String dir, String category, List<Item> results) {
		HtmlElement ul = page.getElementById("searchFixCheck");
		Iterable<HtmlElement> lis = ul.getChildElements();
		for (Iterator<HtmlElement> iterator = lis.iterator(); iterator.hasNext();) {
			HtmlElement n = iterator.next();
			
		if (n instanceof HtmlListItem) {
			HtmlListItem listItem = (HtmlListItem) n;
			Object node = listItem.getByXPath("//a[@class='searchResultTitle']").get(0);
		
		if (node instanceof HtmlAnchor) {
			HtmlAnchor anchor = (HtmlAnchor) node;
				Node li = anchor.getParentNode().getParentNode();
				// TODO: convert to java
//				osoba = li.search("span[text()='[osoba]']")[0]
//				if (!osoba.nil?) # skip [osoba]
//				   next
//				end
				String title = anchor.getTextContent().trim();
				
				List<?> searchResultRatings = listItem.getByXPath("//div[@class='searchResultRating']");
				for (Iterator iterator2 = searchResultRatings.iterator(); iterator2
						.hasNext();) {
					Object object = (Object) iterator2.next();
					
//					Node liChild = liChildNodes.item(i);
					if (object instanceof HtmlDivision ) {
						HtmlDivision  div = (HtmlDivision) object;
//						if (div.getClassName().equals("searchResultRating")) {
							Node span = div.getChildNodes().item(1);
							String text = span.getTextContent();
							Pattern p = Pattern.compile("(ocena: )([0-9]{1}\\.[0-9]{1,2})");
							Matcher matcher = p.matcher(text);
							if (matcher.find())
								results.add(new Item(dir, title, Float.parseFloat(matcher.group(2)), category));
//						}
					}
			}
		}
		}
		}
    }

	private static Map<String, Item> sortByValue(Map<String, Item> map) {
		List<Item> list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});
		Map<String, Item> result = new LinkedHashMap<String, Item>();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Item> entry = (Map.Entry)it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	public static void writeSortedItemsToFile(Map<String, Item> sortedItems, String filePath) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
		for (Iterator<Item> it = sortedItems.values().iterator(); it.hasNext();) {
			Item item = it.next();
			out.write(item.toString());
			out.newLine();
		}
		out.close();
	}
	
	public static Map<String, Item> readCache(String root) throws IOException {
		String filePath = findLatestCacheFile(root);
		Map<String, Item> result = new HashMap<String, Item>();
		File file = new File(filePath);
		if (!file.exists() || file.isDirectory())
			return result;
		System.out.println("Found cache file:" + filePath);
		FileReader fr = new FileReader(filePath);
	    BufferedReader br = new BufferedReader(fr);
	    String s;
	    while ((s = br.readLine()) != null) {
	    	Item i = Item.toItem(s);
	    	if (i.rating > 0)
	    		result.put(i.dir, i);
	    }
	    fr.close();
	    return result;
	}
	
	public static String findLatestCacheFile(String root) {
		File rootFile = new File(root);
		if (rootFile.isDirectory()) {
			String[] list = rootFile.list();
			if (list.length > 0) {
				String latest = "";
				for (int i = 0; i < list.length; i++) {
					if (list[i].endsWith(".txt") && list[i].compareTo(latest) > 0)
						latest = list[i];
				}
				return root + "/" + latest;
			}
		}
		return root + "//" + "test.txt";
	}
	
	public static void main(String[] args) throws IOException, SAXException {
		Map<String, String[]> dirs_hash = scanDirs(args[0], args[1]);
		System.out.println("categories=" + dirs_hash.size());
		
		Map<String, Item> movies_cache = readCache(args[0]);
		Map<String, Item> movies_hash = getMovieRatings(dirs_hash, movies_cache);
		System.out.println("======================================> Sorting");
		Map<String, Item> items_sorted = sortByValue(movies_hash);

		for (Iterator<Item> it = items_sorted.values().iterator(); it.hasNext();) {
			Item item = it.next();
			System.out.println(item.toString());
		}

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar c1 = Calendar.getInstance(); // today
			writeSortedItemsToFile(items_sorted, args[0] + "\\"	+ sdf.format(c1.getTime()) + ".txt");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done.");
	}
}
