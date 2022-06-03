package simulator.view;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {

	Controller _ctrl;

	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		_ctrl = ctrl;
		initGUI();
	}

	private void initGUI() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		setContentPane(mainPanel);
		
		mainPanel.add(new ControlPanel(_ctrl), BorderLayout.PAGE_START);
		mainPanel.add(new StatusBar(_ctrl), BorderLayout.PAGE_END);
		JPanel subPanel = new JPanel();
		subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
		mainPanel.add(subPanel, BorderLayout.CENTER);
		subPanel.add(new BodiesTable(_ctrl));
		subPanel.add(new Viewer(_ctrl));
		mainPanel.add(subPanel);
		
		this.pack();
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
	}

}

