package ControlPanel;

import Engine.MusicPlayer;
import Levels.Level;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import static javax.swing.JOptionPane.showMessageDialog;

public class ControlPanel extends JFrame implements ActionListener, ChangeListener {

    private final JButton playPauseButton = new JButton();
    private final JButton openFileButton = new JButton("Open");

    private final JButton templateDefaultButton = new JButton("Default");
    private final JButton templateType1Button = new JButton("Type 1");
    private final JButton templateType2Button = new JButton("Type 2");

    private final JSlider volumeSlider = new JSlider(0,100);
    private final JProgressBar playbackProgressBar = new JProgressBar(0,100);
    private final JLabel currentPlaybackTime = new JLabel();
    private final JLabel overallTime = new JLabel();
    private final JLabel audioTitle = new JLabel();
    private final MusicPlayer player;
    private final JFileChooser fc = new JFileChooser();
    private int colorTemplate = 0;
    private final JLabel colorTemplateText = new JLabel("Colors Templates:");

    public int getColorTemplate() {
        return colorTemplate;
    }

    public ControlPanel(MusicPlayer player){
        super();
        this.player = player;

        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        float ration = (float) player.getPlaybackPosition() / player.getDuration();
        playbackProgressBar.setValue((int)(ration*100));

        playPauseButton.addActionListener(this);
        playPauseButton.setFocusable(false);
        playPauseButton.setText("Waiting");
        playPauseButton.setEnabled(false);

        templateDefaultButton.addActionListener(this);
        templateDefaultButton.setFocusable(false);

        templateType1Button.addActionListener(this);
        templateType1Button.setFocusable(false);

        templateType2Button.addActionListener(this);
        templateType2Button.setFocusable(false);

        openFileButton.addActionListener(this);
        openFileButton.setFocusable(false);

        volumeSlider.addChangeListener(this);
        volumeSlider.setFocusable(false);
        volumeSlider.setValue((int) player.getVolume());

        fc.setCurrentDirectory(new File
                (System.getProperty("user.home") +
                        System.getProperty("file.separator")+ "Music"));

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(3, 1));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(currentPlaybackTime);
        topPanel.add(playbackProgressBar);
        topPanel.add(overallTime);
        controlsPanel.add(topPanel);

        JPanel templatePanel = new JPanel();
        templatePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        templatePanel.add(colorTemplateText);
        templatePanel.add(templateDefaultButton);
        templatePanel.add(templateType1Button);
        templatePanel.add(templateType2Button);
        controlsPanel.add(templatePanel);

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        middlePanel.add(playPauseButton);
        middlePanel.add(volumeSlider);
        middlePanel.add(openFileButton);
        controlsPanel.add(middlePanel);

        add(controlsPanel, BorderLayout.SOUTH);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(audioTitle);
        add(titlePanel, BorderLayout.NORTH);

        setResizable(false);
        setSize(400,200);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        toFront();
        requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == playPauseButton ){
            if(player.isPaused()) {
                player.resume();
                playPauseButton.setText("Playing");
            }
            else {
                player.pause();
                playPauseButton.setText("Paused");
            }
        }

        if (e.getSource() == openFileButton) {
            int returnVal = fc.showOpenDialog(ControlPanel.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                boolean success  = player.openFile(file.getAbsolutePath());
                if(success) {
                    playPauseButton.setText("Playing");
                    playPauseButton.setEnabled(true);
                }
                else showMessageDialog(ControlPanel.this,
                        "File open error");
            }
        }

        if (e.getSource() == templateDefaultButton){
            this.colorTemplate = 0;
        }

        if (e.getSource() == templateType1Button){
            this.colorTemplate = 1;
        }

        if (e.getSource() == templateType2Button){
            this.colorTemplate = 2;
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == volumeSlider){
            player.setVolume(volumeSlider.getValue());
        }
    }

    public void updateProgressBar(float dt){
        float ration = (float) player.getPlaybackPosition() / player.getDuration();
        playbackProgressBar.setValue((int) (ration*100));
        String currentTimeText;

        if(player.isPlaying()) {
            currentTimeText = String.format("%02d:%02d", player.getPlaybackPosition()/60,
                    player.getPlaybackPosition()%60);
        }
        else {
            currentTimeText = "00:00";
            playPauseButton.setEnabled(false);
            playPauseButton.setText("Waiting");
        }

        currentPlaybackTime.setText(currentTimeText);

        String overallTimeString = String.format("%02d:%02d", player.getDuration()/60,
                player.getDuration()%60);

        if(player.getDuration()==-1 || !player.isPlaying())
            overallTimeString = "--:--";

        overallTime.setText(overallTimeString);
        audioTitle.setText(player.getFileName());
    }
}