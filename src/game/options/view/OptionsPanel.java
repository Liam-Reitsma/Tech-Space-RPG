package game.options.view;

import game.gui.common.AudioSliderUI;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.config.DisplayResolution;
import game.gui.common.PanelButton;
import game.gui.common.scaledlayout.LayoutBox;
import game.options.controller.OptionsAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class OptionsPanel extends JPanel
{
    private static final Image OPTIONS_CONTENT_IMAGE = new ImageIcon("resources/images/Options Content.png").getImage();
    private static final Image OPTIONS_BORDER_IMAGE = new ImageIcon("resources/images/Options Border.png").getImage();
    private static final int OPTIONS_BACKGROUND_IMAGE_TOTAL_WIDTH = OPTIONS_BORDER_IMAGE.getWidth(null);
    private static final int OPTIONS_BACKGROUND_IMAGE_TOTAL_HEIGHT = OPTIONS_BORDER_IMAGE.getHeight(null);
    
    private static final int OPTIONS_IMAGE_BORDER_WIDTH = (OPTIONS_BACKGROUND_IMAGE_TOTAL_WIDTH - OPTIONS_CONTENT_IMAGE.getWidth(null)) / 2;
    private static final int OPTIONS_IMAGE_BORDER_HEIGHT = (OPTIONS_BACKGROUND_IMAGE_TOTAL_HEIGHT - OPTIONS_CONTENT_IMAGE.getHeight(null)) / 2;
    
    private static final int LABEL_FONT_SIZE = 36;
    
    private final OptionsAction optionsAction;
    
    public OptionsPanel(OptionsAction oAction)
    {
        setLayout(new ScaledLayout());
        
        optionsAction = oAction;
        
        createComponents();
    }
    
    private void createComponents()
    {
        LayoutBox mainBox = new LayoutBox();
        LayoutBox[] verticalBoxes = mainBox.splitVertical(20, 20, 20, 20, 20);
        String[] texts = new String[]{"Master Audio", "SFX Audio", "Music Audio"};
        
        JComboBox<DisplayResolution> resolutionComboBox = new JComboBox<>();
        for(DisplayResolution res : DisplayResolution.values()) resolutionComboBox.addItem(res);
        
        
        LayoutBox[] horizontalBoxes = verticalBoxes[0].splitHorizontal(40, 60);
        add(createLabel("Resolution:"), horizontalBoxes[0].insets(25));
        add(resolutionComboBox, horizontalBoxes[1].insets(20, 30, 20, 20));
        
        for(int i = 0; i < texts.length; i++)
        {
            horizontalBoxes = verticalBoxes[i + 1].splitHorizontal(40, 60);
            add(createLabel(texts[i]), horizontalBoxes[0].insets(25));
            add(createSlider(), horizontalBoxes[1].insets(0, 25, 10, 25));
        }
        
        ActionListener resButtonListener = e -> optionsAction.requestResolutionChange((DisplayResolution) resolutionComboBox.getSelectedItem());
        
        PanelButton applyResButton = new PanelButton("Apply", resButtonListener);
        add(applyResButton, verticalBoxes[4]);
        
        addMouseListener(new MouseAdapter() {});
    }
    
    private JSlider createSlider()
    {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
        slider.setOpaque(false);
        slider.setUI(new AudioSliderUI(slider, Color.LIGHT_GRAY, 0.1f, 0.1f));
        
        return slider;
    }
    
    private JLabel createLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Agency FB", Font.PLAIN, LABEL_FONT_SIZE));
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