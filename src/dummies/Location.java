package dummies;

import java.util.Iterator;
import java.util.Vector;

public class Location {
	int id;
	double longitude;
	double latitude;
	Vector<Integer> edges_id;
	public Location(int id, double d, double e) {
		this.id = id;
		this.longitude = d;
		this.latitude = e;
	}

	public Location() {
		// TODO Auto-generated constructor stub
		this.edges_id = new Vector<Integer>();
		//System.out.println("how can this be possible");
	}

	boolean DeleteEdgeById(int id) {
		//for (Vector<Integer>::iterator iter = this->edges_id.begin(); iter != this->edges_id.end(); iter++) {
		for(Iterator<Integer> iter = this.edges_id.iterator(); iter.hasNext();) {
			if (iter.next() == id) {
				//this.edges_id.remove(id);
				iter.remove();
				return true;
			}
		}
		System.out.println("location: delete edge by id failed, there is not exist an edge id is " +id);
		//cout << "location: delete edge by id failed, there is not exist an edge id is " << id << endl;
		return false;
	}
}
