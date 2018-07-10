package test;

//import dummygenerator.Location;
//import dummygenerator.LocationDummySet;
//import dummygenerator.LocationDummySets;

import dummygenerator.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class test {

    public static void main(String[] args) {
//        Server client = new Server(20001, 20001, 114, 34);
//        Server middle = new Server(20002, 20002, 114, 35);
//        Server provider = new Server(20003, 20003, 115, 34);
//        Server middle2 = new Server(20004, 20004, 115, 35);
//        Server middle3 = new Server(20005, 20005, 115, 36);
//
//        client.send(20002, 20003, 114, 34);
//        client.send(20004, 20003, 115, 35);
//        client.send(20005, 20003, 116, 35);
        Kusers kusers = new Kusers();
        int mink = 9;
        int maxk = 40;
        int userid = 20001;
        double delta = 0.00300;
        int lbsid = 20004;
        //int k = 9;
        int idnum = 100;
        int idmin = 20005;
        Set<Integer> locids = new HashSet<>();
        Set<Integer> allusedids = new HashSet<>();
        Set<Integer> allids = kusers.getalluserids(idnum, idmin);
        Vector<Double> reallon = new Vector<Double>();
        Vector<Double> reallat = new Vector<Double>();
        reallon.add(-118.23556);
        reallon.add(-118.23806);
        reallat.add(34.10444);
        reallat.add(34.09889);
        Server userserver;
        userserver = new Server(userid, userid, reallon.get(0), reallat.get(0));
        Server lbsserver = new Server(lbsid, lbsid, -118.23576, 34.10484);
        Vector<Location> alllocations = kusers.getklocations(-118.23556, 34.10444, locids, idnum, -118.23556 - delta, 34.10444 - delta, -118.23556, 34.10444 + delta);
        int i = 0;
        for (int id : allids) {
            new Server(id, id, alllocations.get(i).longitude, alllocations.get(i).latitude);
        }
        allusedids.add(lbsid);
        allusedids.add(userid);
        for(int k = mink; k < maxk; k ++) {
            userserver.send(userid, allids, allusedids, k, lbsid, reallon, reallat);
        }
    }
}
