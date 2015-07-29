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
package frk.gpssimulator.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import frk.gpssimulator.GpsSimulator;
import frk.gpssimulator.model.Leg;
import frk.gpssimulator.model.Point;
import frk.gpssimulator.service.GpsSimulatorFactory;
import frk.gpssimulator.service.PathService;
import frk.gpssimulator.support.NavUtils;

/**
 * @author Gunnar Hillert
 *
 */
@Service
public class DefaultGpsSimulatorFactory implements GpsSimulatorFactory {

	@Autowired
	private PathService pathService;

	@Override
	public GpsSimulator prepareGpsSimulator(GpsSimulator gpsSimulator, File kmlFile) {

		final List<Point> points;

		if (kmlFile == null) {
			points = this.pathService.getCoordinatesFromGoogle(this.pathService.loadDirectionInput().get(0));
		}
		else {
			points = this.pathService.getCoordinatesFromKmlFile(kmlFile);
		}

		return prepareGpsSimulator(gpsSimulator, points);
	}

	@Override
	public GpsSimulator prepareGpsSimulator(GpsSimulator gpsSimulator, List<Point> points) {
		gpsSimulator.setCurrentPosition(null);;
		final List<Leg>legs = createLegsList(points);
		gpsSimulator.setLegs(legs);
		gpsSimulator.setStartPosition();
		return gpsSimulator;
	}

	/**
	 * Creates list of legs in the path
	 *
	 * @param points
	 */
	private List<Leg> createLegsList(List<Point> points) {
		final List<Leg>legs = new ArrayList<Leg>();
		for (int i = 0; i < (points.size() - 1); i++) {
			Leg leg = new Leg();
			leg.setId(i);
			leg.setStartPosition(points.get(i));
			leg.setEndPosition(points.get(i + 1));
			Double length = NavUtils.getDistance(points.get(i), points.get(i + 1));
			leg.setLength(length);
			Double heading = NavUtils.getBearing(points.get(i), points.get(i + 1));
			leg.setHeading(heading);
			legs.add(leg);
		}
		return legs;
	}
}
