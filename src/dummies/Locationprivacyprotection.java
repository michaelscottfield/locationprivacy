package dummies;

import java.io.IOException;

public class Locationprivacyprotection {
	public static void main(String[] args) throws IOException {
		while (true) {
			System.out.println("another round");
			Location_dummy_sets location_dummy_sets = new Location_dummy_sets();
			location_dummy_sets.CreateLocationDummySets();
			System.out.println("out of con");
			if (location_dummy_sets.DummiesFilter()) {
				//System.out.println("running");
				break;
			}
		}
		return;
	}
}
