package game.gui.options;

import game.gui.common.PanelCloseButton;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledConstraint;
import game.gui.common.scaledlayout.ScaledLayoutAnchor;
import game.options.OptionsAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

public class OptionsTitlePanel extends JPanel
{
    private static final Image OPTIONS_TITLE_CONTENT = new ImageIcon("resources/images/Options Title Content.png").getImage();
    private static final Image OPTIONS_TITLE_BORDER = new ImageIcon("resources/images/Options Title Border.png").getImage();
    
    private static final int OPTIONS_TITLE_IMAGE_TOTAL_WIDTH = OPTIONS_TITLE_BORDER.getWidth(null);
    private static final int OPTIONS_TITLE_IMAGE_TOTAL_HEIGHT = OPTIONS_TITLE_BORDER.getHeight(null);
    
    private static final int OPTIONS_TITLE_IMAGE_BORDER_WIDTH = (OPTIONS_TITLE_IMAGE_TOTAL_WIDTH - OPTIONS_TITLE_CONTENT.getWidth(null)) / 2;
    private static final int OPTIONS_TITLE_IMAGE_BORDER_HEIGHT = (OPTIONS_TITLE_IMAGE_TOTAL_HEIGHT - OPTIONS_TITLE_CONTENT.getHeight(null)) / 2;
    
    private static final int LABEL_FONT_SIZE = 45;
    
    public OptionsTitlePanel(LayoutManager2 layoutManager, OptionsAction optionsAction)
    {
        super(layoutManager);
        
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
        ActionListener closeButtonListener = e -> optionsAction.requestCloseOptions();
        LayoutBox mainBox = new LayoutBox(0, 0, 100, 100);
        LayoutBox[] boxes = mainBox.splitHorizontal(50, 50);
        
        JLabel label = new JLabel("Options");
        label.setFont(new Font("Agency FB", Font.BOLD, LABEL_FONT_SIZE));
        label.setForeground(Color.WHITE);
        add(label, boxes[0].insets(10, 0, 0, 0));
        
        PanelCloseButton applyResButton = new PanelCloseButton("X", closeButtonListener);
        add(applyResButton, new ScaledConstraint(1, new LayoutBox(boxes[1], 50, 50, ScaledLayoutAnchor.EAST), ScaledLayoutAnchor.CENTER));
        
        addMouseListener(new MouseAdapter() {});
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(OPTIONS_TITLE_BORDER, 0, 0, getWidth(), getHeight(), null);
        
        int insetX = (int)(getWidth() * ((double) OPTIONS_TITLE_IMAGE_BORDER_WIDTH / OPTIONS_TITLE_IMAGE_TOTAL_WIDTH));
        int insetY = (int)(getHeight() * ((double) OPTIONS_TITLE_IMAGE_BORDER_HEIGHT / OPTIONS_TITLE_IMAGE_TOTAL_HEIGHT));
        
        g.drawImage(OPTIONS_TITLE_CONTENT, insetX, insetY, getWidth() - insetX * 2, getHeight() - insetY * 2, null);
    }
}