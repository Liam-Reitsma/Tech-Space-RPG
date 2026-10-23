package game.player.view;

import game.frame.view.DragViewAction;
import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.items.EquipSlot;
import game.items.EquippableItem;
import game.items.armour.ArmourSlot;
import game.items.weapon.WeaponSlot;
import game.player.controller.PlayerAction;
import game.player.model.SlotIdentity;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerEquippedPanel extends JPanel
{
    private static final Image EQUIPPED_BACKGROUND = new ImageIcon("resources/images/Equipped_Screen.png").getImage();
    
    private final Map<EquipSlot, ItemSlot> equippedInventoryMap;
    
    public PlayerEquippedPanel(PlayerAction playerAction, DragViewAction dragViewAction)
    {
        setLayout(new ScaledLayout());
        setBackground(Color.blue);
        
        equippedInventoryMap = new HashMap<>();
        EquipSlot[] equipSlots = new EquipSlot[]{WeaponSlot.MAIN_HAND, ArmourSlot.HEAD, ArmourSlot.CHEST, ArmourSlot.ARMS, ArmourSlot.THIGHS, ArmourSlot.SHINS, ArmourSlot.BOOTS};
        LayoutBox middleBox = new LayoutBox().splitHorizontal(40, 20, 40)[1];
        LayoutBox[] verticalBoxes = middleBox.splitVertical(1, 1, 1, 1, 1, 1, 1);
        
        for(int i = 0; i < equipSlots.length; i++)
        {
            SlotIdentity source = new SlotIdentity(null, equipSlots[i]);
            ItemSlot itemSlot = new ItemSlot(source, 0, dragViewAction,
                    (destination) -> playerAction.requestSlotReleased(source, destination));
            equippedInventoryMap.put(equipSlots[i], itemSlot);
            add(itemSlot, verticalBoxes[i].insets(0, 10));
        }
    }
    
    public void updateEquippedItem(EquipSlot slot, EquippableItem item)
    {
        ItemSlot itemSlot = equippedInventoryMap.get(slot);
        if(item == null)
            itemSlot.removeItem();
        else
            itemSlot.setItem(item, 1, true);
    }
    
    public void highlightSlots(EquipSlot slot, SlotHighlight highlight)
    {
        equippedInventoryMap.get(slot).flashHighlight(highlight);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(EQUIPPED_BACKGROUND, 0, 0, getWidth(), getHeight(), null);
    }
}