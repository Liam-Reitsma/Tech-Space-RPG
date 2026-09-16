package game.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class OptionsPanel extends JPanel
{
    public OptionsPanel(GUIAction guiAction)
    {
        setLayout(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints();
        
        int y = 0;
        
        gbConstraints.fill = GridBagConstraints.NONE;
        gbConstraints.anchor = GridBagConstraints.CENTER;
        gbConstraints.gridy = ++y;
        gbConstraints.gridx = 0;
        
        gbConstraints.insets = new Insets(0, 0, 60, 0);
        
        JComboBox<DisplayResolution> resolutionComboBox = new JComboBox<>();
        
        for(DisplayResolution res : DisplayResolution.values())
        {
            resolutionComboBox.addItem(res);
        }
        
        add(resolutionComboBox, gbConstraints);
        
        gbConstraints.gridy = ++y;
        JSlider masterAudio = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        masterAudio.setMajorTickSpacing(25);
        masterAudio.setPaintTicks(true);
        masterAudio.setPaintLabels(true);
        
        add(masterAudio, gbConstraints);
        
        gbConstraints.gridy = ++y;
        JSlider sfxAudio = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        sfxAudio.setMajorTickSpacing(25);
        sfxAudio.setPaintTicks(true);
        sfxAudio.setPaintLabels(true);
        
        add(sfxAudio, gbConstraints);
        
        gbConstraints.gridy = ++y;
        JSlider musicAudio = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
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
}