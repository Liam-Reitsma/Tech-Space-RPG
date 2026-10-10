package game.gui.errors;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.gui.common.scaledlayout.ScaledLayoutAnchor;

import javax.swing.*;
import java.awt.*;

public class ErrorTitlePanel extends JPanel
{
    private static final Image IMAGE = new ImageIcon("resources/images/Options Title Content.png").getImage();
    
    public ErrorTitlePanel(String title)
    {
        setLayout(new ScaledLayout());
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("OCR Extended A", Font.BOLD, 1000 / title.length()));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(titleLabel, new LayoutBox());
        setOpaque(false);
    }
    
    protected void paintComponent(Graphics g)
    {
        g.drawImage(IMAGE, 0, 0, getWidth(), getHeight(), this);
        
        super.paintComponent(g);
    }
}