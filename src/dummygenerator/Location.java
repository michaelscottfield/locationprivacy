package dummygenerator;

import java.util.Iterator;
import java.util.Vector;

public class Location {
    public int id;
    public double longitude;
    public double latitude;
    public Vector<Integer> edges_id = new Vector<Integer>();

    public Location(int id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    boolean DeleteEdgeById(int id) {
        int edge_id;
        for(Iterator<Integer> it = this.edges_id.iterator(); it.hasNext(); ) {
            edge_id = it.next();
            if(edge_id == id) {
                it.remove();
                return true;
            }
        }
        System.out.println("location: delete edge by id failed, there is not exist an edge id is " + id);
        return false;
    }
}
