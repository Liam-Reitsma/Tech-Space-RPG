package game.player.view;

import game.items.EquipSlot;
import game.items.Item;
import game.items.armour.ArmourSlot;
import game.items.weapon.WeaponSlot;

import javax.swing.*;
import java.awt.*;

public class ItemSlot extends JPanel
{
    private static final Image EMPTY_SLOT = new ImageIcon("resources/images/inventory/Empty.png").getImage();
    private static final Image MAIN_HAND_IMAGE = new ImageIcon("resources/images/inventory/Main_Hand.png").getImage();
    private static final Image HELMET_IMAGE = new ImageIcon("resources/images/inventory/Helmet.png").getImage();
    private static final Image CHESTPLATE_IMAGE = new ImageIcon("resources/images/inventory/Chestplate.png").getImage();
    private static final Image ARMS_IMAGE = new ImageIcon("resources/images/inventory/Arms.png").getImage();
    private static final Image THIGHS_IMAGE = new ImageIcon("resources/images/inventory/Thighs.png").getImage();
    private static final Image SHINS_IMAGE = new ImageIcon("resources/images/inventory/Shins.png").getImage();
    private static final Image BOOTS_IMAGE = new ImageIcon("resources/images/inventory/Boots.png").getImage();
    
    private Image image;
    private int quantity;
    
    public ItemSlot()
    {
        image = EMPTY_SLOT;
        quantity = 0;
    }
    
    public ItemSlot(EquipSlot slot, int count)
    {
        if(slot instanceof WeaponSlot weaponSlot && weaponSlot == WeaponSlot.MAIN_HAND)
                image = MAIN_HAND_IMAGE;
        else if(slot instanceof ArmourSlot armourSlot)
            switch(armourSlot)
            {
                case HEAD -> image = HELMET_IMAGE;
                case CHEST -> image = CHESTPLATE_IMAGE;
                case ARMS -> image = ARMS_IMAGE;
                case THIGHS -> image = THIGHS_IMAGE;
                case SHINS -> image = SHINS_IMAGE;
                case BOOTS -> image = BOOTS_IMAGE;
            }
        else
            image = EMPTY_SLOT;
        quantity = count;
        
        repaint();
    }
    
    public void setItem(Item item, int count)
    {
        //TODO get image from item;
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