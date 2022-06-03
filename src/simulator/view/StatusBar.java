package simulator.view;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

@SuppressWarnings("serial")
public class StatusBar extends JPanel implements SimulatorObserver{
	// ...
	private JLabel _currTime; // for current time
	private JLabel _currLaws; // for gravity laws
	private JLabel _numOfBodies; // for number of bodies
	
	StatusBar(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}
	private void initGUI() {
		this.setLayout( new FlowLayout( FlowLayout.LEFT ));
		this.setBorder( BorderFactory.createBevelBorder( 1 ));
		_currTime = new JLabel("Time: ");
		_currLaws = new JLabel("Laws: ");
		_numOfBodies = new JLabel("Bodies: ");
		this.add(_currTime);
		this.add(new JSeparator());
		this.add(_numOfBodies);
		this.add(new JSeparator());
		this.add(_currLaws);			
	}
	
	// SimulatorObserver methods
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				_currTime.setText("Time: " + time);
				_numOfBodies.setText("Bodies: " + bodies.size());
				_currLaws.setText("Laws: " + gLawsDesc);
			}
		});

	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				_currTime.setText("Time: " + time);
				_numOfBodies.setText("Bodies: " + bodies.size());
				_currLaws.setText("Laws: " + gLawsDesc);
			}
		});
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				_numOfBodies.setText("Bodies: " + bodies.size());
			}
		});
	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				_currTime.setText("Time: " + time);
			}
		});
	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				_currTime.setText("Time: " + dt);
			}
		});
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				_currLaws.setText("Laws: " + gLawsDesc);
			}
		});
	}

}
