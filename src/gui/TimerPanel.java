package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class TimerPanel extends JPanel {
	private int milliseconds;
	private int seconds;
	private int observationTime;
	private boolean timerHeld;
	JLabel digitalTimer;
	
	public TimerPanel() {
		setBackground(Color.BLACK);
		
		digitalTimer = new JLabel("0:00.000");
		digitalTimer.setFont(new Font("Serif", 1, 80));
		digitalTimer.setForeground(Color.WHITE);
		digitalTimer.setHorizontalAlignment(0);
		
		add(digitalTimer);
	}

	private Timer observationTimer = new Timer(1000, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (observationTime == 1) {
				digitalTimer.setText("+2");
				TimerDisplay.timerState = 0;
				observationTimer.stop();
			} else {
				digitalTimer.setText(Integer.toString(--observationTime));
			}
		}
	});

	private Timer holdTimer = new Timer(550, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if (timerHeld) {
				TimerDisplay.timerState = 2;
				digitalTimer.setForeground(new Color(15, 134, 15));
				holdTimer.stop();
			}
		}
	});

	private Timer stopwatchTimer = new Timer(200, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			milliseconds += 2;
			if (milliseconds == 10) {
				milliseconds = 0;
				seconds++;
			}

			digitalTimer.setText(String.valueOf(seconds) + "." + milliseconds);
		}
	});
	
	public void startObservation() {
		setBackground(Color.GREEN);
		digitalTimer.setForeground(Color.WHITE);
		digitalTimer.setText("15");
		observationTime = 15;
		observationTimer.start();
	}
	
	public void startHoldTimer() {
		digitalTimer.setForeground(Color.RED);
		timerHeld = true;
		holdTimer.start();
	}
	
	public void cancelHoldTimer() {
		holdTimer.stop();
		digitalTimer.setForeground(Color.WHITE);
		timerHeld = false;
	}

	public void startTimer() {
		observationTimer.stop();
		setBackground(Color.BLACK);
		digitalTimer.setForeground(Color.WHITE);
		digitalTimer.setText("0.0");
		seconds = milliseconds = 0;
		stopwatchTimer.start();
	}
	
	public void stopTimer(String solve) {
		stopwatchTimer.stop();
		digitalTimer.setText(solve);
	}

}