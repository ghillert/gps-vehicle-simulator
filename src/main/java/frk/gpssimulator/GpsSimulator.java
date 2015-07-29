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

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import frk.gpssimulator.model.Leg;
import frk.gpssimulator.model.Point;
import frk.gpssimulator.model.PositionInfo;
import frk.gpssimulator.service.GpsdService;
import frk.gpssimulator.service.KmlService;
import frk.gpssimulator.support.NavUtils;

/**
 * Simulates a vehicle moving along a path defined in a kml file.
 *
 * @author faram
 * @author Gunnar Hillert
 */
public class GpsSimulator implements Runnable {

	private long id;

	private MessageChannel messageChannel;

	private KmlService kmlService;
	private GpsdService gpsdService;

	private boolean useGpsd = false;

	private AtomicBoolean cancel = new AtomicBoolean();

	private Double speedInMps; // In meters/sec
	private Boolean shouldMove;
	private boolean exportPositionsToKml = false;

	private Integer reportInterval = 500; // millisecs at which to send position reports
	private PositionInfo currentPosition = null;
	private List<Leg> legs;

	@Override
	public void run() {
		try {

			if (cancel.get()) {
				destroy();
				return;
			}
			while (!Thread.interrupted()) {
				long startTime = new Date().getTime();
				if (currentPosition != null) {
					if (shouldMove) {
						moveVehicle();
						currentPosition.setSpeed(speedInMps);
					} else {
						currentPosition.setSpeed(0.0);
					}

					if (this.exportPositionsToKml) {
						kmlService.updatePosition(id, currentPosition);
					}

					if (useGpsd) {
						this.gpsdService.updatePosition(currentPosition);
					}

					messageChannel.send(MessageBuilder.withPayload(currentPosition).build());
				}

				// wait till next position report is due
				sleep(startTime);
			} // loop endlessly
		} catch (InterruptedException ie) {
			destroy();
			return;
		}

		destroy();
	}

	/**
	 * On thread interrupt. Send null position to all consumers to indicate that
	 * sim has closed.
	 */
	void destroy() {
		currentPosition = null;
	}

	/**
	 * Sleep till next position report is due.
	 *
	 * @param startTime
	 * @throws InterruptedException
	 */
	private void sleep(long startTime) throws InterruptedException {
		long endTime = new Date().getTime();
		long elapsedTime = endTime - startTime;
		long sleepTime = reportInterval - elapsedTime > 0 ? reportInterval - elapsedTime : 0;
		Thread.sleep(sleepTime);
	}

	/**
	 * Set new position of vehicle based on current position and vehicle speed.
	 */
	void moveVehicle() {
		Double distance = speedInMps * reportInterval / 1000.0;
		Double distanceFromStart = currentPosition.getDistanceFromStart() + distance;
		Double excess = 0.0; // amount by which next postion will exceed end
								// point of present leg

		for (int i = currentPosition.getLeg().getId(); i < legs.size(); i++) {
			Leg currentLeg = legs.get(i);
			excess = distanceFromStart > currentLeg.getLength() ? distanceFromStart - currentLeg.getLength() : 0.0;

			if (Double.doubleToRawLongBits(excess) == 0) {
				// this means new position falls within current leg
				currentPosition.setDistanceFromStart(distanceFromStart);
				currentPosition.setLeg(currentLeg);
				Point newPosition = NavUtils.getPosition(currentLeg.getStartPosition(), distanceFromStart,
						currentLeg.getHeading());
				currentPosition.setPosition(newPosition);
				return;
			}
			distanceFromStart = excess;
		}

		// if we've reached here it means vehicle has moved beyond end of path
		// so go back to start of path
		setStartPosition();
	}

	/**
	 * Position vehicle at start of path.
	 */
	public void setStartPosition() {
		currentPosition = new PositionInfo();
		Leg leg = legs.get(0);
		currentPosition.setLeg(leg);
		currentPosition.setPosition(leg.getStartPosition());
		currentPosition.setDistanceFromStart(0.0);
	}

	/**
	 * @return the speed
	 */
	public Double getSpeedInMps() {
		return speedInMps;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeedInMps(Double speed) {
		this.speedInMps = speed;
	}

	public void setSpeedInKph(Double speed) {
		this.speedInMps = speed / 3.6;
	}

	public Double getSpeedInKph() {
		return this.speedInMps * 3.6;
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

	public void setMessageChannel(MessageChannel sendPosition) {
		this.messageChannel = sendPosition;
	}

	public synchronized void cancel() {
		this.cancel.set(true);
	}

	public void setExportPositionsToKml(boolean exportPositionsToKml) {
		this.exportPositionsToKml = exportPositionsToKml;
	}

	public void setKmlService(KmlService kmlService) {
		this.kmlService = kmlService;
	}

	public void setGpsdService(GpsdService gpsdService) {
		this.gpsdService = gpsdService;
	}

	public void setLegs(List<Leg> legs) {
		this.legs = legs;
	}

	public PositionInfo getCurrentPosition() {
		return currentPosition;
	}

	public void setCurrentPosition(PositionInfo currentPosition) {
		this.currentPosition = currentPosition;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
