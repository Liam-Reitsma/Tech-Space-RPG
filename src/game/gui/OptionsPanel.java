package game.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OptionsPanel extends JPanel
{
    GUIAction guiAction;
    
    public OptionsPanel(GUIAction action)
    {
        guiAction = action;
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints();
        createComponents(gbConstraints);
        createLabels(gbConstraints);
        
        setSize(getPreferredSize());
    }
    
    private void createComponents(GridBagConstraints gbConstraints)
    {
        gbConstraints.anchor = GridBagConstraints.NORTH;
        gbConstraints.fill = GridBagConstraints.NONE;
        gbConstraints.insets = new Insets(30, 10, 30, 30);
        gbConstraints.gridx = 1;
        
        int y = 0;
        
        JComboBox<DisplayResolution> resolutionComboBox = new JComboBox<>();
        gbConstraints.gridy = ++y;
        for(DisplayResolution res : DisplayResolution.values())
        {
            resolutionComboBox.addItem(res);
        }
        add(resolutionComboBox, gbConstraints);
        
        JSlider masterAudio = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        gbConstraints.anchor = GridBagConstraints.SOUTH;
        gbConstraints.gridy = ++y;
        masterAudio.setMajorTickSpacing(25);
        masterAudio.setPaintTicks(true);
        masterAudio.setPaintLabels(true);
        add(masterAudio, gbConstraints);
        
        JSlider sfxAudio = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        gbConstraints.gridy = ++y;
        sfxAudio.setMajorTickSpacing(25);
        sfxAudio.setPaintTicks(true);
        sfxAudio.setPaintLabels(true);
        add(sfxAudio, gbConstraints);
        
        JSlider musicAudio = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        gbConstraints.gridy = ++y;
        musicAudio.setMajorTickSpacing(25);
        musicAudio.setPaintTicks(true);
        musicAudio.setPaintLabels(true);
        add(musicAudio, gbConstraints);
        
        ActionListener resButtonListener = e -> guiAction.changeResolution((DisplayResolution) resolutionComboBox.getSelectedItem());
        
        ConfirmButton applyResButton = new ConfirmButton("Apply", resButtonListener);
        
        gbConstraints.gridy = ++y;
        gbConstraints.anchor = GridBagConstraints.SOUTHEAST;
        add(applyResButton, gbConstraints);
    }
    
    private void createLabels(GridBagConstraints gbConstraints)
    {
        gbConstraints.anchor = GridBagConstraints.NORTH;
        gbConstraints.gridx = 0;
        gbConstraints.insets = new Insets(30, 30, 30, 10);
        
        int y = 0;
        
        gbConstraints.gridy = ++y;
        add(new JLabel("Resolution"), gbConstraints);
        
        gbConstraints.gridy = ++y;
        add(new JLabel("Master Volume"), gbConstraints);
        
        gbConstraints.gridy = ++y;
        add(new JLabel("SFX Volume"), gbConstraints);
        
        gbConstraints.gridy = ++y;
        add(new JLabel("Music Volume"), gbConstraints);
    }
}