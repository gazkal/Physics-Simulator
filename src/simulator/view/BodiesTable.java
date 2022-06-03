package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

@SuppressWarnings("serial")
public class BodiesTable extends JPanel {
	BodiesTable(Controller ctrl) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "Bodies",
				TitledBorder.LEFT, TitledBorder.TOP));
		BodiesTableModel tableModel = new BodiesTableModel(ctrl);
		JTable table = new JTable(tableModel);
		this.add(new JScrollPane(table), BorderLayout.PAGE_START);
		table.setFillsViewportHeight(true); 
     	setVisible(true);	
	}
}
