package game.player.view;

import game.gui.common.scaledlayout.LayoutBox;
import game.gui.common.scaledlayout.ScaledLayout;
import game.items.EquipSlot;
import game.items.EquippableItem;
import game.items.armour.ArmourSlot;
import game.items.weapon.WeaponSlot;
import game.player.controller.PlayerAction;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerEquippedPanel extends JPanel
{
    private final static Image EQUIPPED_BACKGROUND = new ImageIcon("resources/images/Equipped_Screen.png").getImage();
    
    private final Map<EquipSlot, ItemSlot> equippedInventoryMap;
    
    public PlayerEquippedPanel(PlayerAction playerAction)
    {
        setLayout(new ScaledLayout());
        setBackground(Color.blue);
        
        equippedInventoryMap = new HashMap<>();
        EquipSlot[] equipSlots = new EquipSlot[]{WeaponSlot.MAIN_HAND, ArmourSlot.HEAD, ArmourSlot.CHEST, ArmourSlot.ARMS, ArmourSlot.THIGHS, ArmourSlot.SHINS, ArmourSlot.BOOTS};
        LayoutBox middleBox = new LayoutBox().splitHorizontal(40, 20, 40)[1];
        LayoutBox[] verticalBoxes = middleBox.splitVertical(1, 1, 1, 1, 1, 1, 1);
        
        for(int i = 0; i < equipSlots.length; i++)
        {
            int index = i;
            ItemSlot itemSlot = new ItemSlot(equipSlots[i], 0, () -> playerAction.requestSlotPressed(index), (e, dragSlot) -> playerAction.requestSlotDragged(equipSlots[index], e, dragSlot));
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
            itemSlot.setItem(item, 1);
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        g.drawImage(EQUIPPED_BACKGROUND, 0, 0, getWidth(), getHeight(), null);
    }
}