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

import javax.swing.SwingUtilities;

/**
 * Entry point into the app. Initializes and starts all required classes and threads.
 * @author faram
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			GpsSimulator sim = new GpsSimulator();
			final Gui gui = new Gui();
			//define the dispatcher which sends position reports to various consumers
			Dispatcher dispatcher = new Dispatcher(gui);

			Thread simThread = new Thread(sim);
			simThread.setName("GpsSimulator");

			//start the gui in the event dispatcher thread
			gui.setSim(sim);
			gui.setSimThread(simThread);
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					gui.setVisible(true);
				}
			});

			//start the gps simulator thread
			sim.setDispatcher(dispatcher);
			simThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}


	}//main------------------------------------


}
