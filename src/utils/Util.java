package utils;

import java.util.HashSet;
import java.util.Set;
import utils.Variable;
import dummygenerator.Location;

public class Util {
    private double Radian(double d) {
        return d * Variable.PI / 180.0;
    }

    public double GetDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLon1 = Radian(lon1);
        double radLon2 = Radian(lon2);
        double radLat1 = Radian(lat1);
        double radLat2 = Radian(lat2);

        double b = radLon1 - radLon2;
        double a = radLat1 - radLat2;
        double dst = 2 * Math.asin((Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2))));

        dst = dst * Variable.EARTH_RADIUS * 1000;
        return dst;
    }

    public Set<Integer> GetRand(int m, int n) {
        Set<Integer> s = new HashSet<Integer>();
        while (true) {
            int r = (int)(Math.random() * n);
            s.add(r);
            if (s.size() == m)
                break;
        }
        return s;
    }

    public double AngleVectors(Location l1, Location l2, Location l3, Location l4) {
        double vector1_y = l2.latitude - l1.latitude;
        double vector1_x = l2.longitude - l1.longitude;
        double vector2_y = l4.latitude - l3.latitude;
        double vector2_x = l4.longitude - l3.longitude;

        double a = vector1_x * vector2_x + vector1_y * vector2_y;
        double b = Math.sqrt(Math.pow(vector1_x, 2) + Math.pow(vector1_y, 2)) * Math.sqrt(Math.pow(vector2_x, 2) + Math.pow(vector2_y, 2));
        if (b == 0) {
            return 2 * Variable.PI;
        }
        return Math.acos(a / b);
    }

    public double GetTime(Location l1, Location l2) {
        return this.GetDistance(l1.longitude, l1.latitude, l2.longitude, l2.latitude) / Variable.SPEED;
    }
}
