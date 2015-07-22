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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import frk.gpssimulator.model.PositionInfo;

/**
 *
 * @author faram
 */
public class Gui extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;

	private GpsSimulator sim;
	private Thread simThread;
	//------------------
	File currentDir = new File(".");    //used by path kml file chooser dialog box
	Boolean shouldMove = false;         //move vehicle

	/** Creates new form Gui */
	public Gui() {
		try {
			//set native look n feel for the widgets rather than default java look n feel.
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception uie) {
			uie.printStackTrace();
		}
		initComponents();

		// the aboutTextArea in the about tab has a scrollPane around it. the scrollPane
		//has a hidden child, viewport which is not accessible in the gui designer.
		//its transparency, border needs to be set programatically here otherwise it
		//shows up on the linux port of the java jre.
		aboutScrollPane.getViewport().setOpaque(false);
		aboutScrollPane.setViewportBorder(null);
		//String pwd = System.getProperty("user.dir");
		//pathKmlTextField.setText(pwd);

	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jTextField1 = new javax.swing.JTextField();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		simPanel = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		pathKmlTextField = new javax.swing.JTextField();
		browseButton = new javax.swing.JButton();
		jPanel5 = new javax.swing.JPanel();
		headingTextField = new javax.swing.JTextField();
		legTextField = new javax.swing.JTextField();
		latTextField = new javax.swing.JTextField();
		lonTextField = new javax.swing.JTextField();
		jLabel5 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		speedSpinner = new javax.swing.JSpinner();
		startButton = new javax.swing.JButton();
		serialPanel = new javax.swing.JPanel();
		jPanel6 = new javax.swing.JPanel();
		jLabel6 = new javax.swing.JLabel();
		serialPortComboBox = new javax.swing.JComboBox();
		aboutPanel = new javax.swing.JPanel();
		aboutScrollPane = new javax.swing.JScrollPane();
		aboutTextArea = new javax.swing.JTextArea();

		jTextField1.setText("jTextField1");

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("GPS Vehicle Simulator");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		jLabel2.setText("Path KML");

		pathKmlTextField.setEditable(false);
		pathKmlTextField.setToolTipText("KML file contains path on which simulated vechicle will travel at given speed.");
		pathKmlTextField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pathKmlTextFieldActionPerformed(evt);
			}
		});

		browseButton.setText("Browse");
		browseButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				browseButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(
			jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel4Layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jLabel2)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(pathKmlTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(browseButton)
				.addContainerGap())
		);
		jPanel4Layout.setVerticalGroup(
			jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel4Layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel2)
					.addComponent(pathKmlTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(browseButton))
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		headingTextField.setEditable(false);
		headingTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		legTextField.setEditable(false);
		legTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		latTextField.setEditable(false);
		latTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		latTextField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				latTextFieldActionPerformed(evt);
			}
		});

		lonTextField.setEditable(false);
		lonTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

		jLabel5.setText("Heading");

		jLabel7.setText("Leg");

		jLabel8.setText("Latitude");

		jLabel9.setText("Longitude");

		javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout.setHorizontalGroup(
			jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel5Layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(jLabel8)
					.addComponent(jLabel9))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
					.addComponent(latTextField)
					.addComponent(lonTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
				.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(jLabel5)
					.addComponent(jLabel7))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
					.addComponent(legTextField)
					.addComponent(headingTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
				.addContainerGap())
		);
		jPanel5Layout.setVerticalGroup(
			jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel5Layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(headingTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(latTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(jLabel8)
					.addComponent(jLabel5))
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(legTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(lonTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(jLabel9)
					.addComponent(jLabel7))
				.addContainerGap())
		);

		jLabel1.setText("Speed (kph)");

		speedSpinner.setModel(new javax.swing.SpinnerNumberModel(36, 1, 999, 1));
		speedSpinner.setToolTipText("Speed at which simulated vehicle will travel on given path.");
		speedSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				speedSpinnerStateChanged(evt);
			}
		});

		startButton.setText("Start vehicle");
		startButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				startButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(
			jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel3Layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jLabel1)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(speedSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
				.addComponent(startButton)
				.addContainerGap())
		);
		jPanel3Layout.setVerticalGroup(
			jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel3Layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel1)
					.addComponent(speedSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
					.addComponent(startButton))
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		javax.swing.GroupLayout simPanelLayout = new javax.swing.GroupLayout(simPanel);
		simPanel.setLayout(simPanelLayout);
		simPanelLayout.setHorizontalGroup(
			simPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(simPanelLayout.createSequentialGroup()
				.addContainerGap()
				.addGroup(simPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(simPanelLayout.createSequentialGroup()
						.addGroup(simPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
							.addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap(14, Short.MAX_VALUE))
					.addGroup(simPanelLayout.createSequentialGroup()
						.addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(14, 14, 14))))
		);
		simPanelLayout.setVerticalGroup(
			simPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(simPanelLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
				.addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		jTabbedPane1.addTab("Simulator", simPanel);

		jLabel6.setText("Serial port");

		serialPortComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None" }));

		javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
		jPanel6.setLayout(jPanel6Layout);
		jPanel6Layout.setHorizontalGroup(
			jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel6Layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jLabel6)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addComponent(serialPortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(161, Short.MAX_VALUE))
		);
		jPanel6Layout.setVerticalGroup(
			jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(jPanel6Layout.createSequentialGroup()
				.addContainerGap()
				.addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
					.addComponent(jLabel6)
					.addComponent(serialPortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		javax.swing.GroupLayout serialPanelLayout = new javax.swing.GroupLayout(serialPanel);
		serialPanel.setLayout(serialPanelLayout);
		serialPanelLayout.setHorizontalGroup(
			serialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(serialPanelLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addContainerGap())
		);
		serialPanelLayout.setVerticalGroup(
			serialPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(serialPanelLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
				.addContainerGap(142, Short.MAX_VALUE))
		);

		jTabbedPane1.addTab("Serial port", null, serialPanel, "Serial port settings");

		aboutScrollPane.setBorder(null);
		aboutScrollPane.setOpaque(false);

		aboutTextArea.setColumns(20);
		aboutTextArea.setEditable(false);
		aboutTextArea.setRows(5);
		aboutTextArea.setText("GPS Vehicle Simulator\nver. 1.0\n05 Feb 2011\nby Faram Khambatta\nhttp://faram-website.appspot.com");
		aboutTextArea.setBorder(null);
		aboutTextArea.setFocusable(false);
		aboutTextArea.setOpaque(false);
		aboutScrollPane.setViewportView(aboutTextArea);

		javax.swing.GroupLayout aboutPanelLayout = new javax.swing.GroupLayout(aboutPanel);
		aboutPanel.setLayout(aboutPanelLayout);
		aboutPanelLayout.setHorizontalGroup(
			aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(aboutPanelLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(aboutScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
				.addContainerGap())
		);
		aboutPanelLayout.setVerticalGroup(
			aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(aboutPanelLayout.createSequentialGroup()
				.addContainerGap()
				.addComponent(aboutScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
				.addContainerGap())
		);

		jTabbedPane1.addTab("About", aboutPanel);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
				.addContainerGap())
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
			.addGroup(layout.createSequentialGroup()
				.addContainerGap()
				.addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
		// TODO add your handling code here:
		//JOptionPane.showMessageDialog(null,"alert", "alert", JOptionPane.ERROR_MESSAGE);
		shouldMove = !shouldMove;
		sim.setShouldMove(shouldMove);
		if (shouldMove) {
			startButton.setText("Stop vehicle");
		} else {
			startButton.setText("Start vehicle");
		}
}//GEN-LAST:event_startButtonActionPerformed

	private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
		//open a file chooser dialog box to allow user to select path kml
		JFileChooser fc = new JFileChooser(currentDir);
		FileFilter ft = new FileNameExtensionFilter("KML file", "kml");
		fc.addChoosableFileFilter(ft);
		int ret = fc.showOpenDialog(null);
		//once user selects file...
		if (ret == JFileChooser.APPROVE_OPTION) {
			currentDir = fc.getCurrentDirectory();
			File f = fc.getSelectedFile();
			pathKmlTextField.setText(f.getAbsolutePath());

			//stop the vehicle
			shouldMove = false;
			startButton.setText("Start vehicle");
			sim.setShouldMove(shouldMove);

			//update sim speed
			Integer speedKph = (Integer) speedSpinner.getValue();
			Double speed = speedKph * 1000.0 / 3600.0;  // metres/sec
			sim.setSpeed(speed);
			//load path kml into simulator
			try {
				sim.loadPathKml(f);
			} catch (Exception lpke) {
				lpke.printStackTrace();
				JOptionPane.showMessageDialog(null, lpke.toString(), "Alert", JOptionPane.ERROR_MESSAGE);
			}
		}
}//GEN-LAST:event_browseButtonActionPerformed

	private void pathKmlTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pathKmlTextFieldActionPerformed
		// TODO add your handling code here:
}//GEN-LAST:event_pathKmlTextFieldActionPerformed

	private void latTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_latTextFieldActionPerformed
		// TODO add your handling code here:
	}//GEN-LAST:event_latTextFieldActionPerformed

	private void speedSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_speedSpinnerStateChanged
		// convert to m/s and update sim speed
		Integer speedKph = (Integer) speedSpinner.getValue();
		Double speed = speedKph * 1000.0 / 3600.0;  // metres/sec
		sim.setSpeed(speed);
	}//GEN-LAST:event_speedSpinnerStateChanged

	private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
		// interrupt sim thread to allow it to close gracefully
		try {
			simThread.interrupt();
			simThread.join(1000);   // wait max 1 sec for tread to die
		} catch(Exception e) {
			e.printStackTrace();
		}
	}//GEN-LAST:event_formWindowClosing

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel aboutPanel;
	private javax.swing.JScrollPane aboutScrollPane;
	private javax.swing.JTextArea aboutTextArea;
	private javax.swing.JButton browseButton;
	private javax.swing.JTextField headingTextField;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField latTextField;
	private javax.swing.JTextField legTextField;
	private javax.swing.JTextField lonTextField;
	private javax.swing.JTextField pathKmlTextField;
	private javax.swing.JPanel serialPanel;
	private javax.swing.JComboBox serialPortComboBox;
	private javax.swing.JPanel simPanel;
	private javax.swing.JSpinner speedSpinner;
	private javax.swing.JButton startButton;
	// End of variables declaration//GEN-END:variables

	/**
	 * @return the sim
	 */
	public GpsSimulator getSim() {
		return sim;
	}

	/**
	 * @param sim the sim to set
	 */
	public void setSim(GpsSimulator sim) {
		this.sim = sim;
	}

	/**
	 * Updates position fields based on current position sent by dispatcher.
	 * @param posInfo
	 */
	public void updatePositionInfo(PositionInfo posInfo) {
		if (posInfo == null) {
			latTextField.setText("---");
			lonTextField.setText("---");
			headingTextField.setText("---");
			legTextField.setText("---");
			return;
		}

		Double latitude = posInfo.getPosition().getLatitude();
		latTextField.setText(String.format("%.6f", latitude));

		Double longitude = posInfo.getPosition().getLongitude();
		lonTextField.setText(String.format("%.6f", longitude));

		Double heading = posInfo.getLeg().getHeading();
		headingTextField.setText(String.format("%.0f", heading));

		legTextField.setText(posInfo.getLeg().getId().toString());
	}//------------------------------

	/**
	 * @return the simThread
	 */
	public Thread getSimThread() {
		return simThread;
	}

	/**
	 * @param simThread the simThread to set
	 */
	public void setSimThread(Thread simThread) {
		this.simThread = simThread;
	}





}
