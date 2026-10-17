package game.player.view;

import static game.player.view.SlotHighlight.*;

import game.frame.view.DragViewAction;
import game.items.EquipSlot;
import game.items.Item;
import game.items.armour.ArmourSlot;
import game.items.weapon.WeaponSlot;
import game.player.model.SlotIdentity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItemSlot extends JPanel
{
    private static final int STROKE_SIZE = 4;
    private static final int FLASH_INTERVAL = 200;
    private static final int FLASH_COUNT = 4;
    
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
    private boolean isDragging;
    private SlotHighlight slotHighlight;
    private SlotHighlight flashHighlight;
    
    private int flashCounter;
    
    private final ItemSlot dragSlot;
    private final SlotIdentity slotIdentity;
    private final DragViewAction dragViewAction;
    private final Timer flashTimer;
    
    public ItemSlot(ItemSlot itemSlot)
    {
        image = itemSlot.image;
        quantity = itemSlot.quantity;
        isDragging = false;
        slotHighlight = NONE;
        dragSlot = null;
        slotIdentity = itemSlot.slotIdentity;
        dragViewAction = null;
        flashTimer = new Timer(FLASH_INTERVAL, null);
    }
    
    public ItemSlot(SlotIdentity slotType, int count, DragViewAction dvAction, ItemSlotReleased released)
    {
        image = getImageFromSlot(slotType.equipSlot());
        quantity = count;
        isDragging = false;
        slotHighlight = NONE;
        slotIdentity = slotType;
        dragViewAction = dvAction;
        dragSlot = new ItemSlot(this);
        flashTimer = new Timer(FLASH_INTERVAL, null);
        flashTimer.addActionListener(e ->
        {
            setSlotHighlight(slotHighlight == NONE ? flashHighlight : NONE);
            
            if(++flashCounter >= FLASH_COUNT * 2)
            {
                flashTimer.stop();
                setSlotHighlight(NONE);
            }
        });
        
        repaint();
        
        if(slotType.isValid())
        {
            MouseAdapter adapter = createMouseAdapter(released);
            addMouseListener(adapter);
            addMouseMotionListener(adapter);
        }
    }
    
    private MouseAdapter createMouseAdapter(ItemSlotReleased released)
    {
        return new MouseAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if(!isDragging)
                {
                    isDragging = true;
                    repaint();
                }
                
                dragViewAction.displayOnOverlay(e, dragSlot);
            }
            
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if(isDragging)
                {
                    isDragging = false;
                    repaint();
                    dragViewAction.removeFromOverlay(dragSlot);
                    Component component = dragViewAction.getDropTarget(e);
                    if(component instanceof ItemSlot newItemSlot)
                        released.onRelease(newItemSlot.slotIdentity);
                    else
                        released.onRelease(null);
                }
            }
        };
    }
    
    public void setItem(Item item, int count)
    {
        image = new ImageIcon(item.getImageURL()).getImage();
        quantity = count;
        dragSlot.image = image;
        dragSlot.quantity = quantity;
        
        repaint();
    }
    
    public void removeItem()
    {
        image = getImageFromSlot(slotIdentity.equipSlot());
        quantity = 0;
        dragSlot.image = image;
        dragSlot.quantity = quantity;
        
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
        
        if(isDragging)
            g.drawImage(EMPTY_SLOT, 0, 0, getWidth(), getHeight(), null);
        else
        {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            int textWidth = g.getFontMetrics().stringWidth(String.valueOf(quantity));
            if(quantity > 1)
            {
                g.setColor(Color.WHITE);
                g.drawString(String.valueOf(quantity), getWidth() - textWidth - 10, 20);
            }
        }
        
        if(slotHighlight != NONE)
        {
            Graphics2D g2D = (Graphics2D)g.create();
            
            g2D.setColor(slotHighlight == VALID ? new Color(0, 255, 0, 60) : new Color(255, 0, 0, 60));
            g2D.fillRect(0, 0, getWidth(), getHeight());
            
            g2D.setColor(slotHighlight == VALID ? Color.GREEN : Color.RED);
            g2D.setStroke(new BasicStroke(STROKE_SIZE));
            g2D.drawRect(STROKE_SIZE / 2, STROKE_SIZE / 2, getWidth() - STROKE_SIZE, getHeight() - STROKE_SIZE);
            
            g2D.dispose();
        }
    }
    
    public void setSlotHighlight(SlotHighlight highlight)
    {
        slotHighlight = highlight;
        repaint();
    }
    
    public void flashHighlight(SlotHighlight highlight)
    {
        flashTimer.stop();
        flashCounter = 0;
        flashHighlight = highlight;
        setSlotHighlight(NONE);
        
        flashTimer.setRepeats(true);
        flashTimer.start();
    }
}