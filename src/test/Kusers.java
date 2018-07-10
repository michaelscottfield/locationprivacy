package test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
//import java.util.Iterator;
import dummygenerator.Location;
import dummygenerator.LocationDummySets;
import utils.Util;
import dummygenerator.LocationDummySet;

public class Kusers {
    Util util = new Util();
    LocationDummySets locationdummysets = new LocationDummySets();

    public Set<Integer> getalluserids(int idnum, int idmin) {
        Set<Integer> allids;
        Set<Integer> allids2 = new HashSet<>();
        allids = util.GetRand(idnum, idnum * 2);
        Iterator<Integer> iter = allids.iterator();
        int id;
        while (iter.hasNext()) {
            id = iter.next();
            allids2.add(id + idmin);
        }
        return allids2;
    }

    public int getlocid(double longtitude, double latitude, Set<Integer> locids) {
        int tempid;
        double fractionalPart = longtitude - Math.floor(longtitude);
        String fract = String.format("%.5f", fractionalPart);
        fract = fract.substring(2, fract.length() - 1);
        tempid = Integer.parseInt(fract);
        while (locids.contains(tempid)) {
            tempid = tempid + (int) (tempid * Math.random());
        }
        locids.add(tempid);
        return tempid;
    }

    public Vector<Location> getklocations(double reallon, double relalat, Set<Integer> locids, int k, double lonmin, double latmin, double lonmax, double latmax) {
        Vector<Location> kloations = new Vector<>();
        Vector<Double> lons = new Vector<Double>();
        Vector<Double> lats = new Vector<Double>();
        double lontemp;
        double lattemp;
        int tempid;
        for (int i = 0; i < k; i++) {
            lontemp = Double.parseDouble(String.format("%.5f", lonmin + Math.random() * (lonmax - lonmin)));
            while (lons.contains(lontemp) || lontemp == reallon) {
                lontemp = Double.parseDouble(String.format("%.5f", lonmin + Math.random() * (lonmax - lonmin)));
            }
            lons.add(lontemp);
            lattemp = Double.parseDouble(String.format("%.5f", latmin + Math.random() * (latmax - latmin)));
            while (lats.contains(lattemp) || lattemp == relalat) {
                lattemp = Double.parseDouble(String.format("%.5f", latmin + Math.random() * (latmax - latmin)));
            }
            lats.add(lattemp);
            tempid = getlocid(lontemp, lattemp, locids);
            kloations.add(new Location(tempid, lontemp, lattemp));
        }
        return kloations;
    }

    public Set<Integer> getkuserids(Set<Integer> allports, int k, Set<Integer> allids) {
        Set<Integer> kuserids = new HashSet<>();
        double choserate = k / allids.size();
        double randomp;
        while (kuserids.size() < k) {
            for (int id : allids) {
                randomp = Math.random();
                if (randomp >= choserate && !kuserids.contains(id) && !allports.contains(id)) {
                    kuserids.add(id);
                    allports.add(id);
                }
                if (kuserids.size() == k) {
                    break;
                }
            }
        }
        return kuserids;
    }

    public void sendtokusers(Server userserver, int lbsid, int userid, Set<Integer> kuserids, Location realoc, Vector<Location> klocations) {
        int k = kuserids.size();
        //System.out.println(k);
        LocationDummySet dummies = locationdummysets.GetDummySet(realoc, k + 1);
        int i = 0;
        userserver.longitude = realoc.longitude;
        userserver.latitude = realoc.latitude;
        //System.out.println("userid:" + userid);
        Vector<Server> kservers = new Vector<Server>();
        for (int id : kuserids) {
            System.out.println(id);
            kservers.add(new Server(id, id, klocations.get(i).longitude, klocations.get(i).latitude));
            i++;
        }

        for (i = 0; i < k; i++) {
            userserver.send(kservers.get(i).id, lbsid, dummies.location_dummy_set.get(i).longitude, dummies.location_dummy_set.get(i).latitude);
            i++;
        }
    }
}
