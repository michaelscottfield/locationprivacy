package dummies;

import java.util.Iterator;
import java.util.Vector;

public class Location {
	int id;
	double longitude;
	double latitude;
	Vector<Integer> edges_id;
	public Location(int id, double longitude, double latitude) {
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Location() {
		// TODO Auto-generated constructor stub
	}

	boolean DeleteEdgeById(int id) {
		for(Iterator<Integer> iter = this.edges_id.iterator(); iter.hasNext();) {
			if (iter.next() == id) {
				this.edges_id.remove(id);
				return true;
			}
		}
		System.out.println("location: delete edge by id failed, there is not exist an edge id is " + id);
		return false;
	}
}
