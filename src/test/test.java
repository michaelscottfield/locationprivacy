package test;

import dummygenerator.Location;
import dummygenerator.LocationDummySet;
import dummygenerator.LocationDummySets;

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
        LocationDummySets locationDummySets = new LocationDummySets();
        Location first = new Location(0, -118.23556, 34.10444);
        LocationDummySet dummySet_first = locationDummySets.GetDummySet(first, 10);
        Location second = new Location(0, -118.23806, 34.09889);
        LocationDummySet dummySet_second = locationDummySets.GetDummySet(second, 10);
        System.out.println("fa");
    }
}
