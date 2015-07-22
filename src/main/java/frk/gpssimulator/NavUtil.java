/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package frk.gpssimulator;

import frk.gpssimulator.model.Point;

/**
 * Navigation utility functions.
 * @author faram
 */
public class NavUtil {
	//earth's radius 6371000 metres
	static Integer R = 6371000;

	/**
	 * Returns distance (in metres) between 2 points.
	 * @param pt1
	 * @param pt2
	 * @return
	 */
	public static Double getDistance(Point pt1, Point pt2) {
		double lat1 = Math.toRadians(pt1.getLatitude());
		double lon1 = Math.toRadians(pt1.getLongitude());
		double lat2 = Math.toRadians(pt2.getLatitude());
		double lon2 = Math.toRadians(pt2.getLongitude());

		double dLat = lat2 - lat1;
		double dLon = lon2 - lon1;
		double a = Math.pow(Math.sin(dLat/2),2) + Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin(dLon/2),2);
		double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		double d = R*c;

		return d;
	}//getDistance----------------------------------------


	/**
	 * Returns bearing of position 2 from position 1.
	 * @param pt1
	 * @param pt2
	 * @return
	 */
	public static Double getBearing(Point pt1, Point pt2) {
		double longitude1 = pt1.getLongitude();
		double longitude2 = pt2.getLongitude();
		double latitude1 = Math.toRadians(pt1.getLatitude());
		double latitude2 = Math.toRadians(pt2.getLatitude());
		double longDiff= Math.toRadians(longitude2-longitude1);
		double y= Math.sin(longDiff)*Math.cos(latitude2);
		double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

		return (Math.toDegrees(Math.atan2(y, x))+360)%360;

	}//getBearing------------------------------------------


	/**
	 * Returns coords of position which is given distance and bearing from given point.
	 * @param pt1
	 * @param dist
	 * @param brg
	 * @return
	 */
	public static Point getPosition(Point pt1, double d, double brg) {
		if(d == 0) {
			return pt1;
		}

		double lat1 = Math.toRadians(pt1.getLatitude());
		double lon1 = Math.toRadians(pt1.getLongitude());
		brg = Math.toRadians(brg);

		double lat2 = Math.asin(Math.sin(lat1)*Math.cos(d/R) + Math.cos(lat1)*Math.sin(d/R)*Math.cos(brg));
		double x = Math.sin(brg)*Math.sin(d/R)*Math.cos(lat1);
		double y = Math.cos(d/R) - Math.sin(lat1)*Math.sin(lat2);
		double lon2 = lon1 + Math.atan2(x, y);

		Point pos = new Point(Math.toDegrees(lat2), Math.toDegrees(lon2), null);

		return pos;

	}//getPosition---------------------------------------


}
