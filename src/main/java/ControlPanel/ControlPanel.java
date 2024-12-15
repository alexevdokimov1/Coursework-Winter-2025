package ControlPanel;

import Drawable.DrawableShape;
import Engine.MusicPlayer;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.Properties;

import static java.lang.System.exit;
import static javax.swing.JOptionPane.showMessageDialog;

public class ControlPanel extends JFrame implements ActionListener, ChangeListener, KeyListener {

    private final JButton playPauseButton = new JButton();
    private final JButton openFileButton = new JButton("Открыть");

    private final JMenuBar bar = new JMenuBar();

    private final JMenu fileMenu = new JMenu("Файл");
    private final JMenuItem fileItem_Open = new JMenuItem("Открыть");
    private final JMenuItem fileItem_Exit = new JMenuItem("Выйти");

    private final JMenu shapeMenu = new JMenu("Форма");
    private final JMenuItem shapeItem_Circle = new JMenuItem("Кольцо");
    private final JMenuItem shapeItem_Heart = new JMenuItem("Сердце");

    private final JMenu colorTemplateMenu = new JMenu("Цветовые схемы");
    private final JMenuItem colorTemplateItem_Default = new JMenuItem("По умолчанию");
    private final JMenuItem colorTemplateItem_Type1 = new JMenuItem("Тип 1");
    private final JMenuItem colorTemplateItem_Type2 = new JMenuItem("Тип 2");
    private final JMenuItem colorTemplateItem_Type3 = new JMenuItem("Тип 3");

    private final JSlider volumeSlider = new JSlider(0,100);
    private final JProgressBar playbackProgressBar = new JProgressBar(0,100);
    private final JLabel currentPlaybackTime = new JLabel();
    private final JLabel overallTime = new JLabel();
    private final JLabel audioTitle = new JLabel();
    private final JFileChooser fc = new JFileChooser();
    private int colorTemplate = 0;
    private DrawableShape shape = DrawableShape.CIRCLE;
    private final MusicPlayer player;

    private final Path path = Paths.get("settings/Player Config.xml");

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
        playPauseButton.setText("Ожидание");
        playPauseButton.setEnabled(false);

        openFileButton.addActionListener(this);
        openFileButton.setFocusable(false);

        volumeSlider.addChangeListener(this);
        volumeSlider.setFocusable(false);
        volumeSlider.setValue(player.getVolume());

        fc.setCurrentDirectory(new File
                (System.getProperty("user.home") +
                        FileSystems.getDefault().getSeparator() + "Music"));

        fileMenu.add(fileItem_Open);
        fileMenu.add(fileItem_Exit);

        fileItem_Open.addActionListener(this);
        fileItem_Exit.addActionListener(this);

        shapeMenu.add(shapeItem_Circle);
        shapeMenu.add(shapeItem_Heart);

        shapeItem_Circle.addActionListener(this);
        shapeItem_Heart.addActionListener(this);

        colorTemplateMenu.add(colorTemplateItem_Default);
        colorTemplateMenu.add(colorTemplateItem_Type1);
        colorTemplateMenu.add(colorTemplateItem_Type2);
        colorTemplateMenu.add(colorTemplateItem_Type3);

        colorTemplateItem_Default.addActionListener(this);
        colorTemplateItem_Type1.addActionListener(this);
        colorTemplateItem_Type2.addActionListener(this);
        colorTemplateItem_Type3.addActionListener(this);

        bar.add(fileMenu);
        bar.add(shapeMenu);
        bar.add(colorTemplateMenu);

        setJMenuBar(bar);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(3, 1));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(currentPlaybackTime);
        topPanel.add(playbackProgressBar);
        topPanel.add(overallTime);
        controlsPanel.add(topPanel);

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

        Properties properties = new Properties();
        try {
            properties.loadFromXML(Files.newInputStream(path));
            shape = DrawableShape.getType(properties.getProperty("SHAPE"));
            colorTemplate = Integer.parseInt(properties.getProperty("TEMPLATE"));
            player.setVolume(Integer.parseInt(properties.getProperty("VOLUME")));
            volumeSlider.setValue(player.getVolume());

            String lastOpenFile = properties.getProperty("FILE");

            if(!lastOpenFile.equals("0")){
                playPauseButton.setEnabled(true);
                player.openFile(lastOpenFile);
                player.pause();
            }

        } catch (IOException e) {
        }

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
                playPauseButton.setText("Играет");
            }
            else {
                player.pause();
                playPauseButton.setText("Пауза");
            }
        }

        if (e.getSource() == openFileButton || e.getSource() == fileItem_Open) {
            int returnVal = fc.showOpenDialog(ControlPanel.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                boolean success  = player.openFile(file.getAbsolutePath());
                saveValues();
                playPauseButton.setText("Играет");
                playPauseButton.setEnabled(true);
                if(!success) {
                   showMessageDialog(ControlPanel.this,
                            "Ошибка при открытии файла");
                    playPauseButton.setEnabled(false);
                }
            }
        }

        if (e.getSource() == fileItem_Exit) {
            exit(0);
        }

        if (e.getSource() == colorTemplateItem_Default){
            this.colorTemplate = 0;
            saveValues();
        }

        if (e.getSource() == colorTemplateItem_Type1){
            this.colorTemplate = 1;
            saveValues();
        }

        if (e.getSource() == colorTemplateItem_Type2){
            this.colorTemplate = 2;
            saveValues();
        }

        if (e.getSource() == colorTemplateItem_Type3){
            this.colorTemplate = 3;
            saveValues();
        }

        if (e.getSource() == shapeItem_Circle){
            shape = DrawableShape.CIRCLE;
            saveValues();
        }

        if (e.getSource() == shapeItem_Heart){
            shape = DrawableShape.HEART;
            saveValues();
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource() == volumeSlider){
            player.setVolume(volumeSlider.getValue());
            saveValues();
        }
    }

    public void updateProgressBar(){
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
            playPauseButton.setText("Ожидание");
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
                playPauseButton.setText("Играет");
            }
            else {
                player.pause();
                playPauseButton.setText("Пауза");
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            player.setVolume(player.getVolume()-10);
            volumeSlider.setValue(player.getVolume());
            saveValues();
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            player.setVolume(player.getVolume()+10);
            volumeSlider.setValue(player.getVolume());
            saveValues();
        }
    }

    public void saveValues(){
        Properties properties = new Properties();
        properties.setProperty("SHAPE", shape.toString());
        properties.setProperty("TEMPLATE", Integer.toString(colorTemplate));
        properties.setProperty("VOLUME", Integer.toString(player.getVolume()));
        properties.setProperty("FILE",player.getFilepath());
        try {
            properties.storeToXML(Files.newOutputStream(path),"Config file for player appearance");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}