package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;

	private  String[] columnNames = {
			"Id",
        	"Mass",
        	"Position",
        	"Velocity",
        	"Acceleration"};

	private List<Body> _bodies;

	BodiesTableModel(Controller ctrl) {
		_bodies = new ArrayList<>();
		ctrl.addObserver(this);
	}

	@Override
	public int getRowCount() {
		return _bodies.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Body b = _bodies.get(rowIndex);
		
		if (columnIndex == 0)	return b.getId();
     	if (columnIndex == 1) 	return b.getMass();
     	if (columnIndex == 2)     	return b.getPos();
     	if (columnIndex == 3)       	return b.getVel().magnitude();
     	if (columnIndex == 4)       	return b.getAcc().magnitude();
     	return null;

	}
	@Override
	public String getColumnName(int column) {
	        return columnNames[column];
	    }
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				bodies.forEach(body->{
					_bodies.add(body);
				});
				fireTableStructureChanged();
			}
		});

		
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				_bodies.clear();
				fireTableStructureChanged();
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
				fireTableStructureChanged();
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
				fireTableStructureChanged();
			}
		});

		
	}

	private void updateModelBody(Body b) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				boolean notfound=true;
				int i=0;
				while(i < _bodies.size()&& notfound) {
					if(_bodies.get(i).equals(b)) {
						b.copyToBody(_bodies.get(i));
					}
					i++;
				}
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
