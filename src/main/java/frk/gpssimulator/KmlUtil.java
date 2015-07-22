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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.Link;
import de.micromata.opengis.kml.v_2_2_0.NetworkLink;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.RefreshMode;
import frk.gpssimulator.model.Point;
import frk.gpssimulator.model.PositionInfo;

/**
 * KML utility functions.
 * Each thread that utilizes this class should instantiate a new instance of it because jaxb marshal/unmarshalling is
 * not thread safe.
 * @author faram
 */
public class KmlUtil {

	private final JAXBContext jc;
	private final Marshaller marshaller;
	private final Unmarshaller unmarshaller;

	public KmlUtil() {
		try {
			jc = JAXBContext.newInstance(Kml.class);
			marshaller = jc.createMarshaller();
			unmarshaller = jc.createUnmarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
		} catch(JAXBException e) {
			throw new IllegalStateException(e);
		}
	}//---------------------


	/**
	 * Returns list of points contained in the path kml file.
	 * @param f path kml file
	 * @return
	 * @throws JAXBException
	 * @throws NumberFormatException
	 */
	public final List<Point> getCoordinates(File f) throws JAXBException, NumberFormatException {

		final Kml kml = (Kml) unmarshaller.unmarshal(f);

		final Document doc = (Document) kml.getFeature();
		List<Feature> features = doc.getFeature();
		List<Point> pointsToReturn = new ArrayList<Point>();

		for (Feature feature : features) {
			System.out.println(feature.getName());
			if (feature instanceof Placemark) {
				final Placemark placemark = (Placemark) feature;
				if (placemark.getGeometry() instanceof LineString) {
					final LineString lineString = (LineString) placemark.getGeometry();
					List<Coordinate> coordinates = lineString.getCoordinates();
					for(Coordinate coord : coordinates) {
						Point point2 = new Point(
								coord.getLatitude(),
								coord.getLongitude(),
								coord.getAltitude());
						pointsToReturn.add(point2);
					}
					break;
				}
			}

		}
		return pointsToReturn;

	}

	/**
	 * Creates a kml object containing a network link and writes it to given file.
	 * This file is read by google earth. The file points to another file 'pos.kml' which has
	 * the latest position and is read periodically by google earth.
	 * @param f kml file
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	public final void createGpsKml(File f) throws JAXBException, FileNotFoundException {
		Link link = KmlFactory.createLink();
		link.setHref("pos.kml");
		link.setRefreshMode(RefreshMode.ON_INTERVAL);
		link.setRefreshInterval(1.0);

		NetworkLink networkLink = KmlFactory.createNetworkLink();
		networkLink.setName("GPS");
		networkLink.setOpen(Boolean.TRUE);
		networkLink.setLink(link);

		Kml kml = KmlFactory.createKml();
		kml.setFeature(networkLink);

		OutputStream out = new FileOutputStream(f);
		marshaller.marshal(kml, out);
	}

	public void createPosKml(PositionInfo position) throws JAXBException, FileNotFoundException {
		de.micromata.opengis.kml.v_2_2_0.Point point = KmlFactory.createPoint();
		Integer speedKph = 0;

		if(position != null) {
			Coordinate coordinate = KmlFactory.createCoordinate(position.getPosition().getLongitude(), position.getPosition().getLatitude());
			point.getCoordinates().add(coordinate);
		}
		else {
			Coordinate coordinate = KmlFactory.createCoordinate(0,0);
			point.getCoordinates().add(coordinate);
		}

		Placemark placemark = KmlFactory.createPlacemark();

		if(position != null) {
			Double speed = position.getSpeed();
			speedKph = (int)(speed * 3600 / 1000);
		}
		else {
			speedKph = 0;
		}

		placemark.setName(speedKph.toString()+" kph");
		placemark.setGeometry(point);

		Kml kml = KmlFactory.createKml();
		kml.setFeature(placemark);
		OutputStream out = new FileOutputStream("pos.kml");
		marshaller.marshal(kml, out);


	}//createPosKml-------------------------

}
