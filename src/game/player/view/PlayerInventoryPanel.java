package game.player.view;

import static game.player.model.Inventory.INVENTORY_CAPACITY;

import game.frame.view.DragViewAction;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.player.controller.PlayerAction;
import game.player.model.ItemStack;
import game.player.model.SlotIdentity;

import javax.swing.*;
import java.awt.*;

public class PlayerInventoryPanel extends JPanel
{
    private static final Image INVENTORY_BACKGROUND = new ImageIcon("resources/images/Inventory_Screen.png").getImage();
    
    private final ItemSlot[] itemSlots;
    
    public PlayerInventoryPanel(PlayerAction playerAction, DragViewAction dragViewAction)
    {
        setLayout(new ScaledLayout());
        setBackground(Color.blue);
        
        itemSlots = new ItemSlot[INVENTORY_CAPACITY];
        LayoutBox[] verticalBoxes = new LayoutBox().splitVertical(50, 50);
        LayoutBox[] topBoxes = verticalBoxes[0].splitHorizontal(1, 1, 1, 1, 1, 1);
        LayoutBox[] botBoxes = verticalBoxes[1].splitHorizontal(1, 1, 1, 1, 1, 1);
        
        for(int vertical = 0; vertical < 2; vertical++)
        {
            for(int horizontal = 0; horizontal < 6; horizontal++)
            {
                int index = vertical * 6 + horizontal;
                SlotIdentity source = new SlotIdentity(index, null);
                ItemSlot itemSlot = new ItemSlot(source, 0, dragViewAction,
                        (destination) -> playerAction.requestSlotReleased(source, destination));
                if(vertical == 0)
                    add(itemSlot, topBoxes[horizontal].insets(10, 25));
                else
                    add(itemSlot, botBoxes[horizontal].insets(10, 25));
                    
                itemSlots[index] = itemSlot;
            }
        }
    }
    
    public void updateInventory(ItemStack[] itemStacks, boolean[] canEquip)
    {
        for(int i = 0; i < itemStacks.length; i++)
            if(itemStacks[i].getItem() == null)
                itemSlots[i].removeItem();
            else
                itemSlots[i].setItem(itemStacks[i].getItem(), itemStacks[i].getQuantity(), canEquip[i]);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(INVENTORY_BACKGROUND, 0, 0, getWidth(), getHeight(), null);
    }
}