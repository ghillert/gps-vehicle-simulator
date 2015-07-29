/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package frk.gpssimulator.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import frk.gpssimulator.GpsSimulator;
import frk.gpssimulator.model.DirectionInput;
import frk.gpssimulator.model.GpsSimulatorInstance;
import frk.gpssimulator.model.Point;
import frk.gpssimulator.service.GpsSimulatorFactory;
import frk.gpssimulator.service.KmlService;
import frk.gpssimulator.service.PathService;

/**
 *
 * @author Gunnar Hillert
 *
 */
@RestController
@RequestMapping("/api")
public class RestApi {

	@Autowired
	private PathService pathService;

	@Autowired
	private KmlService kmlService;

	@Autowired
	private GpsSimulatorFactory gpsSimulatorFactory;

	@Autowired
	@Qualifier("sendPosition")
	private MessageChannel messageChannel;

	@Autowired
	private AsyncTaskExecutor taskExecutor;

	private Map<Long, GpsSimulatorInstance> taskFutures = new HashMap<>();

	long instanceCounter = 1;

	@RequestMapping("/dc")
	public List<GpsSimulatorInstance>dc() {
		List<DirectionInput> inputs = this.pathService.loadDirectionInput();
		List<GpsSimulatorInstance> instances = new ArrayList<>();
		Point lookAtPoint = null;

		final Set<Long> instanceIds = new HashSet<>(taskFutures.keySet());

		for (DirectionInput directionInput : inputs) {
			List<Point> points = this.pathService.getCoordinatesFromGoogle(directionInput);

			if (lookAtPoint == null) {
				lookAtPoint = points.get(0);
			}

			final GpsSimulator gpsSimulator = new GpsSimulator();
			gpsSimulator.setMessageChannel(messageChannel);
			gpsSimulator.setKmlService(kmlService);
			gpsSimulator.setShouldMove(true);
			gpsSimulator.setExportPositionsToKml(true);
			gpsSimulator.setSpeedInKph(40d);
			gpsSimulator.setId(instanceCounter);

			instanceIds.add(instanceCounter);
			gpsSimulatorFactory.prepareGpsSimulator(gpsSimulator, points);

			final Future<?> future = taskExecutor.submit(gpsSimulator);
			final GpsSimulatorInstance instance = new GpsSimulatorInstance(instanceCounter, gpsSimulator, future);
			taskFutures.put(instanceCounter, instance);
			instanceCounter++;
			instances.add(instance);
		}

		kmlService.setupKmlIntegration(instanceIds, lookAtPoint);

		return instances;
	}


	@RequestMapping("/start")
	public GpsSimulatorInstance start() {
		final GpsSimulator gpsSimulator = new GpsSimulator();
		gpsSimulator.setMessageChannel(messageChannel);
		gpsSimulator.setKmlService(kmlService);
		gpsSimulator.setShouldMove(true);
		gpsSimulator.setExportPositionsToKml(true);
		gpsSimulator.setSpeedInKph(40d);
		gpsSimulator.setId(instanceCounter);
		final Set<Long> instanceIds = new HashSet<>(taskFutures.keySet());
		instanceIds.add(instanceCounter);
		kmlService.setupKmlIntegration(instanceIds);

		gpsSimulatorFactory.prepareGpsSimulator(gpsSimulator, new File("src/data/test-route-1.kml"));

		final Future<?> future = taskExecutor.submit(gpsSimulator);
		final GpsSimulatorInstance instance = new GpsSimulatorInstance(instanceCounter, gpsSimulator, future);
		taskFutures.put(instanceCounter, instance);
		instanceCounter++;
		return instance;
	}

	@RequestMapping("/status")
	public Collection<GpsSimulatorInstance> status() {
		return taskFutures.values();
	}

	@RequestMapping("/cancel")
	public int cancel() {
		int numberOfCancelledTasks = 0;
		for (Map.Entry<Long, GpsSimulatorInstance> entry : taskFutures.entrySet()) {
			GpsSimulatorInstance instance = entry.getValue();
			instance.getGpsSimulator().cancel();
			boolean wasCancelled = instance.getGpsSimulatorTask().cancel(true);
			if (wasCancelled) {
				numberOfCancelledTasks++;
			}
		}
		taskFutures.clear();
		return numberOfCancelledTasks;
	}

	@RequestMapping("/directions")
	public List<DirectionInput> directions() {
		return pathService.loadDirectionInput();
	}

}
