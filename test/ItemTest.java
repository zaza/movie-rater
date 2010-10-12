import junit.framework.TestCase;

public class ItemTest extends TestCase {

	public void testToItemOnFound() {
		String s = "8.66	drama	Brothers -> Kompania braci / 	Band of Brothers";
		Item it1 = Item.toItem(s);
		Item it2 = new Item("Brothers", "Kompania braci / 	Band of Brothers",
				8.66f, "drama");
		assertEquals(it2.category, it1.category);
		assertEquals(it2.dir, it1.dir);
		assertEquals(it2.rating, it1.rating);
		assertEquals(it2.title, it1.title);
	}

	public void testToItemOnNotFound() {
		String s = "0.0	drama	The_Notebook_-_Pamietnik_2004_DVDRip_LektorPL -> ";
		Item it1 = Item.toItem(s);
		Item it2 = new Item("The_Notebook_-_Pamietnik_2004_DVDRip_LektorPL",
				"", 0.0f, "drama");
		assertEquals(it2.category, it1.category);
		assertEquals(it2.dir, it1.dir);
		assertEquals(it2.rating, it1.rating);
		assertEquals(it2.title, it1.title);
	}

}
