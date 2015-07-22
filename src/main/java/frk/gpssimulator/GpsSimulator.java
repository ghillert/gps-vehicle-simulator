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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import frk.gpssimulator.model.Leg;
import frk.gpssimulator.model.Point;
import frk.gpssimulator.model.PositionInfo;

/**
 * Simulates a vehicle moving along a path defined in a kml file.
 * @author faram
 */
public class GpsSimulator implements Runnable {
	//-- set by caller

	private KmlUtil kmlUtil = new KmlUtil();
	private Dispatcher dispatcher;
	private Double speed;   // metres/sec
	private Boolean shouldMove;
	//-- set by caller
	Integer reportInterval = 100;   //millisecs at which to send position reports
	PositionInfo currentPosition = null;
	List<Leg> legs;
	Integer legIndex = 0;
	Double legHeading;

	public void run() {
		try {
			while (!Thread.interrupted()) {
				long startTime = new Date().getTime();
				if (currentPosition != null) {
					if(shouldMove) {
						moveVehicle();
						currentPosition.setSpeed(speed);
					} else {
						currentPosition.setSpeed(0.0);
					}
				}

				dispatcher.send(currentPosition);
				//wait till next position report is due
				sleep(startTime);
			}//loop endlessly
		} catch(InterruptedException ie) {
			destroy();
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		destroy();
	}//run----------------


	/**
	 * On thread interrupt.
	 * Send null position to all consumers to indicate that sim has closed.
	 */
	void destroy() {
		currentPosition = null;
		dispatcher.send(currentPosition);
	}

	/**
	 * Sleep till next position report is due.
	 * @param startTime
	 * @throws InterruptedException
	 */
	private void sleep(long startTime) throws InterruptedException {
		long endTime = new Date().getTime();
		long elapsedTime = endTime - startTime;
		long sleepTime = (reportInterval - elapsedTime) > 0 ? (reportInterval - elapsedTime) : 0;
		Thread.sleep(sleepTime);
	}

	/**
	 * Loads path from path kml file and positions vehicle at start of path.
	 * @param pathFile
	 */
	public void loadPathKml(File pathFile) throws JAXBException, NumberFormatException {
		currentPosition = null;
		List<Point>points = kmlUtil.getCoordinates(pathFile);
		createLegsList(points);
		setStartPosition();
	}//loadPathKml--------------


/**
	* Creates list of legs in the path
	* @param points
	*/
	void createLegsList(List<Point> points) {
		legs = new ArrayList<Leg>();
		for(int i=0; i<(points.size()-1); i++) {
			Leg leg = new Leg();
			leg.setId(i);
			leg.setStartPosition(points.get(i));
			leg.setEndPosition(points.get(i+1));
			Double length = NavUtil.getDistance(points.get(i), points.get(i+1));
			leg.setLength(length);
			Double heading = NavUtil.getBearing(points.get(i), points.get(i+1));
			leg.setHeading(heading);
			legs.add(leg);
		}


	}//createLegsList--------------

	/**
	 * Set new position of vehicle based on current position and vehicle speed.
	 */
	void moveVehicle() {
		Double distance = speed *  reportInterval/1000.0;
		Double distanceFromStart = currentPosition.getDistanceFromStart() + distance;
		Double excess = 0.0;    // amount by which next postion will exceed end point of present leg

		for(int i = currentPosition.getLeg().getId(); i < legs.size(); i++) {
			Leg currentLeg = legs.get(i);
			excess = (distanceFromStart > currentLeg.getLength()) ? (distanceFromStart - currentLeg.getLength()) : 0.0;
			//System.out.println("leg: "+currentLeg.getId()+" legLength: "+currentLeg.getLength()+" excess: "+excess+ " distanceFromStart: "+distanceFromStart);
			if(excess == 0.0) {
				//this means new position falls within current leg
				currentPosition.setDistanceFromStart(distanceFromStart);
				currentPosition.setLeg(currentLeg);
				Point newPosition = NavUtil.getPosition(currentLeg.getStartPosition(), distanceFromStart, currentLeg.getHeading());
				currentPosition.setPosition(newPosition);
				return;
			}
			distanceFromStart = excess;
		}

		//if we've reached here it means vehicle has moved beyond end of path so go back to start of path
		setStartPosition();


	}//setPosition--------------------------

	/**
	 * Position vehicle at start of path.
	 */
	void setStartPosition() {
		currentPosition = new PositionInfo();
		Leg leg = legs.get(0);
		currentPosition.setLeg(leg);
		currentPosition.setPosition(leg.getStartPosition());
		currentPosition.setDistanceFromStart(0.0);
	}//setStartPosition------------------

	/**
	 * @return the dispatcher
	 */
	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * @param dispatcher the dispatcher to set
	 */
	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	/**
	 * @return the speed
	 */
	public Double getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(Double speed) {
		this.speed = speed;
		//System.out.println("speed is "+speed);
	}

	/**
	 * @return the shouldMove
	 */
	public Boolean getShouldMove() {
		return shouldMove;
	}

	/**
	 * @param shouldMove the shouldMove to set
	 */
	public void setShouldMove(Boolean shouldMove) {
		this.shouldMove = shouldMove;
	}
}
