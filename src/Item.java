public class Item implements Comparable<Item> {
	String dir;
	String title;
	float rating;
	String category;

	public Item(String dir, String title, float rating, String category) {
		this.dir = dir;
		this.title = title;
		this.rating = rating;
		this.category = category;
	}

	@Override
	public String toString() {
		return rating + "\t" + category + "\t" + dir + " -> " + title;
	}

	public int compareTo(Item o) {
		if (rating > o.rating)
			return -1;
		if (rating < o.rating)
			return 1;
		return 0;
	}

	public static Item toItem(String s) {
		String[] split1 = s.split(" -> ");
		String[] split2 = split1[0].split("\t");
		return new Item(split2[2], split1.length == 2 ? split1[1] : "", Float
				.parseFloat(split2[0]), split2[1]);
	}
}
