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
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;

import frk.gpssimulator.model.Point;

/**
 * @author hillert
 *
 */
public class KmlUtilTests {

	private KmlUtil kmlUtil = new KmlUtil();

	/**
	 * Test method for {@link frk.gpssimulator.KmlUtil#getCoordinates(java.io.File)}.
	 * @throws JAXBException
	 * @throws NumberFormatException
	 */
	@Test
	public void testGetCoordinates() throws NumberFormatException, JAXBException {

		File file = new File("src/data/test-route-1.kml");

		Assert.assertTrue(file.exists());
		Assert.assertTrue(file.isFile());

		List<Point> points = kmlUtil.getCoordinates(file);

		Assert.assertTrue(points.size() > 0);

		for (Point point : points) {
			System.out.println(String.format("Lat/Lang: %s,%s | Altitude: %s",
					point.getLatitude(), point.getLongitude(), point.getAltitude()));
		}

		Assert.assertEquals(Integer.valueOf(167), Integer.valueOf(points.size()));
	}

	/**
	 * Test method for {@link frk.gpssimulator.KmlUtil#createGpsKml(java.io.File)}.
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testCreateGpsKml() throws FileNotFoundException, JAXBException {
		kmlUtil.createGpsKml(new File("gps.kml"));
	}

	/**
	 * Test method for {@link frk.gpssimulator.KmlUtil#createPosKml(frk.gpssimulator.PositionInfo)}.
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	@Test
	public void testCreatePosKml() throws FileNotFoundException, JAXBException {
		kmlUtil.createPosKml(null);
	}

}
