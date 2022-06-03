package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;
import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

@SuppressWarnings("serial")
public class Viewer extends JComponent implements SimulatorObserver{
	//...
	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;
	
	Viewer (Controller ctrl)
	{
		initGUI();
		ctrl.addObserver(this);
	}
	
	private void initGUI()
	{
		
		_bodies = new ArrayList<>();
		_scale = 1.0;
		_showHelp = true;
		
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "Viewer",
				TitledBorder.LEFT, TitledBorder.TOP));
		this.setMinimumSize(new Dimension(600, 400));
		this.setPreferredSize(new Dimension(600, 400));
		
		
		addKeyListener(new KeyListener() {
			//...
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '-':
					_scale = _scale * 1.1;
					break;
				case '+': _scale = Math.max(1000.0, _scale / 1.1);
					break;
				case '=':
					autoScale();
					break;
				case 'h':
					_showHelp = !_showHelp;
					break;
				default:
				}
				repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		addMouseListener(new MouseListener() {
			//...
			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	@Override
	protected void paintComponent (Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// use ’gr’ to draw not ’g’
		// calculate the center
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;

		gr.setColor(Color.RED);
		gr.drawLine(_centerX + 5, _centerY,_centerX - 5, _centerY);
		gr.drawLine(_centerX, _centerY + 5, _centerX, _centerY - 5);

			
		for (int i = 0; i < _bodies.size(); i++)
		{
			gr.setColor(Color.BLUE);	
			Vector v = new Vector(_bodies.get(i).getPos());
			gr.fillOval(_centerX + (int) (v.coordinate(0)/_scale), _centerY - (int) (v.coordinate(1)/_scale), 
					10, 10);
			gr.setColor(Color.BLACK);
			gr.drawString(_bodies.get(i).getId(), _centerX + (int) (v.coordinate(0)/_scale), _centerY - (int) (v.coordinate(1)/_scale));
		}

		if (_showHelp) {
			gr.setColor(Color.RED);
			gr.drawString("h: Toggle help, +: zoom-in, -: zoom_out, =: fit", 10, 30);
			gr.drawString(" Scaling ratio: " + _scale, 10, 45);
		}
	}
	
	// other private/protected methods
	private void autoScale() {
		double max = 1.0;
		for (Body b : _bodies) {
			Vector p = b.getPos();
			for (int i = 0; i < p.dim(); i++)
				max = Math.max(max,
						Math.abs(b.getPos().coordinate(i)));
		}
		
		double size = Math.max(1.0, Math.min((double) getWidth(), (double) getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}
	
	private void updateModelBody(Body b) {
		boolean notfound=true;
		int i=0;
		while(i < _bodies.size()&& notfound) {
			if(_bodies.get(i).equals(b)) {
				b.copyToBody(_bodies.get(i));
				notfound=false;
			}
			i++;
		}
	}
	
	
	// SimulatorObserver methods
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				bodies.forEach(body->{
					_bodies.add(body);
				});
				autoScale();
				repaint();
			}
		});

	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_bodies.clear();
				autoScale();
				repaint();
			}
		});

	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if(!_bodies.contains(b))
					_bodies.add(b);
				autoScale();
				repaint();
			}
		});

	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				bodies.forEach(body->{
					updateModelBody(body);
				});
				repaint();
			}
		});
	}
	
	

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub
		
	}

}
