package dummygenerator;

import java.util.Iterator;
import java.util.Vector;

public class LocationDummySet {
    int num_dummy;
    double time;
    int id_location_real;
    int index_location_real;
    public Vector<Location> location_dummy_set = new Vector<Location>();

    public LocationDummySet(int num_dummy, double time, Location location_real, int k, double s) {
        this.num_dummy = num_dummy;
        this.time = time;
        if(!this.CreateLocationDummy(location_real, k, s)) {
            System.out.println("LocationDummySet: create location dummy failed");
        }
    }

    private boolean CreateLocationDummy(Location location_real, int k, double s) {
        int c = (int) Math.ceil(Math.sqrt(k));
        double g = Math.sqrt(s) / (c - 1) * 0.01 / 1000;
        int idx;
        int idy;
        while(true){
            idx = (int) (Math.random() * c);
            idy = (int) (Math.random() * c);
            if(idx * c + idy <= k - 1) {
                break;
            }
        }
        this.id_location_real = idx * c + idy;
        this.index_location_real = idx * c + idy;
        for (int i = 0; i <= c - 1; i++) {
            for (int j = 0; j <= c - 1; j++) {
                Location location = new Location(i * c + j, (i - idx) * g + location_real.longitude,
                        (j - idy) * g + location_real.latitude);
                this.location_dummy_set.add(location);
                if (location.id >= k - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean UpdateIndexLocationReal() {
        for(int i = 0; i < this.location_dummy_set.size(); i++) {
            if(this.location_dummy_set.get(i).id == this.id_location_real) {
                this.index_location_real = i;
                return true;
            }
        }
        System.out.println( "LocationDummySet: update index of real location failed, not exist real location");
        return false;
    }

    public Location GetLocationById(int id) {
        Location loc;
        for(Iterator<Location> iter = this.location_dummy_set.iterator(); iter.hasNext();){
            loc = iter.next();
            if (loc.id == id) {
                return loc;
            }
        }
        System.out.println("LocationDummySet: get location by id failed, there is not exist a location id is " + id);
        return null;
    }

    public boolean DeleteLocationById(int id_set, int id_edge) {
        Location loc;
        for(Iterator<Location> iter = this.location_dummy_set.iterator(); iter.hasNext();){
            loc = iter.next();
            if (loc.id == id_set) {
                if (!loc.DeleteEdgeById(id_edge)) {
                    System.out.println("LocationDummySet: delete location by id failed");
                    return false;
                }
                return true;
            }
        }
        System.out.println("LocationDummySet: delete location by id failed, not exist location id is " + id_set);
        return false;
    }
}
