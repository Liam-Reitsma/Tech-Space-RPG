package game.player.view;

import game.items.Item;
import game.items.armour.Armour;
import game.items.weapon.Weapon;

import javax.swing.*;
import java.awt.*;

public class ItemSlot extends JPanel
{
    private static final Image EMPTY_SLOT = new ImageIcon("resources/images/inventory/empty.png").getImage();
    private static final Image WEAPON_SLOT = new ImageIcon("resources/images/inventory/weapon.png").getImage();
    private static final Image ARMOUR_SLOT = new ImageIcon("resources/images/inventory/armour.png").getImage();
    
    private Image image;
    private int quantity;
    
    public ItemSlot()
    {
        image = EMPTY_SLOT;
        quantity = 0;
    }
    
    public void setItem(Item item, int count)
    {
        if(item instanceof Weapon)
            image = WEAPON_SLOT;
        else if(item instanceof Armour)
            image = ARMOUR_SLOT;
        else
            image = EMPTY_SLOT;
        quantity = count;
        
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        int textWidth = g.getFontMetrics().stringWidth(String.valueOf(quantity));
        if(quantity > 1)
            g.drawString(String.valueOf(quantity), getWidth() - textWidth - 15, 15);
    }
}