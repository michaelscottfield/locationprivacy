package dummies;

import java.io.IOException;

public class Locationprivacyprotection {
	public static void main(String[] args) throws IOException {
		while (true) {
			Location_dummy_sets location_dummy_sets = new Location_dummy_sets();
			location_dummy_sets.CreateLocationDummySets();
			if (location_dummy_sets.DummiesFilter()) {
				break;
			}
		}
		return;
	}
}
