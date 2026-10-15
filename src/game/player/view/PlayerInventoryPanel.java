package game.player.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.items.EquipSlot;
import game.items.armour.ArmourSlot;
import game.items.weapon.WeaponSlot;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerInventoryPanel extends JPanel
{
    private final static Image INVENTORY_BACKGROUND = new ImageIcon("resources/images/Inventory_Screen.png").getImage();
    
    private final Map<EquipSlot, ItemSlot> inventoryMap;
    
    public PlayerInventoryPanel()
    {
        setLayout(new ScaledLayout());
        setBackground(Color.blue);
        
        inventoryMap = new HashMap<>();
        LayoutBox[] verticalBoxes = new LayoutBox().splitVertical(50, 50);
        LayoutBox[] topBoxes = verticalBoxes[0].splitHorizontal(1, 1, 1, 1, 1, 1);
        LayoutBox[] botBoxes = verticalBoxes[1].splitHorizontal(1, 1, 1, 1, 1, 1);
        
        for(int i = 0; i < 6; i++)
        {
            ItemSlot itemSlot = new ItemSlot();
            add(itemSlot, topBoxes[i].insets(10, 25));
        }
        for(int i = 0; i < 6; i++)
        {
            ItemSlot itemSlot = new ItemSlot();
            add(itemSlot, botBoxes[i].insets(10, 25));
        }
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(INVENTORY_BACKGROUND, 0, 0, getWidth(), getHeight(), null);
    }
}