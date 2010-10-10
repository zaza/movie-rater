import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
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

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLDivElement;
import org.xml.sax.SAXException;


public class MovieRater {

	private static String extractMovieName(String subdir) {
		//	  while dir =~ /([A-Z][a-z]{2,})([A-Z][a-z]+)/
		//	    dir = dir.gsub($1+''+$2, $1+' '+$2)
		//	  end
		String result = subdir.toLowerCase();
		result = result.replace("r5.line", "");
		result = result.replace("dd5.1", "");
		result = result.replace(".", " ");
		result = result.replace("_", " ");
		result = result.replaceAll("\\[.*\\].*", "");
		result = result.replaceAll("\\{.*\\}.*", "");
		result = result.replaceAll("\\(.*\\).*", "");
		result = result.replaceAll("19|20[0-9]{2}", "");
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
	  result = result.replace("lektor", "");
	  result = result.replace("limited", "");
	  result = result.replace("nappl", "");
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
		return result.trim();
	}
	
	private static Map<String, String[]> scanDirs(String args0, String args2) {
		Map<String, String[]> result = new HashMap<String, String[]>();
		
		File path = new File(args0);
		List<String> excludes = Arrays.asList(args2.split(","));
		if (path.isDirectory()) {
			String[] list = path.list();
			for (int i = 0; i < list.length; i++) {
				File subdir = new File(path.getAbsolutePath()+"\\"+list[i]);
				if (subdir.isDirectory() && !excludes.contains(list[i])) {
					result.put(list[i], subdir.list());
				}
			}
		}
		return result;
	}
	
	private static Map<String, Item> getMovieRating(Map<String, String[]> dirs_hash, String creds) throws IOException, SAXException {
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
				String encoded = URLEncoder.encode(subdir, "UTF-8");
				
				
				DOMParser parser = new DOMParser();
				parser.parse("http://www.filmweb.pl/search/film?q=" + encoded);
				Item it = search(parser.getDocument(), subdirs[i], category);
				if (it != null)
					item = it; // found!
				System.out.println(item.toString());
				movies_hash.put(subdirs[i], item);
			}
		}
		
		return movies_hash;
	}
	
	public static Item search(Node node, String dir, String category) {
		if (node instanceof HTMLAnchorElement) {
			HTMLAnchorElement anchor = (HTMLAnchorElement) node;
			if (anchor.getClassName().equals("searchResultTitle")) {
				Node li = node.getParentNode().getParentNode();
				// TODO: convert
//				osoba = li.search("span[text()='[osoba]']")[0]
//				if (!osoba.nil?) # skip [osoba]
//				   next
//				end
				String title = anchor.getTextContent().trim();
				
				NodeList liChildNodes = li.getChildNodes();
				for (int i = 0; i < liChildNodes.getLength(); i++) {
					Node liChild = liChildNodes.item(i);
					if (liChild instanceof HTMLDivElement ) {
						HTMLDivElement  div = (HTMLDivElement) liChild;
						if (div.getClassName().equals("searchResultRating")) {
							Node span = div.getChildNodes().item(1);
							String text = span.getTextContent();
							Pattern p = Pattern.compile("(ocena: )([0-9]{1}\\.[0-9]{1,2})");
							Matcher matcher = p.matcher(text);
							if (matcher.find())
								return new Item(dir, title, Float.parseFloat(matcher.group(2)), category);
						}
					}
				}
			}
		}
		Node child = node.getFirstChild();
		while (child != null) {
			Item item = search(child, dir, category);
			if (item != null)
				return item;
			child = child.getNextSibling();
		}
		return null;
    }

	static Map sortByValue(Map map) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		});
		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry)it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	
	/**
	 * @param args
	 * @throws SAXException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, SAXException {
		Map dirs_hash = scanDirs(args[0], args[1]);
		System.out.println("categories=" + dirs_hash.size());
		
		Map movies_hash = getMovieRating(dirs_hash, args[1]);
		System.out.println("======================================> Sorting");
		Map items_sorted = sortByValue(movies_hash);
		for (Iterator iterator = items_sorted.values().iterator(); iterator.hasNext();) {
			Item item = (Item) iterator.next();
			System.out.println(item.toString());
		}
		System.out.println("Done.");
	}
	
}