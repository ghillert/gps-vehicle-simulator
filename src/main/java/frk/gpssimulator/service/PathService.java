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
package frk.gpssimulator.service;

import java.io.File;
import java.util.List;

import frk.gpssimulator.model.DirectionInput;
import frk.gpssimulator.model.Point;

/**
 *
 * @author Gunnar Hillert
 *
 */
public interface PathService {

	/**
	 *
	 * @return
	 */
	List<DirectionInput> loadDirectionInput();

	/**
	 *
	 * @param directionInput
	 * @return
	 */
	List<Point> getCoordinatesFromGoogle(DirectionInput directionInput);

	/**
	 * Returns list of points contained in the path kml file.
	 * @param kmlFile path kml file
	 * @return
	 */
	List<Point> getCoordinatesFromKmlFile(File kmlFile);
}