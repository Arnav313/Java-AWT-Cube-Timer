package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import scramblegenerator.CubeScrambler;
import times.Statistics;

//requires java.desktop;

public class TimerDisplay extends JFrame {
  private static final long serialVersionUID = 280542556734373319L;
  
  private int milliseconds;
  
  private int seconds;
  
  private long startTime;
  
  private long time;
  
  private int timerState;
  
  private int observationTime;
  
  private boolean timerHeld;
  
  private JPanel dataPanel;
  
  private JComboBox<String> sessionPicker;
  
  private JPanel statPanel;
  
  private JLabel[] statList;
  
  private JTextField indexDeletionField;
  
  private JButton deleteButton;
  
  private JButton deleteAllButton;
  
  private JPanel timeListPanel;
  
  private JTable timeList;
  
  private JPanel mainPanel;
  
  private JLabel digitalTimer;
  
  private JPanel scramblePanel;
  
  private JLabel scrambleLabel;
  
  private CubeScrambler scrambler;
  
  private String scramble;
  
  private Statistics session;
  
  private Timer observationTimer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (observationTime == 1) {
            digitalTimer.setText("+2");
            timerState = 0;
            observationTimer.stop();
          } else {
            digitalTimer.setText(Integer.toString(--observationTime));
          } 
        }
      });
  
  private Timer holdTimer = new Timer(550, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (timerHeld) {
            digitalTimer.setForeground(new Color(15, 134, 15));
            timerState = 2;
            holdTimer.stop();
          } 
        }
      });
  
  private Timer stopwatchTimer = new Timer(100, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          milliseconds++;
          if (milliseconds == 10) {
            milliseconds = 0;
            seconds++;
          } 
          String currTime = String.valueOf(seconds) + "." + milliseconds;
          digitalTimer.setText(currTime);
        }
      });
  
  public TimerDisplay() {
    timerHeld = false;
    timerState = 0;
    observationTime = 15;
    session = new Statistics();
    scrambler = new CubeScrambler(15, 3);
  }
  
  private void prepareStatPanel() {
    statPanel = new JPanel();
    statPanel.setBackground(Color.BLUE);
    statPanel.setLayout(new GridLayout(4, 2));
    String[] s = session.getStats();
    statList = new JLabel[7];
    int i;
    for (i = 0; i < 4; i++) {
      statList[i] = new JLabel(s[i]);
      statList[i].setFont(new Font("Arial", 1, 10));
      statList[i].setForeground(Color.WHITE);
      statPanel.add(statList[i]);
    } 
    for (i = 4; i < 7; i++) {
      statList[i] = new JLabel(s[i]);
      statList[i].setFont(new Font("Arial", 1, 10));
      statList[i].setForeground(Color.WHITE);
      statPanel.add(statList[i]);
    } 
  }
  
  private void prepareTimeListPanel() {
    timeListPanel = new JPanel();
    timeListPanel.setBackground(Color.BLUE);
    timeListPanel.setLayout(new BorderLayout());
    timeList = new JTable(new DefaultTableModel(new Object[] { "No.", "Time", "Scramble" }, 0));
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
    cellRenderer.setHorizontalAlignment(0);
    timeList.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
    timeList.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
    timeList.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
    timeList.setAutoResizeMode(3);
    timeListPanel.add(new JScrollPane(timeList), "Center");
  }
  
  private void prepareDataPanel() {
    dataPanel = new JPanel();
    dataPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    sessionPicker = new JComboBox<>();
    sessionPicker.addItem("Default Session");
    sessionPicker.addItem("Create a new session");
    sessionPicker.getInputMap(1)
      .put(KeyStroke.getKeyStroke(32, 0, false), "none");
    sessionPicker.addItemListener(new ItemListener() {
          public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
              String s = sessionPicker.getSelectedItem().toString();
              System.out.println(s);
            } 
          }
        });
    prepareStatPanel();
    indexDeletionField = new JTextField();
    deleteButton = new JButton("Delete time");
    deleteButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            requestFocus();
            int index = Integer.parseInt(indexDeletionField.getText());
            indexDeletionField.setText("");
            session.deleteTime(index);
            updateStats();
            updateTimeList(index);
          }
        });
    deleteAllButton = new JButton("Delete all times");
    deleteAllButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            requestFocus();
            DefaultTableModel model = (DefaultTableModel)timeList.getModel();
            model.setRowCount(0);
            session = new Statistics();
            updateStats();
          }
        });
    prepareTimeListPanel();
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
    scramblePanel.setLayout(new GridBagLayout());
    scramblePanel.setBackground(Color.RED);
    scramble = scrambler.generateScramble();
    scrambleLabel = new JLabel(scramble);
    scrambleLabel.setFont(new Font("Arial", 0, 15));
    scrambleLabel.setForeground(Color.WHITE);
    scramblePanel.add(scrambleLabel);
    digitalTimer = new JLabel("0:00.000");
    digitalTimer.setFont(new Font("Serif", 1, 80));
    digitalTimer.setForeground(Color.WHITE);
    digitalTimer.setHorizontalAlignment(0);
    digitalTimer.addMouseListener(new MouseListener() {
          public void mouseClicked(MouseEvent e) {
            requestFocus();
          }
          
          public void mousePressed(MouseEvent e) {}
          
          public void mouseReleased(MouseEvent e) {}
          
          public void mouseEntered(MouseEvent e) {}
          
          public void mouseExited(MouseEvent e) {}
        });
    mainPanel.add(scramblePanel, "First");
    mainPanel.add(digitalTimer, "Center");
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
  
  private void startCountdown() {
    timerState = 1;
    mainPanel.setBackground(Color.GREEN);
    digitalTimer.setForeground(Color.WHITE);
    digitalTimer.setText("15");
    observationTime = 15;
    observationTimer.start();
  }
  
  private void startStopwatch() {
    observationTimer.stop();
    mainPanel.setBackground(Color.BLACK);
    digitalTimer.setForeground(Color.WHITE);
    digitalTimer.setText("0.0");
    seconds = milliseconds = 0;
    timerState = 3;
    startTime = System.currentTimeMillis();
    stopwatchTimer.start();
  }
  
  public void createSession() {}
  
  private void updateStats() {
    String[] s = session.getStats();
    for (int i = 0; i < 7; i++)
      statList[i].setText(s[i]); 
  }
  
  private void updateTimeList(int index) {
    DefaultTableModel model = (DefaultTableModel)timeList.getModel();
    int rowCount = model.getRowCount();
    model.removeRow(rowCount-- - index);
    for (int i = 0; i <= rowCount - index; i++)
      model.setValueAt(Integer.valueOf(rowCount - i), i, 0); 
  }
  
  private void addToTimeList(String solve) {
    DefaultTableModel model = (DefaultTableModel)timeList.getModel();
    model.insertRow(0, new Object[] { String.valueOf(session.getCount()), solve, scramble });
  }
  
  class Listeners implements KeyListener {
    public void keyTyped(KeyEvent e) {}
    
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == 32)
        if (timerState == 1) {
          digitalTimer.setForeground(Color.RED);
          timerHeld = true;
          holdTimer.start();
        } else if (timerState == 3) {
          time = System.currentTimeMillis() - startTime;
          timerState = 4;
          String solve = session.convertToString(time);
          stopwatchTimer.stop();
          digitalTimer.setText(solve);
          session.addTime(time);
          updateStats();
          addToTimeList(solve);
          scramble = scrambler.generateScramble();
          scrambleLabel.setText(scramble);
        }  
    }
    
    public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == 32)
        if (timerState == 0) {
          startCountdown();
        } else if (timerState == 1) {
          holdTimer.stop();
          digitalTimer.setForeground(Color.WHITE);
          timerHeld = false;
        } else if (timerState == 2) {
          startStopwatch();
        } else if (timerState == 4) {
          timerState = 0;
        }  
    }
  }
}