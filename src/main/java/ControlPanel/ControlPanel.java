package ControlPanel;

import Engine.MusicPlayer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ControlPanel extends JFrame implements ActionListener, ChangeListener {

    private final JButton changeAudioState = new JButton("Play");
    private final JSlider volumeSlider = new JSlider(0,100);
    private final JProgressBar playbackProgressBar = new JProgressBar(0,100);
    private final JLabel currentPlaybackTime = new JLabel();
    private final JLabel overallTime = new JLabel();
    private final JFileChooser fileChooser = new JFileChooser();
    private final MusicPlayer player;

    public ControlPanel(MusicPlayer player){
        super("Player");

        this.player = player;
        volumeSlider.setValue((int) player.getVolume());
        float ration = (float) player.getPlaybackPosition() / player.getDuration();
        playbackProgressBar.setValue((int)(ration*100));

        setLayout(new GridLayout(3,2));
        changeAudioState.addActionListener(this);
        changeAudioState.setFocusable(false);
        volumeSlider.addChangeListener(this);
        volumeSlider.setFocusable(false);
        fileChooser.addActionListener(this);

        add(playbackProgressBar);
        add(changeAudioState);
        add(fileChooser);
        add(volumeSlider);
        add(currentPlaybackTime);
        add(overallTime);

        setSize(300,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == changeAudioState ){
            if(player.isPaused()) player.resume();
            else player.pause();
        }
        if(e.getSource() == fileChooser){
            player.openFile(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() instanceof JSlider slider){
            player.setVolume(slider.getValue());
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
        else currentTimeText = "00:00";

        currentPlaybackTime.setText(currentTimeText);

        String overallTimeString = String.format("%02d:%02d", player.getDuration()/60,
                player.getDuration()%60);
        overallTime.setText(overallTimeString);
    }
}
// Bugs
// Время на таймере скачет