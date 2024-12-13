package ControlPanel;

import Drawable.DrawableShape;
import Engine.MusicPlayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.nio.file.FileSystems;

import static javax.swing.JOptionPane.showMessageDialog;

public class ControlPanel extends JFrame implements ActionListener, ChangeListener, KeyListener {

    private final JButton playPauseButton = new JButton();
    private final JButton openFileButton = new JButton("Open");

    private final JLabel shapeText = new JLabel("Shapes:");
    private final JButton shapeCircle = new JButton("Circle");
    private final JButton shapeHeart = new JButton("Heart");

    private final JLabel colorTemplateText = new JLabel("Colors Templates:");
    private final JButton templateDefaultButton = new JButton("Default");
    private final JButton templateType1Button = new JButton("Type 1");
    private final JButton templateType2Button = new JButton("Type 2");
    private final JButton templateType3Button = new JButton("Type 3");

    private final JSlider volumeSlider = new JSlider(0,100);
    private final JProgressBar playbackProgressBar = new JProgressBar(0,100);
    private final JLabel currentPlaybackTime = new JLabel();
    private final JLabel overallTime = new JLabel();
    private final JLabel audioTitle = new JLabel();
    private final JFileChooser fc = new JFileChooser();
    private int colorTemplate = 0;
    private DrawableShape shape = DrawableShape.CIRCLE;
    private final MusicPlayer player;

    public int getColorTemplate() {
        return colorTemplate;
    }

    public DrawableShape getDrawableShape() {
        return shape;
    }

    public ControlPanel(MusicPlayer player){
        super();
        this.player = player;

        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        float ration = (float) player.getPlaybackPosition() / player.getDuration();
        playbackProgressBar.setValue((int)(ration*100));

        addKeyListener(this);

        playPauseButton.addActionListener(this);
        playPauseButton.setFocusable(false);
        playPauseButton.setText("Waiting");
        playPauseButton.setEnabled(false);

        shapeCircle.addActionListener(this);
        shapeCircle.setFocusable(false);

        shapeHeart.addActionListener(this);
        shapeHeart.setFocusable(false);

        templateDefaultButton.addActionListener(this);
        templateDefaultButton.setFocusable(false);

        templateType1Button.addActionListener(this);
        templateType1Button.setFocusable(false);

        templateType2Button.addActionListener(this);
        templateType2Button.setFocusable(false);

        templateType3Button.addActionListener(this);
        templateType3Button.setFocusable(false);

        openFileButton.addActionListener(this);
        openFileButton.setFocusable(false);

        volumeSlider.addChangeListener(this);
        volumeSlider.setFocusable(false);
        volumeSlider.setValue((int) player.getVolume());

        fc.setCurrentDirectory(new File
                (System.getProperty("user.home") +
                        FileSystems.getDefault().getSeparator() + "Music"));

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(4, 1));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(currentPlaybackTime);
        topPanel.add(playbackProgressBar);
        topPanel.add(overallTime);
        controlsPanel.add(topPanel);

        JPanel shapePanel = new JPanel();
        shapePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        shapePanel.add(shapeText);
        shapePanel.add(shapeCircle);
        shapePanel.add(shapeHeart);
        controlsPanel.add( shapePanel);

        JPanel templatePanel = new JPanel();
        templatePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        templatePanel.add(colorTemplateText);
        templatePanel.add(templateDefaultButton);
        templatePanel.add(templateType1Button);
        templatePanel.add(templateType2Button);
        templatePanel.add(templateType3Button);
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

        if (e.getSource() == templateType3Button){
            this.colorTemplate = 3;
        }

        if (e.getSource() == shapeCircle){
            shape = DrawableShape.CIRCLE;
        }

        if (e.getSource() == shapeHeart){
            shape = DrawableShape.HEART;
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

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            if(player.isPaused()) {
                player.resume();
                playPauseButton.setText("Playing");
            }
            else {
                player.pause();
                playPauseButton.setText("Paused");
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            player.setVolume(player.getVolume()-10);
            volumeSlider.setValue(player.getVolume());
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            player.setVolume(player.getVolume()+10);
            volumeSlider.setValue(player.getVolume());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}