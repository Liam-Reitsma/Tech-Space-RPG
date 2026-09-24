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
    private static final Image OPTIONS_CONTENT_IMAGE = new ImageIcon("resources/images/Options Content.png").getImage();
    private static final Image OPTIONS_BORDER_IMAGE = new ImageIcon("resources/images/Options Border.png").getImage();
    private static final int OPTIONS_BACKGROUND_IMAGE_TOTAL_WIDTH = OPTIONS_BORDER_IMAGE.getWidth(null);
    private static final int OPTIONS_BACKGROUND_IMAGE_TOTAL_HEIGHT = OPTIONS_BORDER_IMAGE.getHeight(null);
    
    private static final int OPTIONS_IMAGE_BORDER_WIDTH = (OPTIONS_BACKGROUND_IMAGE_TOTAL_WIDTH - OPTIONS_CONTENT_IMAGE.getWidth(null)) / 2;
    private static final int OPTIONS_IMAGE_BORDER_HEIGHT = (OPTIONS_BACKGROUND_IMAGE_TOTAL_HEIGHT - OPTIONS_CONTENT_IMAGE.getHeight(null)) / 2;
    
    private static final int LABEL_FONT_SIZE = 72;
    
    private static final int SLIDER_PREFERRED_WIDTH = 860;
    private static final int SLIDER_PREFERRED_HEIGHT = 129;
    
    private static final int BUTTON_PREFERRED_WIDTH = 200;
    private static final int BUTTON_PREFERRED_HEIGHT = 45;
    
    GUIAction guiAction;
    
    public OptionsPanel(GUIAction action)
    {
        guiAction = action;
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbConstraints = new GridBagConstraints();
        
        setBorder(BorderFactory.createEmptyBorder(30,30,30,30));
        
        createComponents(gbConstraints);
        createLabels(gbConstraints);
        addComponentListener(new ComponentAdapter()
        {
            @Override public void componentResized(ComponentEvent e)
            {
                Container parent = getParent();
                
                double panelWidth = parent.getWidth();
                double panelHeight = parent.getHeight();
                
                for(Component component : getComponents())
                {
                    
                    if(component instanceof JSlider slider)
                    {
                        slider.setPreferredSize(new Dimension((int)Math.round(SLIDER_PREFERRED_WIDTH * 0.8f * (panelWidth / PROGRAM_WIDTH)), (int)Math.round(SLIDER_PREFERRED_HEIGHT * 0.5f * (panelHeight / PROGRAM_HEIGHT))));
                    }
                    else if(component instanceof JLabel label)
                    {
                        label.setFont(label.getFont().deriveFont((float)(LABEL_FONT_SIZE * panelWidth) / PROGRAM_WIDTH));
                    }
                    else if(component instanceof JComboBox<?> combo)
                    {
                        combo.setFont(combo.getFont().deriveFont((float)(LABEL_FONT_SIZE / 2f * panelWidth) / PROGRAM_WIDTH));
                    }
                    else if(component instanceof PanelButton button)
                    {
                        button.resizeButton((float)((BUTTON_PREFERRED_WIDTH * panelWidth) / PROGRAM_WIDTH), ((float)(BUTTON_PREFERRED_HEIGHT * panelHeight) / PROGRAM_HEIGHT));
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
        PanelButton applyResButton = new PanelButton("Apply", resButtonListener);
        add(applyResButton, gbConstraints);
        
        addMouseListener(new MouseAdapter() {});
    }
    
    private JSlider createSlider()
    {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        slider.setOpaque(false);
        slider.setUI(new AudioSliderUI(slider, Color.LIGHT_GRAY, 0.1f, 0.1f));
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
        add(createLabel("Music Volume", labelFont), gbConstraints);
    }
    
    private JLabel createLabel(String name, Font font)
    {
        JLabel label = new JLabel(name);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        
        return label;
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