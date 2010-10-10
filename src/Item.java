public class Item implements Comparable<Item>{
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
		// TODO Auto-generated method stub
		return rating + "\t" + category + "\t" + dir + " -> " + title;
	}

	@Override
	public int compareTo(Item o) {
		if (rating > o.rating) return -1;
		if (rating < o.rating) return 1;
		return 0;
	}

}
