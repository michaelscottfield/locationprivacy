package dummies;
import java.util.*;
public class Utils {
	double Radian(double d) {
		return d * Stdafx.PI / 180.0;
	}

	public double GetDistance(double lon1, double lat1, double lon2, double lat2) {
		double radLon1 = Radian(lon1);
		double radLon2 = Radian(lon2);
		double radLat1 = Radian(lat1);
		double radLat2 = Radian(lat2);

		double b = radLon1 - radLon2;
		double a = radLat1 - radLat2;
		double dst = 2 * Math.asin((Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2))));

		dst = dst * Stdafx.EARTH_RADIUS * 1000;
		return dst;
	}

	Set<Integer> GetRand(int m, int n) {
		Set<Integer> s = new HashSet<Integer>();
		while (true) {
			int r = (int)Math.random() * n;
			s.add(r);
			if (s.size() == m)
				break;
		}
		return s;
	}
}
