package dummies;

import java.util.Iterator;
import java.util.Vector;

public class Location_dummy_set {
	int num_dummy;
	double time;
	int id_location_real;
	int index_location_real;
	Vector<Location> location_dummy_set;

	boolean CreateLocationDummySet(int num_dummy, double time, Location location_real, int k, double s) {
		this.num_dummy = num_dummy;
		this.time = time;
		this.CreateLocationDummy(location_real, k, s);
		return true;
	}

	boolean CreateLocationDummy(Location location_real, int k, double s) {
		int c = (int) Math.ceil(Math.sqrt(k));
		double g = Math.sqrt(s) / (c - 1) * 0.01 / 1000;
		int idx = (int) (Math.random() * c);
		int idy = (int) (Math.random() * c);
		this.id_location_real = idx * c + idy;
		this.index_location_real = idx * c + idy;
		for (int i = 0; i <= c - 1; i++) {
			for (int j = 0; j <= c - 1; j++) {
				/*Location location = null;
				location.id = i * c + j;
				location.longitude = (i - idx) * g + location_real.longitude;
				location.latitude = (j - idy) * g + location_real.latitude;*/
				Location location = new Location(i * c + j, (i - idx) * g + location_real.longitude,(j - idy) * g + location_real.latitude);
				this.location_dummy_set.add(location);
				if (location.id >= k - 1) {
					return true;
				}
			}
		}
		System.out.println("location_dummy_set: create location dummy failed");
		//cout << "location_dummy_set: create location dummy failed" << endl;
		return false;
	}

	boolean UpdateIndexLocationReal() {
		int i = 0;
		for(Iterator<Location> iter = this.location_dummy_set.iterator(); iter.hasNext();) {
			if (iter.next().id == this.id_location_real) {
				this.index_location_real = i;
				return true;
			}
			i ++;
		}
		/*for (int i = 0; i < this.location_dummy_set.size(); i++) {
			
		}*/
		System.out.println( "location_dummy_set: update index of real location failed, not exist real location");
		//cout << "location_dummy_set: update index of real location failed, not exist real location" << endl;
		return false;
	}

	Location GetLocationById(int id) {
		Location loc;
		for(Iterator<Location> iter = this.location_dummy_set.iterator(); iter.hasNext();){
			loc = iter.next();
			if (loc.id == id) {
				return loc;
			}
		}
		System.out.println("location_dummy_set: get location by id failed, there is not exist a location id is " +id);
		//cout << "location_dummy_set: get location by id failed, there is not exist a location id is " << id << endl;
		return null;
	}

	boolean DeleteLocationById(int id_set, int id_edge) {
		Location loc;
		//for (vector<Location>::iterator iter = this->location_dummy_set.begin(); iter != this->location_dummy_set.end(); iter++) {
		for(Iterator<Location> iter = this.location_dummy_set.iterator(); iter.hasNext();){	
			loc = iter.next();
			if (loc.id == id_set) {
				if (!loc.DeleteEdgeById(id_edge)) {
					System.out.println("cout << \"location_dummy_set: delete location by id failed");
					//cout << "location_dummy_set: delete location by id failed" << endl;
					return false;
				}
				return true;
			}
		}
		System.out.println("location_dummy_set: delete location by id failed, not exist location id is " + id_set);
		//cout << "location_dummy_set: delete location by id failed, not exist location id is " << id_set << endl;
		return false;
	}
}
