package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver, ActionListener {

	private static final long serialVersionUID = 1L;
	private JButton load, gLaws, play, stop, turnOff;
	private JTextField deltaTimeTextBox;
	private JSpinner stepTimeSpinner, delaySpinner;
	private Controller _ctrl;
	private volatile Thread _thread;
	private static final String iconPath = "resources/icons/";

	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		initGUI();
		_ctrl.addObserver(this);
	}

	private void initGUI() {
		
		this.setLayout(new BorderLayout());
		
		JToolBar toolBar = new JToolBar();

		load = new JButton();
		load.setActionCommand("LOAD");
		load.setIcon(new ImageIcon(iconPath.concat("open.png")));
		load.setToolTipText("Loads bodies from a JSON file");
		load.addActionListener(this);
		toolBar.add(load);
		
		toolBar.addSeparator();

		gLaws = new JButton();
		gLaws.setActionCommand("PHYSICS");
		gLaws.setIcon(new ImageIcon(iconPath.concat("physics.png")));
		gLaws.setToolTipText("Changes GravityLaws to be applied");
		gLaws.addActionListener(this);
		toolBar.add(gLaws);
		
		toolBar.addSeparator();

		play = new JButton();
		play.setActionCommand("RUN");
		play.setIcon(new ImageIcon(iconPath.concat("run.png")));
		play.setToolTipText("Runs simulation for the number of steps declared");
		play.addActionListener(this);
		toolBar.add(play);
		
		stop = new JButton();
		stop.setActionCommand("STOP");
		stop.setIcon(new ImageIcon(iconPath.concat("stop.png")));
		stop.setToolTipText("Stops simulation");
		stop.addActionListener(this);
		toolBar.add(stop);
		
		JLabel delayLbl = new JLabel("Delay:");
		delayLbl.setBorder(new EmptyBorder(5, 5, 5, 5));
		toolBar.add(delayLbl);
			
		Long val = new Long(0);
		Long min = new Long(0);
		Long max = new Long(1000);
		Long step = new Long(1);
		delaySpinner = new JSpinner(new SpinnerNumberModel(val,min, max, step));
		delaySpinner.setMinimumSize(new Dimension(80, 30));
		delaySpinner.setPreferredSize(new Dimension(80, 30));
		delaySpinner.setMaximumSize(new Dimension(80, 30));
		toolBar.add(delaySpinner);
		
		JLabel stepsLbl = new JLabel("Steps:");
		stepsLbl.setBorder(new EmptyBorder(5, 5, 5, 5));
		toolBar.add(stepsLbl);
		
		stepTimeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
		stepTimeSpinner.setMinimumSize(new Dimension(80, 30));
		stepTimeSpinner.setPreferredSize(new Dimension(80, 30));
		stepTimeSpinner.setMaximumSize(new Dimension(80, 30));
		toolBar.add(stepTimeSpinner);
		
		JLabel deltaTimeLbl = new JLabel("Delta-Time:");
		deltaTimeLbl.setBorder(new EmptyBorder(5, 5, 5, 5));
		toolBar.add(deltaTimeLbl);
		
		deltaTimeTextBox = new JTextField();
		deltaTimeTextBox.setMinimumSize(new Dimension(80, 30));
		deltaTimeTextBox.setPreferredSize(new Dimension(80, 30));
		deltaTimeTextBox.setMaximumSize(new Dimension(80, 30));
		toolBar.add(deltaTimeTextBox);
		

		toolBar.addSeparator();
		
		toolBar.add(Box.createHorizontalGlue());
		turnOff = new JButton();
		turnOff.setActionCommand("EXIT");
		turnOff.setIcon(new ImageIcon(iconPath.concat("exit.png")));
		turnOff.setToolTipText("Exits program");
		turnOff.addActionListener(this);
		toolBar.add(turnOff);

		this.add(toolBar );
		this.setVisible(true);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("LOAD")) {
			loadCommand();
		} else if (e.getActionCommand().equals("PHYSICS")) {
			physicsCommand();
		} else if (e.getActionCommand().equals("RUN")) {
				runCommand();
		} else if (e.getActionCommand().equals("STOP")) {
			stopCommand();
		} else {
			exitCommand();
		}

	}

	private void loadCommand() {
		JFileChooser fc = new JFileChooser();
		File workingDirectory = new File(System.getProperty("user.dir"));
		fc.setCurrentDirectory(workingDirectory);
		int v = fc.showOpenDialog(null);
		try {
			if (v == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile(); // obtenemos el fichero seleccionado
				_ctrl.reset();
				_ctrl.loadBodies(file);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}


	}

	private void physicsCommand() {
		List<JSONObject> arrayJSON = _ctrl.getGravityLawsFactory().getInfo();
		String[] optionArray = new String[arrayJSON.size()];
		int i=0;
		for(JSONObject glJSON : arrayJSON) {
			optionArray[i] = glJSON.getString("desc");
			i++;
		}
		
		String seleccion =  (String) JOptionPane.showInputDialog(
				null, 
				"Select gravity laws to be used:",
				"Gravity Laws Selector",
				JOptionPane.PLAIN_MESSAGE,
				null, 
				optionArray,
				null) // opciones																				// vacío
				;
		int k=0;
		
		if (seleccion != null) {
			while (!arrayJSON.get(k).getString("desc").equals(seleccion))
				k++;
			
			try {
				_ctrl.setGravityLaws(arrayJSON.get(k));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void runCommand(){
		load.setEnabled(false);
		gLaws.setEnabled(false);
		play.setEnabled(false);
		turnOff.setEnabled(false);
		try {
			_ctrl.setDeltaTime(Double.parseDouble(deltaTimeTextBox.getText()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			load.setEnabled(true);
			gLaws.setEnabled(true);
			play.setEnabled(true);
			turnOff.setEnabled(true);
			return;
		}
		if(_ctrl.bodyListisEmpty()) {
			JOptionPane.showMessageDialog(null, "No bodies loaded in simulator", "Error",
					JOptionPane.ERROR_MESSAGE);
			load.setEnabled(true);
			gLaws.setEnabled(true);
			play.setEnabled(true);
			turnOff.setEnabled(true);
			return;
		}		
		_thread = new Thread(new Runnable() {
			public void run() {
				run_sim((Integer) stepTimeSpinner.getValue(), (Long) delaySpinner.getValue());
				load.setEnabled(true);
				gLaws.setEnabled(true);
				play.setEnabled(true);
				turnOff.setEnabled(true);
				_thread = null;
			}
		});
		_thread.start();
	}

	private void stopCommand() {
		if(_thread != null) {
			_thread.interrupt();
		}
	}

	private void exitCommand() {
		Object[] options = { "Yes", "No" };
		int n = JOptionPane.showOptionDialog(null, "Exit the program?", "Confirmation", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null, options, options[1]);
		if (n == JOptionPane.YES_OPTION)
			System.exit(0);
	}

	private void run_sim(int n, long delay) {
		/*while ( n>0 && (the current thread has not been intereptred) ) {
			// 1. execute the simulator one step, i.e., call method
			// _ctrl.run(1) and handle exceptions if any*/
		while ( n>0 && !_thread.isInterrupted() ) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Unexpected error during simulation.",
						JOptionPane.ERROR_MESSAGE);
				load.setEnabled(true);
				gLaws.setEnabled(true);
				play.setEnabled(true);
				turnOff.setEnabled(true);
				return;
			}
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				_thread.interrupt();
			}
			n--;
			}
	}

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				deltaTimeTextBox.setText(Double.toString(dt));
			}
		});



	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				deltaTimeTextBox.setText(Double.toString(dt));
			}
		});

	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b){
		// TODO Auto-generated method stub

	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				deltaTimeTextBox.setText(Double.toString(dt));
			}
		});

	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub

	}

}
