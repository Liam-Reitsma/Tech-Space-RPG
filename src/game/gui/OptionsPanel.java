package game.gui;

import static game.gui.GUIConfig.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;

public class OptionsPanel extends JPanel
{
    private static final int LABEL_FONT_SIZE = 72;
    private static final int SLIDER_PREFERRED_WIDTH = 860;
    private static final int SLIDER_PREFERRED_HEIGHT = 129;
    GUIAction guiAction;
    
    public OptionsPanel(GUIAction action)
    {
        guiAction = action;
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints();
        createComponents(gbConstraints);
        createLabels(gbConstraints);
        addComponentListener(new ComponentAdapter()
        {
            @Override public void componentResized(ComponentEvent e)
            {
                double panelWidth = getSize().getWidth();
                double panelHeight = getSize().getHeight();
                
                for(Component component : getComponents())
                {
                    
                    if(component instanceof JSlider slider)
                    {
                        slider.setPreferredSize(new Dimension((int)Math.round(SLIDER_PREFERRED_WIDTH * (panelWidth / PROGRAM_WIDTH)), (int)Math.round(SLIDER_PREFERRED_HEIGHT * (panelHeight / PROGRAM_HEIGHT))));
                    }
                    else
                    {
                        component.setFont(component.getFont().deriveFont((float)(LABEL_FONT_SIZE * panelWidth) / PROGRAM_WIDTH));
                    }
                }
                
                revalidate();
                repaint();
            }
        });
    }
    
    private void createComponents(GridBagConstraints gbConstraints)
    {
        gbConstraints.anchor = GridBagConstraints.CENTER;
        gbConstraints.fill = GridBagConstraints.NONE;
        gbConstraints.gridx = 1;
        gbConstraints.weighty = 0.0;
        gbConstraints.insets = new Insets(0, 10, 0, 0);
        
        int y = 0;
        
        gbConstraints.gridy = ++y;
        JComboBox<DisplayResolution> resolutionComboBox = new JComboBox<>();
        for(DisplayResolution res : DisplayResolution.values())
        {
            resolutionComboBox.addItem(res);
        }
        add(resolutionComboBox, gbConstraints);
        
        gbConstraints.fill = GridBagConstraints.HORIZONTAL;
        gbConstraints.weightx = 1.0;
        gbConstraints.insets = new Insets(10, 10, 10, 0);
        
        gbConstraints.gridy = ++y;
        add(createSlider(), gbConstraints);
        
        gbConstraints.gridy = ++y;
        add(createSlider(), gbConstraints);
        
        gbConstraints.gridy = ++y;
        add(createSlider(), gbConstraints);
        
        ActionListener resButtonListener = e -> guiAction.changeResolution((DisplayResolution) resolutionComboBox.getSelectedItem());
        
        gbConstraints.anchor = GridBagConstraints.SOUTHEAST;
        gbConstraints.fill = GridBagConstraints.NONE;
        gbConstraints.gridy = ++y;
        gbConstraints.weightx = 0.0;
        gbConstraints.weighty = 1.0;
        ConfirmButton applyResButton = new ConfirmButton("Apply", resButtonListener);
        add(applyResButton, gbConstraints);
        
        addMouseListener(new MouseAdapter() {});
    }
    
    private JSlider createSlider()
    {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        slider.setOpaque(false);
        slider.setUI(new AudioSliderUI(slider, Color.LIGHT_GRAY, 0.1f));
        slider.setMinimumSize(new Dimension(SLIDER_PREFERRED_WIDTH / 100, SLIDER_PREFERRED_HEIGHT / 100));
        slider.setPreferredSize(new Dimension(SLIDER_PREFERRED_WIDTH, SLIDER_PREFERRED_HEIGHT));
        
        return slider;
    }
    
    private void createLabels(GridBagConstraints gbConstraints)
    {
        gbConstraints.anchor = GridBagConstraints.CENTER;
        gbConstraints.gridx = 0;
        gbConstraints.weightx = 0.0;
        gbConstraints.weighty = 0.0;
        gbConstraints.insets = new Insets(0, 0, 0, 10);
        
        int y = 0;
        
        Font labelFont = new Font("Agency FB", Font.PLAIN, LABEL_FONT_SIZE);
        
        gbConstraints.gridy = ++y;
        add(createLabel("Resolution", labelFont), gbConstraints);
        
        gbConstraints.gridy = ++y;
        add(createLabel("Master Volume", labelFont), gbConstraints);
        
        gbConstraints.gridy = ++y;
        add(createLabel("SFX Volume", labelFont), gbConstraints);
        
        gbConstraints.gridy = ++y;
        add(new JLabel("Music Volume"), gbConstraints);
        add(createLabel("Music Volume", labelFont), gbConstraints);
    }
    
    private JLabel createLabel(String name, Font font)
    {
        JLabel label = new JLabel(name);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        
        return label;
    }
    
    }
}