import info.talacha.filmweb.api.FilmwebApi;
import info.talacha.filmweb.models.Film;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
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

import org.xml.sax.SAXException;

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

		result = result.replace(":)", "");
		result = result.replace(";)", "");
		result = result.replace("r5.line", "");
		result = result.replace("dd5.1", "");
		result = result.replace("audioksiazki.org", "");
		result = result.replace(".", " ");
		result = result.replace("_", " ");
		result = result.replaceAll("\\[.*\\]", "");
		result = result.replaceAll("\\{.*\\}.*", "");
		result = result.replaceAll("\\(.*\\).*", "");
		result = result.replaceAll("(19|20)[0-9]{2}", "");
		result = result.replaceAll("[0-9]+-[0-9]+", "");
		result = result.replaceAll(" pl( |$)", "");
		result = result.replace(" 3d", "");
		result = result.replace(" hd", "");
		result = result.replace(" !", "");
		result = result.replace(" cz ", "");
		result = result.replace(" tv", "");
		result = result.replace(" scr", "");
		result = result.replace("480p", "");
		result = result.replace("720p", "");
		result = result.replace("1080p", "");
		result = result.replace("1080 p", "");
		result = result.replace("ac3", "");
		result = result.replace("avi", "");
		result = result.replace("bbc", "");
		result = result.replace("bbs", "");
		result = result.replace("bdrip", "");
		result = result.replace("blu-rip", "");
		result = result.replaceAll("^c[ ]+", "");
		result = result.replace("bigfags", "");
		result = result.replace("bluray", "");
		result = result.replace("blu ray", "");
		result = result.replace("brrip", "");
		result = result.replace("director cut", "");
		result = result.replace("divx", "");
		result = result.replace("dokument", "");
		result = result.replace("domino", "");
		result = result.replace("drp", "");
		result = result.replace("dub", "");
		result = result.replace("dvbrip", "");
		result = result.replace("dvdscr", "");
		result = result.replace("dvdrip", "");
		result = result.replace("dvd9-br", "");
		result = result.replace("dvd", "");
		result = result.replace("dv d", "");
		result = result.replace("eng", "");
		result = result.replace("film polski", "");
		result = result.replace("film", "");
		result = result.replace("french", "");
		result = result.replace("hdtv", "");
		result = result.replace("horror", "");
		result = result.replace("komedia rom", "");
		result = result.replace("koniec", "");
		result = result.replace("korean", "");
		result = result.replace("lektorpl", "");
		result = result.replace("lektor", "");
		result = result.replace("li mited", "");
		result = result.replace("limited", "");
		result = result.replace("mp4", "");
		result = result.replace("nappl", "");
		result = result.replace("wtopione napisy", "");
		result = result.replace("napisy wklejone", "");
		result = result.replace("napisy", "");
		result = result.replace("p24", "");
		result = result.replace("pal", "");
		result = result.replace("po polsku", "");
		result = result.replace("psig-", "");
		result = result.replace("proper", "");
		result = result.replace("super custon", "");
		result = result.replace("r5", "");
		result = result.replace("readnfo", "");
		result = result.replace("rip", "");
		result = result.replace("rmvbhunters", "");
		result = result.replace("rmv busters", "");
		result = result.replace("rmvb", "");
		result = result.replace("skończony", "");
		result = result.replace("soulcreeper", "");
		result = result.replace("subbed", "");
		result = result.replace("subs", "");
		result = result.replace("thiller", "");
		result = result.replace("untouched", "");
		result = result.replace("unrated", "");
		result = result.replaceAll("x264-?.*", "");
		result = result.replaceAll("xvid-?.*", "");
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
		result = result.replace("klaxxon", "");
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
				File subdir = new File(path, list[i]);
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

					List<Item> results = new ArrayList<Item>();
					search(subdir, category, results);
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
		// TODO:
		if (results.isEmpty())
			return null;
		Item bestMatch = results.get(0);
		for (Iterator<Item> iterator = results.iterator(); iterator.hasNext();) {
			Item item = iterator.next();
			if (item.title.equalsIgnoreCase(dir)) { // perfect match!
				return item;
			}
			if (removePolishCharacters(item.title).equalsIgnoreCase(dir)) {
				return item;
			}
			// Polish / English
			String[] split = item.title.split("/");
			for (int i = 0; i < split.length; i++) {
				if (split[i].trim().equalsIgnoreCase(dir)) { // perfect match!
					return item;
				}
				if (removePolishCharacters(split[i].trim()).equalsIgnoreCase(dir)) {
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

	private static String removePolishCharacters(String polish) {
		String result = polish;
		result = result.replace("ą", "a");
		result = result.replace("ć", "c");
		result = result.replace("ę", "e");
		result = result.replace("ł", "l");
		result = result.replace("ń", "n");
		result = result.replace("ó", "o");
		result = result.replace("ś", "s");
		result = result.replace("ż", "z");
		result = result.replace("ź", "z");
		return result;
	}
	
	public static void search(String dir, String category, List<Item> results) {
		FilmwebApi fa = new FilmwebApi();
		ArrayList<Film> filmList = fa.getFilmList(dir);
		for (Film film : filmList) {
			String title = film.getTitle();
			String polishTitle = film.getPolishTitle();
			String itemTitle = null;
			if (title == null) {
				itemTitle = polishTitle;
			} else if (polishTitle == null) {
				itemTitle = title;
			}  else {
				itemTitle = polishTitle + "/" + title;
			}
			
			DecimalFormat df = new DecimalFormat("#.##");
			if (film.getRate()!=null) { //'5 days of war'
			String rounded = df.format(film.getRate().floatValue());
			float rating = Float.parseFloat(rounded);
			results.add(new Item(dir, itemTitle, rating, category));
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
		for (Iterator<Item> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Item> entry = (Map.Entry)it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	public static void writeSortedItemsToFile(Map<String, Item> sortedItems, File file) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
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
		System.out.println("Found cache file: " + filePath);
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
			writeSortedItemsToFile(items_sorted, new File(args[0], sdf.format(c1.getTime()) + ".txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done.");
	}
}
