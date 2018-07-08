package dummygenerator;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import utils.Util;
import utils.Variable;

import javax.swing.*;

public class LocationDummySets {
    Vector<LocationDummySet> locationDummySets = new Vector<LocationDummySet>();
    Util util = new Util();

    public LocationDummySet GetDummySet(Location location, int k) {
        if(this.locationDummySets.isEmpty()) {
            LocationDummySet locationDummySet = new LocationDummySet(k, 0.0, location, k, 900);
            this.locationDummySets.add(locationDummySet);
            return locationDummySet;
        } else {
            LocationDummySet locationDummySet = this.CreateLocationDummySet(location, k);
            this.locationDummySets.add(locationDummySet);
            return locationDummySet;
        }
    }

    private LocationDummySet CreateLocationDummySet(Location location, int k) {
        LocationDummySet previousDummySet = this.locationDummySets.lastElement();
        Location previousRealLocation = previousDummySet.location_dummy_set.get(previousDummySet.index_location_real);
        double dst = util.GetDistance(location.longitude, location.latitude, previousRealLocation.longitude,
                previousRealLocation.latitude);
        LocationDummySet currentDummySet = new LocationDummySet(k, dst / Variable.SPEED, location,
                Variable.NUM_DUMMY_INITIAL, 193600);
        this.locationDummySets.add(currentDummySet);

        if(!DummyFilter()) {
            System.out.println("LocationDummySets: DummyFilter failed");
            return null;
        }
        return this.locationDummySets.lastElement();
    }

    private boolean DummyFilter() {
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

    private boolean DirectionFilter() {
        int dummySetsSize = this.locationDummySets.size();
        LocationDummySet previous = this.locationDummySets.get(dummySetsSize - 2);
        LocationDummySet current = this.locationDummySets.lastElement();
        Vector<Location> dummySet_previous = previous.location_dummy_set;
        Vector<Location> dummySet_current = current.location_dummy_set;
        Location real_previous = dummySet_previous.get(previous.index_location_real);
        Location real_current = dummySet_current.get(current.index_location_real);
        int k = current.num_dummy;

        for(Location location_current: dummySet_current) {
            for(Location location_previous: dummySet_previous) {
                double angle = util.AngleVectors(location_previous, location_current, real_previous, real_current);
                if(angle < Variable.THETA_A) {
                    location_previous.edges_id.add(location_current.id);
                    location_current.edges_id.add(location_previous.id);
                }
            }
        }

        Location loc;
        for(Iterator<Location> it = dummySet_current.iterator(); it.hasNext(); ) {
            loc = it.next();
            if(loc.edges_id.size() == 0) {
                it.remove();
            }
        }
        current.UpdateIndexLocationReal();

        if(dummySet_current.size() < k) {
            System.out.println("LocationDummySets: direction filter failed, second set num less than " + k);
            return false;
        }

        for(Location location_previous: dummySet_previous) {
            if(location_previous.edges_id.size() == 0) {
                System.out.println("LocationDummySets: direction filter failed, first set has " +
                        "location not connect to second");
                return false;
            }
        }

        return true;
    }

    private boolean TimeReasonabilityFilter() {
        int dummySetsSize = this.locationDummySets.size();
        LocationDummySet previous = this.locationDummySets.get(dummySetsSize - 2);
        LocationDummySet current = this.locationDummySets.lastElement();
        Vector<Location> dummySet_previous = previous.location_dummy_set;
        Vector<Location> dummySet_current = current.location_dummy_set;
        int k = current.num_dummy;

        int edge;
        for(Location location_previous: dummySet_previous) {
            for(Iterator<Integer> it = location_previous.edges_id.iterator(); it.hasNext(); ) {
                edge = it.next();
                Location location_current = current.GetLocationById(edge);
                double time = util.GetTime(location_previous, location_current);
                double time_diff = current.time - previous.time;
                if(Math.abs(time - time_diff) > Variable.THETA_T * time_diff) {
                    current.DeleteLocationById(edge, location_previous.id);
                    it.remove();
                }
            }
        }

        Location loc;
        for(Iterator<Location> it = dummySet_current.iterator(); it.hasNext(); ) {
            loc = it.next();
            if(loc.edges_id.size() == 0) {
                it.remove();
            }
        }
        current.UpdateIndexLocationReal();

        if(dummySet_current.size() < k) {
            System.out.println("LocationDummySets: time reasonability filter, second set num less than " + k);
            return false;
        }

        for(Location location_previous: dummySet_previous) {
            if(location_previous.edges_id.size() == 0) {
                System.out.println("LocationDummySets: direction filter failed, first set has " +
                        "location not connect to second");
                return false;
            }
        }

        return true;
    }

    private boolean DegreeFilter() {
        int dummySetsSize = this.locationDummySets.size();
        LocationDummySet previous = this.locationDummySets.get(dummySetsSize - 2);
        LocationDummySet current = this.locationDummySets.lastElement();
        Vector<Location> dummySet_previous = previous.location_dummy_set;
        Vector<Location> dummySet_current = current.location_dummy_set;
        Location real_previous = dummySet_previous.get(previous.index_location_real);
        Location real_current = dummySet_current.get(current.index_location_real);
        int k = current.num_dummy;

        boolean tag = true;
        Vector<Location> grater_current = new Vector<Location>();
        Vector<Location> less_current = new Vector<Location>();
        int max_current = real_current.edges_id.size();
        for(Location location_current: dummySet_current) {
            if(location_current.id != real_current.id) {
                if(location_current.edges_id.size() > max_current) {
                    tag = false;
                    grater_current.add(location_current);
                } else {
                    less_current.add(location_current);
                }
            }
        }

        if(tag) {
            System.out.println("LocationDummySets: degree reasonability filter failed, " +
                    "second real location degree is gratest");
            return false;
        }

        int try_num = 100;
        while(try_num > 0) {
            int dummy_k = k - 1;
            int grater_current_size = grater_current.size();
            int less_current_size = less_current.size();
            int  m = (int) (grater_current_size < Math.ceil(dummy_k / 2) ? less_current_size : Math.ceil(dummy_k / 2));
            for(int i = 1; i < m; i++) {
                Vector<Location> candidate = new Vector<Location>();
                candidate.add(real_current);
                Set<Integer> rand_grater = util.GetRand(i, grater_current_size);
                for(int index: rand_grater) {
                    candidate.add(grater_current.get(index));
                }
                Set<Integer> rand_less = util.GetRand(dummy_k - i, less_current_size);
                for(int index: rand_less) {
                    candidate.add(less_current.get(index));
                }

                int[] previous_num = new int[k];

                for(Location location_candidate: candidate) {
                    for(int j = 0; j < location_candidate.edges_id.size(); j++) {
                        previous_num[location_candidate.edges_id.get(j)]++;
                    }
                }

                int max_previous = previous_num[previous.index_location_real];
                for(int num_i = 0; num_i < k; num_i++) {
                    if(previous_num[num_i] > max_previous) {
                        current.location_dummy_set = candidate;
                        current.UpdateIndexLocationReal();
                        return true;
                    }
                }
            }
            try_num--;
        }
        System.out.println("LocationDummySets: degree reasonability filter failed");
        return false;
    }
}
