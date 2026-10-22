package game.options.view;

import game.audio.VolumeType;
import game.gui.common.AudioSliderUI;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.config.DisplayResolution;
import game.gui.common.PanelButton;
import game.gui.common.scaledlayout.LayoutBox;
import game.options.controller.OptionsAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class OptionsPanel extends JPanel
{
    private static final Image OPTIONS_CONTENT_IMAGE = new ImageIcon("resources/images/Options Content.png").getImage();
    private static final Image OPTIONS_BORDER_IMAGE = new ImageIcon("resources/images/Options Border.png").getImage();
    private static final int OPTIONS_BACKGROUND_IMAGE_TOTAL_WIDTH = OPTIONS_BORDER_IMAGE.getWidth(null);
    private static final int OPTIONS_BACKGROUND_IMAGE_TOTAL_HEIGHT = OPTIONS_BORDER_IMAGE.getHeight(null);
    
    private static final int OPTIONS_IMAGE_BORDER_WIDTH = (OPTIONS_BACKGROUND_IMAGE_TOTAL_WIDTH - OPTIONS_CONTENT_IMAGE.getWidth(null)) / 2;
    private static final int OPTIONS_IMAGE_BORDER_HEIGHT = (OPTIONS_BACKGROUND_IMAGE_TOTAL_HEIGHT - OPTIONS_CONTENT_IMAGE.getHeight(null)) / 2;
    
    private static final int LABEL_FONT_SIZE = 36;
    
    private final Map<VolumeType, Integer> appliedVolumeMap;
    private final Map<VolumeType, JSlider> volumeTypeJSliderMap;
    
    private final OptionsAction optionsAction;
    
    public OptionsPanel(OptionsAction oAction)
    {
        setLayout(new ScaledLayout());
        
        appliedVolumeMap = new HashMap<>();
        volumeTypeJSliderMap = new HashMap<>();
        for(VolumeType volumeType : VolumeType.values())
            appliedVolumeMap.put(volumeType, 100);
        
        optionsAction = oAction;
        
        createComponents();
    }
    
    private void createComponents()
    {
        LayoutBox mainBox = new LayoutBox();
        LayoutBox[] verticalBoxes = mainBox.splitVertical(20, 20, 20, 20, 20);
        
        JComboBox<DisplayResolution> resolutionComboBox = new JComboBox<>();
        for(DisplayResolution res : DisplayResolution.values()) resolutionComboBox.addItem(res);
        resolutionComboBox.addActionListener(e -> optionsAction.requestSetDisplayResolution((DisplayResolution)resolutionComboBox.getSelectedItem()));
        
        LayoutBox[] horizontalBoxes = verticalBoxes[0].splitHorizontal(40, 60);
        add(createLabel("Resolution:"), horizontalBoxes[0].insets(25));
        add(resolutionComboBox, horizontalBoxes[1].insets(20, 30, 20, 20));
        
        int i = 1;
        for(VolumeType volumeType : VolumeType.values())
        {
            horizontalBoxes = verticalBoxes[i++].splitHorizontal(40, 60);
            add(createLabel(volumeType.toString()), horizontalBoxes[0].insets(25));
            add(createSlider(volumeType), horizontalBoxes[1].insets(0, 25, 10, 25));
        }
        
        PanelButton applyButton = new PanelButton("Apply", e -> optionsAction.requestApplyChanges());
        add(applyButton, verticalBoxes[4]);
        
        addMouseListener(new MouseAdapter() {});
    }
    
    private JSlider createSlider(VolumeType volumeType)
    {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        volumeTypeJSliderMap.put(volumeType, slider);
        slider.setOpaque(false);
        slider.setUI(new AudioSliderUI(slider, Color.LIGHT_GRAY, 0.1f, 0.1f));
        slider.addChangeListener(e -> optionsAction.requestSetVolume(volumeType, slider.getValue()));
        
        return slider;
    }
    
    private JLabel createLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Agency FB", Font.PLAIN, LABEL_FONT_SIZE));
        label.setForeground(Color.WHITE);
        
        return label;
    }

    /**
     * Restores the sliders to their last applied volume settings rather than their default values.
     */
    public void resetValues()
    {
        for(Entry<VolumeType, JSlider> volumeTypeJSliderEntry : volumeTypeJSliderMap.entrySet())
            volumeTypeJSliderEntry.getValue().setValue(appliedVolumeMap.get(volumeTypeJSliderEntry.getKey()));
    }

    /**
     * Updates the stored volume values with the supplied volume values.
     *
     * @param volumeMap The volume settings to store as the last applied values.
     */
    public void setNewVolume(Map<VolumeType, Integer> volumeMap)
    {
        for(Entry<VolumeType, Integer> volumeEntry : volumeMap.entrySet())
            appliedVolumeMap.replace(volumeEntry.getKey(), volumeEntry.getValue());
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        g.drawImage(OPTIONS_BORDER_IMAGE, 0, 0, getWidth(), getHeight(), null);
        
        int insetX = (int)(getWidth() * ((double)OPTIONS_IMAGE_BORDER_WIDTH / OPTIONS_BACKGROUND_IMAGE_TOTAL_WIDTH));
        int insetY = (int)(getHeight() * ((double)OPTIONS_IMAGE_BORDER_HEIGHT / OPTIONS_BACKGROUND_IMAGE_TOTAL_HEIGHT));
        
        g.drawImage(OPTIONS_CONTENT_IMAGE, insetX, insetY, getWidth() - insetX * 2, getHeight() - insetY * 2, null);
    }
}