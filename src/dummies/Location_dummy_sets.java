package dummies;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class Location_dummy_sets {
	Location_dummy_set first;
	Location_dummy_set second;
	Points_of_interest points_of_interest = new Points_of_interest();
	Utils utils = new Utils();
	boolean CreateLocationDummySets() throws IOException {
		this.points_of_interest.CreatePoi();

		Location location_real_first = new Location();
		location_real_first.longitude = -118.23556;
		location_real_first.latitude = 34.10444;
		first.CreateLocationDummySet(Stdafx.NUM_DUMMY, 0.0, location_real_first, Stdafx.NUM_DUMMY, 10000);
		for(Iterator<Location> iter = this.points_of_interest.poi.iterator(); iter.hasNext();) {
		//for (vector<Location>::iterator iter = this->points_of_interest.poi.begin(); iter != this->points_of_interest.poi.end(); iter++) {
			Location loc;
			loc = iter.next();
			double dst = utils.GetDistance(loc.longitude, loc.latitude, location_real_first.longitude, location_real_first.latitude);
			if (Stdafx.DISTANCE_MIN < dst && dst < Stdafx.DISTANCE_MAX) {
				Location location_real_second = new Location();
				location_real_second.longitude = loc.longitude;
				location_real_second.latitude = loc.latitude;
				if (!second.CreateLocationDummySet(Stdafx.NUM_DUMMY, dst / Stdafx.SPEED, location_real_second, Stdafx.NUM_DUMMY_INITIAL, 1000000)) {
					System.out.println("location_dummy_sets: create location dummy sets failed, create location dummy set failed");
					//cout << "location_dummy_sets: create location dummy sets failed, create location dummy set failed" << endl;
					return false;
				}
				return true;
			}
		}
		System.out.println("location_dummy_sets: create location dummy sets failed, not exist a proper next location");
		//cout << "location_dummy_sets: create location dummy sets failed, not exist a proper next location" << endl;
		return false;
	}

	double AngleVectors(Location l1, Location l2, Location l3, Location l4) {
		double vector1_y = l2.latitude - l1.latitude;
		double vector1_x = l2.longitude - l1.longitude;
		double vector2_y = l4.latitude - l3.latitude;
		double vector2_x = l4.longitude - l3.longitude;

		double a = vector1_x * vector2_x + vector1_y * vector2_y;
		double b = Math.sqrt(Math.pow(vector1_x, 2) + Math.pow(vector1_y, 2)) * Math.sqrt(Math.pow(vector2_x, 2) + Math.pow(vector2_y, 2));
		if (b == 0) {
			return 2 * Stdafx.PI;
		}
		return Math.acos(a / b);
	}

	double GetTime(Location l1, Location l2) {
		return utils.GetDistance(l1.longitude, l1.latitude, l2.longitude, l2.latitude) / Stdafx.SPEED;
	}

	boolean DirectionFilter() {
		Vector<Location> location_dummy_set_first = this.first.location_dummy_set;
		Vector<Location> location_dummy_set_second = this.second.location_dummy_set;
		Location location_real_first = this.first.location_dummy_set.get(this.first.index_location_real);
		Location location_real_second = this.second.location_dummy_set.get(this.second.index_location_real);
		int k = this.second.num_dummy;
		Location loc_first, loc_second, loc;
		for(Iterator<Location> iter_second = location_dummy_set_second.iterator(); iter_second.hasNext();) {
		//for (vector<Location>::iterator iter_second = location_dummy_set_second->begin(); iter_second != location_dummy_set_second->end(); iter_second++) {
			loc_second = iter_second.next();
			for(Iterator<Location> iter_first = location_dummy_set_first.iterator(); iter_first.hasNext();) {
			//for (vector<Location>::iterator iter_first = location_dummy_set_first->begin(); iter_first != location_dummy_set_first->end(); iter_first++) {
				loc_first = iter_first.next();
				double angle = this.AngleVectors(loc_first, loc_second, location_real_first, location_real_second);
				if (angle < Stdafx.THETA_A) {
					loc_first.edges_id.add(loc_second.id);
					loc_second.edges_id.add(loc_first.id);
				}
			}
		}
		for(Iterator<Location> iter = location_dummy_set_second.iterator(); iter.hasNext();) {
		//for (vector<Location>::iterator iter = location_dummy_set_second->begin(); iter != location_dummy_set_second->end(); ) {
			loc = iter.next();
			if (loc.edges_id.size() == 0) {
				location_dummy_set_second.remove(loc);
			}
			/*else {
				
			}*/
		}
		this.second.UpdateIndexLocationReal();

		if (location_dummy_set_second.size() < k) {
			System.out.println("location_dummy_sets: direction filter failed, second set num less than "+k);
			//cout << "location_dummy_sets: direction filter failed, second set num less than " << k << endl;
			return false;
		}
		for(Iterator<Location> iter = location_dummy_set_first.iterator(); iter.hasNext();) {
		//for (vector<Location>::iterator iter = location_dummy_set_first->begin(); iter != location_dummy_set_first->end(); iter++) {
			loc_first = iter.next();
			if (loc_first.edges_id.size() == 0) {
				System.out.println("location_dummy_sets: direction filter failed, first set has location not connect to second");
				//cout << "location_dummy_sets: direction filter failed, first set has location not connect to second" << endl;
				return false;
			}
		}

		return true;
	}

	boolean TimeReasonabilityFilter() {
		Vector<Location> location_dummy_set_first = this.first.location_dummy_set;
		Vector<Location> location_dummy_set_second = this.second.location_dummy_set;
		//Location location_real_first = this.first.location_dummy_set.get(this.first.index_location_real);
		//Location location_real_second = this.second.location_dummy_set.get(this.second.index_location_real);
		//vector<Location> *location_dummy_set_first = &this->first.location_dummy_set;
		//vector<Location> *location_dummy_set_second = &this->second.location_dummy_set;
		//Location *location_real_first = &(this->first.location_dummy_set[this->first.index_location_real]);
		//Location *location_real_second = &(this->second.location_dummy_set[this->second.index_location_real]);
		int k = this.second.num_dummy;
		Location loc_first, loc_second;
		for(Iterator<Location> iter_second = location_dummy_set_second.iterator(); iter_second.hasNext();) {
		//for (vector<Location>::iterator iter_second = location_dummy_set_second->begin(); iter_second != location_dummy_set_second->end(); iter_second++) {
			loc_second = iter_second.next();
			for(Iterator<Location> iter_first = location_dummy_set_first.iterator(); iter_first.hasNext();) {
			//for (vector<Location>::iterator iter_first = location_dummy_set_first->begin(); iter_first != location_dummy_set_first->end(); iter_first++) {
				loc_first = iter_first.next();
		//for (vector<Location>::iterator iter_first = location_dummy_set_first->begin(); iter_first != location_dummy_set_first->end(); iter_first++) {
			//for (vector<int>::iterator iter = iter_first->edges_id.begin(); iter != iter_first->edges_id.end(); ) {
				Location location_second = this.second.GetLocationById(loc_second.id);
				double time = this.GetTime(loc_first, location_second);
				double time_difference = this.second.time - this.first.time;
				if (Math.abs(time - time_difference) > Stdafx.THETA_T * time_difference) {
					this.second.DeleteLocationById(loc_second.id, loc_first.id);
					loc_second.edges_id.remove(loc_second.id);
					//iter = iter_first->edges_id.erase(iter);
				}
				else {
					
				}
			}
		}
		for(Iterator<Location> iter_second = location_dummy_set_second.iterator(); iter_second.hasNext();) {
		//for (vector<Location>::iterator iter = location_dummy_set_second->begin(); iter != location_dummy_set_second->end(); ) {
			loc_second = iter_second.next();
			if (loc_second.edges_id.size() == 0) {
				location_dummy_set_second.remove(loc_second);
			}
			
		}
		this.second.UpdateIndexLocationReal();

		if (location_dummy_set_second.size() < k) {
			System.out.println("location_dummy_sets: time reasonability filter failed, second set num less than " + k);
			//cout << "location_dummy_sets: time reasonability filter failed, second set num less than " << k << endl;
			return false;
		}

		for(Iterator<Location> iter_first = location_dummy_set_first.iterator(); iter_first.hasNext();) {
		//for (vector<Location>::iterator iter = location_dummy_set_first->begin(); iter != location_dummy_set_first->end(); iter++) {
			loc_first = iter_first.next();
			if (loc_first.edges_id.size() == 0) {
				System.out.println("location_dummy_sets: time reasonability filter failed, first set has location not connect to second");
				//cout << "location_dummy_sets: time reasonability filter failed, first set has location not connect to second" << endl;
				return false;
			}
		}

		return true;
	}

	boolean DegreeFilter() {
		Vector<Location> location_dummy_set_first = this.first.location_dummy_set;
		Vector<Location> location_dummy_set_second = this.second.location_dummy_set;
		Location location_real_first = this.first.location_dummy_set.get(this.first.index_location_real);
		Location location_real_second = this.second.location_dummy_set.get(this.second.index_location_real);
		int k = this.second.num_dummy;
		/*vector<Location> *location_dummy_set_first = &this->first.location_dummy_set;
		vector<Location> *location_dummy_set_second = &this->second.location_dummy_set;
		Location *location_real_first = &(this->first.location_dummy_set[this->first.index_location_real]);
		Location *location_real_second = &(this->second.location_dummy_set[this->second.index_location_real]);
		int k = this->second.num_dummy;*/
		Location loc_second, loc_first;
		int second_max;
		int tag = 1;
		second_max = location_real_second.edges_id.size();
		Vector<Location> second_grater = new Vector<Location>();
		Vector<Location> second_less = new Vector<Location>();
		for(Iterator<Location> iter_second = location_dummy_set_second.iterator(); iter_second.hasNext();) {
		//for (vector<Location>::iterator iter = location_dummy_set_second->begin(); iter != location_dummy_set_second->end(); iter++) {
			loc_second = iter_second.next();
			if (loc_second.id != this.second.id_location_real) {
				if (loc_second.edges_id.size() > second_max) {
					tag = 0;
					second_grater.add(loc_second);
				}
				else {
					second_less.add(loc_second);
				}
			}
		}

		if (tag == 1) {
			System.out.println("location_dummy_sets: degree reasonability filter failed, second real location degree is gratest");
			//cout << "location_dummy_sets: degree reasonability filter failed, second real location degree is gratest" << endl;
			return false;
		}

		while (true) {
			int dummy_k = k - 1;
			int second_grater_size = second_grater.size();
			int second_less_size = second_less.size();
			int m = (int) (second_grater_size < Math.ceil(dummy_k / 2) ? second_grater_size : Math.ceil(dummy_k / 2));
			for (int i = 1; i <= m; i++) {
				Vector<Location> candidate = new Vector<Location>();
				candidate.add(this.second.location_dummy_set.get(this.second.index_location_real));
				Set<Integer> second_grater_rand = utils.GetRand(i, second_grater_size);
				Iterator<Integer> iter1 = second_grater_rand.iterator();
				while(iter1.hasNext()) {
					candidate.add(second_grater.get((int) iter1.next()));
				}
				//for (set<Integer>::iterator iter = second_grater_rand.begin(); iter != second_grater_rand.end(); iter++) {
					//candidate.push_back(second_grater[*iter]);
				//}
				Set<Integer> second_less_rand = utils.GetRand(dummy_k - i, second_less_size);
				Iterator<Integer> iter2 = second_less_rand.iterator();
				while(iter1.hasNext()) {
					candidate.add(second_less.get((int) iter2.next()));
				}
				//for (set<int>::iterator iter = second_less_rand.begin(); iter != second_less_rand.end(); iter++) {
					//candidate.push_back(second_less[*iter]);
				//}
				//int first_num[] = new int[k];
				Vector<Integer> first_num = new Vector<Integer>();
				for(i = 0; i < k; i++) {
					first_num.add(0);
				}
				//Vector<Integer> first_num = new Vector<Integer>[k];
				for (i = 0; i < candidate.size(); i++) {
					for (int j = 0; j < candidate.get(i).edges_id.size(); j++) {
						//first_num.get(candidate.get(i).edges_id.get(j))++;
					}
				}
				int first_max = first_num.get(this.first.index_location_real);
				for (i = 1; i < first_num.size(); i++) {
					if (first_num.get(i) > first_max) {
						System.out.println("success");
						//cout << "success" << endl;
						System.out.println("First location dummy set:");
						//cout << "First location dummy set:" << endl;
						System.out.println("real location: (" + location_real_first.longitude + ", " + location_real_first.latitude + ")");
						//cout << "real location: (" << location_real_first->longitude << ", " << location_real_first->latitude << ")" << endl;
						System.out.println("dummy set: ");
						//cout << "dummy set: " << endl;
						for(Iterator<Location> iter_first = location_dummy_set_first.iterator(); iter_first.hasNext();) {
						//for (vector<Location>::iterator iter = location_dummy_set_first->begin(); iter != location_dummy_set_first->end(); iter++) {
							//cout << "(" << iter->longitude << ", " << iter->latitude << ")" << endl;
							loc_first = iter_first.next();
							System.out.println("(" +loc_first.longitude + ", " +loc_first.latitude + ")");
						}
						System.out.println("Second location dummy set:");
						//cout << "Second location dummy set:" << endl;
						System.out.println("real location: (" + location_real_second.longitude + ", " + location_real_second.latitude + ")");
						//cout << "real location: (" << location_real_second->longitude << ", " << location_real_second->latitude << ")" << endl;
						System.out.println("dummy set: ");
						//cout << "dummy set: " << endl;
						for(Iterator<Location> iter = candidate.iterator(); iter.hasNext();) {
						//for (vector<Location>::iterator iter = candidate.begin(); iter != candidate.end(); iter++) {
							loc_first = iter.next();
							System.out.println("(" + loc_first.longitude + ", " + loc_first.latitude + ")");
							//cout << "(" << iter->longitude << ", " << iter->latitude << ")" << endl;
						}
						return true;
					}
				}
			}
		}
		//System.out.println("location_dummy_sets: degree reasonability filter failed");
		//cout << "location_dummy_sets: degree reasonability filter failed" << endl;
		//return false;
	}

	boolean DummiesFilter() {
		if (!this.DirectionFilter()) {
			return false;
		}
		if (!this.TimeReasonabilityFilter()) {
			return false;
		}
		if (!this.DegreeFilter()) {
			return false;
		}
		return true;
	}
}
