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
        Kusers kusers = new Kusers();
        double delta = 0.00300;
        Vector<Double>reallon = new Vector<Double>();
        Vector<Double>reallat = new Vector<Double>();
//        Server client = new Server(20001, 20001, 114, 34);
//        Server middle = new Server(20002, 20002, 114, 35);
//        Server provider = new Server(20003, 20003, 115, 34);
//        Server middle2 = new Server(20004, 20004, 115, 35);
//        Server middle3 = new Server(20005, 20005, 115, 36);
//
//        client.send(20002, 20003, 114, 34);
//        client.send(20004, 20003, 115, 35);
//        client.send(20005, 20003, 116, 35);
        /*LocationDummySets locationDummySets = new LocationDummySets();
        Location first = new Location(0, -118.23556, 34.10444);
        LocationDummySet dummySet_first = locationDummySets.GetDummySet(first, 10);
        Location second = new Location(0, -118.23806, 34.09889);
        LocationDummySet dummySet_second = locationDummySets.GetDummySet(second, 10);
        System.out.println("fa");*/
        int idnum = 100;
        int idmin = 20002;
        Set<Integer> allids = kusers.getalluserids(idnum, idmin);
        Set<Integer> locids = new HashSet<>();
        Set<Integer> kuserids;
        Set<Integer> allports = new HashSet<>();
        int k = 9;
        int lbsid;
        lbsid = 23000;
        Server lbsserver = new Server(lbsid, lbsid, -118.23576, 34.10484);
        int userid = 20001;
        allports.add(lbsid);
        allports.add(userid);
        reallon.add(-118.23556);
        reallon.add(-118.23806);
        reallat.add(34.10444);
        reallat.add(34.09889);
        Server userserver = new Server(userid, userid, reallon.get(0), reallat.get(0));
        Location realloc = new Location(kusers.getlocid(reallon.get(0), reallat.get(0), locids),reallon.get(0), reallat.get(0));
        Vector<Location> klocations = kusers.getklocations(reallon.get(0), reallat.get(0), locids, k, reallon.get(0) - delta, reallat.get(0) - delta, reallon.get(0) + delta, reallat.get(0) + delta);
        kuserids = kusers.getkuserids(allports, k, allids);
        kusers.sendtokusers(userserver, lbsid, userid, kuserids, realloc, klocations);
        Location secondrealloc = new Location(kusers.getlocid(reallon.get(1), reallat.get(1), locids),reallon.get(1), reallat.get(1));
        Vector<Location> secondklocations = kusers.getklocations(reallon.get(1), reallat.get(1), locids, k, reallon.get(1) - delta, reallat.get(1) - delta, reallon.get(1) + delta, reallat.get(1) + delta);
        Set<Integer> secondkuserids;
        secondkuserids = kusers.getkuserids(allports, k, allids);
        kusers.sendtokusers(userserver, lbsid, userid, secondkuserids, secondrealloc, secondklocations);
    }
}
