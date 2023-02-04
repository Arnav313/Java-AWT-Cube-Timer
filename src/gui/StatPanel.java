package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import times.Statistics;

@SuppressWarnings("serial")
public class StatPanel extends JPanel {
	private JLabel[] statList;
	Statistics session;
	
	public StatPanel() {
		session = new Statistics();
		
		setBackground(Color.BLUE);
		setLayout(new GridLayout(4, 2));
		String[] s = session.getStats();
		statList = new JLabel[7];
		int i;
		for (i = 0; i < 4; i++) {
			statList[i] = new JLabel(s[i]);
			statList[i].setFont(new Font("Arial", 1, 10));
			statList[i].setForeground(Color.WHITE);
			add(statList[i]);
		}
		for (i = 4; i < 7; i++) {
			statList[i] = new JLabel(s[i]);
			statList[i].setFont(new Font("Arial", 1, 10));
			statList[i].setForeground(Color.WHITE);
			add(statList[i]);
		}
	}
	
	public void addTime(long time) {
		session.addTime(time);
		updateStats();
	}
	
	public void deleteTime(int index) {
		session.deleteTime(index);
		updateStats();
	}
	
	private void updateStats() {
		String[] s = session.getStats();
		for (int i = 0; i < 7; i++)
			statList[i].setText(s[i]);
	}
	
	public void deleteAllTimes() {
		session = new Statistics();
		updateStats();
	}

}
