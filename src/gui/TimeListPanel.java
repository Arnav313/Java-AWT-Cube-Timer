package gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class TimeListPanel extends JPanel {
	private JTable timeList;
	
	public TimeListPanel() {
		setBackground(Color.BLUE);
		setLayout(new BorderLayout());
		timeList = new JTable(new DefaultTableModel(new Object[] { "No.", "Time", "Scramble" }, 0));
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(0);
		timeList.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
		timeList.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
		timeList.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
		timeList.setAutoResizeMode(3);
		add(new JScrollPane(timeList), "Center");
	}
	
	public void addToTimeList(int count, String solve, String scramble) {
		DefaultTableModel model = (DefaultTableModel) timeList.getModel();
		model.insertRow(0, new Object[] { String.valueOf(count), solve, scramble });
	}
	
	public void updateTimeList(int index) {
		DefaultTableModel model = (DefaultTableModel) timeList.getModel();
		int rowCount = model.getRowCount();
		model.removeRow(rowCount-- - index);
		for (int i = 0; i <= rowCount - index; i++)
			model.setValueAt(Integer.valueOf(rowCount - i), i, 0);
	}
	
	public void deleteAllTimes() {
		DefaultTableModel model = (DefaultTableModel) timeList.getModel();
		model.setRowCount(0);
	}

}
