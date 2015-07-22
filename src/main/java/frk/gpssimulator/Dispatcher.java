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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBException;

import frk.gpssimulator.model.PositionInfo;

/**
 * Sends position reports to the following consumers -
 * 1. Gui position labels.
 * 2. Google Earth via file pos.kml. GE periodially reads file gps.kml which refers to pos.kml.
 * 3. NMEA RMC position report to GPSD (linux gps daemon) via pipe /tmp/gps.
 * @author faram
 */
public class Dispatcher {

	private KmlUtil kmlUtil = new KmlUtil();
	private Gui gui;
	private Boolean useGpsd = false;
	private Boolean useGoogleEarth = false;
	String gpsdPipe = "/tmp/gps";
	BufferedWriter pipeWriter;

	/**
	 * Initialize stuff on the filesystem required by the dispatcher.
	 * @param gui
	 */
	public Dispatcher(Gui gui) {
		this.gui = gui;

		if (System.getProperty("os.name").contains("Linux")) {
			try {
				initGpsd();
				useGpsd = true;
			} catch (Exception gpe) {
				gpe.printStackTrace();
				showError(gpe.toString());
			}
		}

		try {
			initGoogleEarth();
			useGoogleEarth = true;
		} catch (Exception gpe) {
			gpe.printStackTrace();
			showError(gpe.toString());
		}

	}//constructor----------------------------------------

	/**
	 * Initialize gpsd.
	 * @return error message, if any
	 */
	void initGpsd() throws IOException, InterruptedException {
		startProc("killall -9 gpsd", false);
		startProc("rm -f " + gpsdPipe, false);
		startProc("mkfifo " + gpsdPipe, false);
		startProc("gpsd " + gpsdPipe, false);
		//writer for gpsd pipe
		pipeWriter = new BufferedWriter(new FileWriter(gpsdPipe));

	}//initGpsd------------------------------------

	/**
	 * Initialize google earth files.
	 * @return error message, if any.
	 */
	void initGoogleEarth() throws IOException, JAXBException {
		//create file gps.kml if it doesnt exist
		File f = new File("gps.kml");
		if (f.exists()) {
			return;
		}

		kmlUtil.createGpsKml(f);

	}//initGoogleEarth--------------------------

	/**
	 * Display message in a message box on the gui.
	 * All swing calls must be done on the event dispatcher thread as shown.
	 * @param err
	 */
	void showError(final String err) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				JOptionPane.showMessageDialog(null, err, "Alert", JOptionPane.ERROR_MESSAGE);
			}
		});
	}//--------------------------

	/**
	 * Sends given position to various consumers.
	 * @param pos
	 */
	public void send(PositionInfo pos) {
		try {
			updateGui(pos);

			//this will create pos.kml each time with new postion which will be read
			//periodically by google earth
			if (useGoogleEarth) {
				kmlUtil.createPosKml(pos);
			}

			if (useGpsd) {
				sendNmeaReport(pos);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}//send----------------------------

	/**
	 * Update position fields on gui. As usual, this needs to be done
	 * within swing's event dispatcher thread.
	 * @param pos
	 */
	private void updateGui(final PositionInfo pos) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				gui.updatePositionInfo(pos);
			}
		});

	}//-------------------------------

	/**
	 * Start given process.
	 * @param command
	 * @param wait for process to exit
	 * @return
	 */
	int startProc(String command, Boolean wait) throws IOException, InterruptedException {
		String[] commandArray = command.split(" ");
		ProcessBuilder pb = new ProcessBuilder(commandArray);
		pb.redirectErrorStream(true);   //redirect errorstream and outputstream to single stream
		Process proc = pb.start();
		BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line;
		//empty the output buff of the proc
		while ((line = in.readLine()) != null) {
			System.out.println(line);
		}

		if (wait) {
			return proc.waitFor();
		} else {
			return 0;
		}

	}//startProc---------------------------

	/**
	 * Sends NMEA RMC report to linux gps daemon, gpsd via predetermined pipe.
	 * @param pos
	 */
	void sendNmeaReport(PositionInfo pos) throws IOException {
		// an NMEA RMC position sentence (report) is of form:  $GPRMC,124426,A,5920.7019,N,02803.2893,E,,,121212,,

		Calendar cal = Calendar.getInstance();
		Integer hour = cal.get(Calendar.HOUR_OF_DAY);
		Integer minute = cal.get(Calendar.MINUTE);
		Integer second = cal.get(Calendar.SECOND);
		Integer date = cal.get(Calendar.DATE);
		Integer month = cal.get(Calendar.MONTH) + 1;    //java Calendar month starts at 0
		Integer year = cal.get(Calendar.YEAR) % 100;      //convert to 2 digit year

		String sHour = String.format("%02d", hour);
		String sMinute = String.format("%02d", minute);
		String sSecond = String.format("%02d", second);
		String sDate = String.format("%02d", date);
		String sMonth = String.format("%02d", month);
		String sYear = String.format("%02d", year);

		String sentence = null;
		if(pos == null) {
		   sentence = "$GPRMC," + sHour + sMinute + sSecond + ",A,,,,,,," + sDate + sMonth + sYear + ",,";
		} else {
			double lat = pos.getPosition().getLatitude();
			double lon = pos.getPosition().getLongitude();

			String latSuffix = "N";
			if (lat < 0) {
				latSuffix = "S";
			}
			lat = Math.abs(lat);
			String lonSuffix = "E";
			if (lon < 0) {
				lonSuffix = "W";
			}
			lon = Math.abs(lon);

			long latDeg = (long) lat;    //degree part
			double latMin = (lat - latDeg) * 60;    //minute part
			long latMinWhole = (long) latMin;    //whole part of minute
			Double latMinFrac = latMin - latMinWhole;   //fractional part of minute
			String sLatDeg = String.format("%02d", latDeg);
			String sLatMinWhole = String.format("%02d", latMinWhole);
			String sLatMinFrac = latMinFrac.toString().replace("0.", ".");
			long lonDeg = (long) lon;    //degree part
			double lonMin = (lon - lonDeg) * 60;    //minute part
			long lonMinWhole = (long) lonMin;    //whole part of minute
			Double lonMinFrac = lonMin - lonMinWhole;   //fractional part of minute
			String sLonDeg = String.format("%02d", lonDeg);
			String sLonMinWhole = String.format("%02d", lonMinWhole);
			String sLonMinFrac = lonMinFrac.toString().replace("0.", ".");

			sentence = "$GPRMC," + sHour + sMinute + sSecond + ",A," + sLatDeg + sLatMinWhole + sLatMinFrac + "," + latSuffix + "," + sLonDeg + sLonMinWhole + sLonMinFrac + "," + lonSuffix + ",,," + sDate + sMonth + sYear + ",,";
		}//pos not null

		pipeWriter.write(sentence + "\r\n");
		pipeWriter.flush();
		//System.out.println(sentence);

	}//sendNmeaReport-----------------
}
