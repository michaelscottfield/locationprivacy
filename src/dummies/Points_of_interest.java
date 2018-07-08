package dummies;

import java.util.Iterator;
import java.util.Vector;
//import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Points_of_interest {
	Vector<Location> poi = new Vector<Location>();
	boolean AddLocation(Location location) {
		int tag = 1;
		//if(poi.size() == 0) {
			//System.out.println("unbelieveble");
		//}
		/*if(poi.size() == 0) {
			this.poi.add(location);
			return true;
		}*/
		for (Iterator<Location> iter = this.poi.iterator(); iter.hasNext();) {
			//System.out.println(poi.size());
			if (iter.next().longitude == location.longitude && iter.next().latitude == location.latitude) {
				tag = 0;
				break;
			}
		}

		if (tag == 0) {
			System.out.println("points_of_interest: already have this location");
			//cout << "points_of_interest: already have this location" << endl;
			return false;
		}

		this.poi.add(location);
		return true;
	}

	boolean CreatePoi() throws IOException {
		//ifstream fin;
		//fin.open("./resources/LosAngeles.CA", ifstream::in);
		//File file = new File("./resources/LosAngeles.CA");
		FileInputStream inputStream = new FileInputStream("./resources/LosAngeles.CA");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String str = null;
		String sArray[];
		while((str = bufferedReader.readLine()) != null)
		{
			sArray = str.split(" ");
			Location location = new Location();
			location.longitude = Double.valueOf(sArray[1].toString());
			location.latitude = Double.valueOf(sArray[2].toString());
			this.AddLocation(location);
		}
		//if (fin) {
		/*if(file.isFile()) {
			while (getline(fin, line)) {
				vector<string> res = StringSplit(line, " ");
				Location location;
				location.longitude = atof(res[1].c_str());
				location.latitude = atof(res[2].c_str());
				this->AddLocation(location);
			}
		}*/
		inputStream.close();
		bufferedReader.close();
		return true;
	}
}
