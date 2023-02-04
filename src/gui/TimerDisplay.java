package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import scramblegenerator.CubeScrambler;

//requires java.desktop;

@SuppressWarnings("serial")
public class TimerDisplay extends JFrame {
	private long startTime;
	private long stopTime;
	private long time;
	public static int timerState;

	private JPanel dataPanel;
	private JComboBox<String> sessionPicker;
	// private JPanel statPanel;
	StatPanel statPanel;
	TimeListPanel timeListPanel;
	private JTextField indexDeletionField;
	private JButton deleteButton;
	private JButton deleteAllButton;
	private JPanel mainPanel;
	private TimerPanel timerPanel;
	private JPanel scramblePanel;
	private JLabel scrambleLabel;
	private CubeScrambler scrambler;
	private String scramble;
	ScrambleImage image;

	public TimerDisplay() {
		statPanel = new StatPanel();
		timeListPanel = new TimeListPanel();
		timerPanel = new TimerPanel();
		timerState = 0;
		scrambler = new CubeScrambler(15, 3);
		image = new ScrambleImage();
	}

	private void prepareDataPanel() {
		dataPanel = new JPanel();
		dataPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		sessionPicker = new JComboBox<>();
		sessionPicker.addItem("Default Session");
		sessionPicker.addItem("Create a new session");
		sessionPicker.getInputMap(1).put(KeyStroke.getKeyStroke(32, 0, false), "none");
		sessionPicker.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == 1) {
					String s = sessionPicker.getSelectedItem().toString();
					System.out.println(s);
				}
			}
		});
		// prepareStatPanel();
		indexDeletionField = new JTextField();
		deleteButton = new JButton("Delete time");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocus();
				int index = Integer.parseInt(indexDeletionField.getText());
				indexDeletionField.setText("");
				statPanel.deleteTime(index);
				timeListPanel.updateTimeList(index);
			}
		});
		deleteAllButton = new JButton("Delete all times");
		deleteAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocus();
				statPanel.deleteAllTimes();
				timeListPanel.deleteAllTimes();
			}
		});

		c.fill = 2;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0D;
		c.weighty = 0.0D;
		dataPanel.add(sessionPicker, c);
		c.gridy = 1;
		dataPanel.add(statPanel, c);
		c.gridy = 2;
		dataPanel.add(indexDeletionField, c);
		c.gridy = 3;
		dataPanel.add(deleteButton, c);
		c.gridy = 4;
		dataPanel.add(deleteAllButton, c);
		c.fill = 1;
		c.gridy = 5;
		c.weighty = 1.0D;
		dataPanel.add(timeListPanel, c);
	}

	private void prepareMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBackground(Color.BLACK);

		scramblePanel = new JPanel();
		//scramblePanel.setLayout(new GridBagLayout());
		scramblePanel.setBackground(Color.RED);
		scramble = scrambler.generateScramble();
		scrambleLabel = new JLabel(scramble);
		scrambleLabel.setFont(new Font("Arial", 0, 15));
		scrambleLabel.setForeground(Color.WHITE);
		scramblePanel.add(scrambleLabel);

		mainPanel.add(scramblePanel);
		mainPanel.add(timerPanel);
	}

	public void prepareGUI() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		setDefaultCloseOperation(3);
		setTitle("Cube Timer");
		setSize(900, 600);
		addKeyListener(new Listeners());
		prepareDataPanel();
		prepareMainPanel();
		c.fill = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5D;
		c.weighty = 0.5D;
		add(dataPanel, c);
		c.fill = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.5D;
		c.weighty = 0.5D;
		add(mainPanel, c);
		dataPanel.setVisible(true);
		statPanel.setVisible(true);
		timeListPanel.setVisible(true);
		mainPanel.setVisible(true);
		scramblePanel.setVisible(true);
		setVisible(true);
		requestFocus();
	}

	public void createSession() {
	}

	class Listeners implements KeyListener {
		public String convertToString(long time) {
			String s;
			long milliseconds = time % 1000L;
			time /= 1000L;
			long seconds = time % 60L;
			time /= 60L;
			if (time == 0L) {
				s = String.valueOf(seconds) + "." + String.format("%03d", new Object[] { Long.valueOf(milliseconds) });
			} else {
				s = String.valueOf(time) + ":" + String.format("%02d", new Object[] { Long.valueOf(seconds) }) + "."
						+ String.format("%03d", new Object[] { Long.valueOf(milliseconds) });
			}
			return s;
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == 32)
				if (timerState == 1) {
					timerPanel.startHoldTimer();
				} else if (timerState == 3) {
					stopTime = System.currentTimeMillis();
					timerState = 4;
					time = stopTime - startTime;
					String solve = convertToString(time);
					timerPanel.stopTimer(solve);
					statPanel.addTime(time);
					timeListPanel.addToTimeList(statPanel.session.getCount(), solve, scramble);
					scramble = scrambler.generateScramble();
					scrambleLabel.setText(scramble);
				}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == 32)
				if (timerState == 0) {
					timerState = 1;
					timerPanel.startObservation();
				} else if (timerState == 1) {
					timerPanel.cancelHoldTimer();
				} else if (timerState == 2) {
					startTime = System.currentTimeMillis();
					timerState = 3;
					timerPanel.startTimer();
				} else if (timerState == 4) {
					timerState = 0;
				}
		}
	}
}