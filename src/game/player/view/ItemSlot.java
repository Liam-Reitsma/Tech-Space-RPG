package game.player.view;

import game.items.EquipSlot;
import game.items.Item;
import game.items.armour.ArmourSlot;
import game.items.weapon.WeaponSlot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    
    private final EquipSlot equipSlot;
    
    public ItemSlot(ItemSlotPressed action)
    {
        image = EMPTY_SLOT;
        quantity = 0;
        equipSlot = null;
        
        MouseAdapter adapter = createMouseAdapter(action);
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }
    
    public ItemSlot(EquipSlot slot, int count, ItemSlotPressed action)
    {
        image = getImageFromSlot(slot);
        quantity = count;
        equipSlot = slot;
        
        repaint();
        
        MouseAdapter adapter = createMouseAdapter(action);
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }
    
    private MouseAdapter createMouseAdapter(ItemSlotPressed action)
    {
        return new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                action.onPressed();
            }
            
            @Override
            public void mouseDragged(MouseEvent e)
            {
            
            }
            
            @Override
            public void mouseReleased(MouseEvent e)
            {
            
            }
        };
    }
    
    public void setItem(Item item, int count)
    {
        image = new ImageIcon(item.getImageURL()).getImage();
        quantity = count;
        
        repaint();
    }
    
    public void removeItem()
    {
        image = getImageFromSlot(equipSlot);
        quantity = 0;
        
        repaint();
    }
    
    private Image getImageFromSlot(EquipSlot slot)
    {
        if(slot instanceof WeaponSlot weaponSlot && weaponSlot == WeaponSlot.MAIN_HAND)
            return MAIN_HAND_IMAGE;
        else if(slot instanceof ArmourSlot armourSlot)
            switch(armourSlot)
            {
                case HEAD ->    { return HELMET_IMAGE; }
                case CHEST ->   { return CHESTPLATE_IMAGE; }
                case ARMS ->    { return ARMS_IMAGE; }
                case THIGHS ->  { return THIGHS_IMAGE; }
                case SHINS ->   { return SHINS_IMAGE; }
                case BOOTS ->   { return BOOTS_IMAGE; }
            }
            
        return EMPTY_SLOT;
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