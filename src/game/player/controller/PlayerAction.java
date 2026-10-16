package game.player.controller;

import game.items.EquipSlot;
import game.player.view.ItemSlot;

import java.awt.event.MouseEvent;

public interface PlayerAction
{
    void requestEquippedItemChange(EquipSlot slot);
    void requestSlotDragged(int index, MouseEvent e, ItemSlot dragSlot);
    void requestSlotDragged(EquipSlot slot, MouseEvent e, ItemSlot dragSlot);
    void requestSlotPressed(int index);
    void requestSlotReleased(int index, MouseEvent e, ItemSlot dragSlot);
    void requestSlotReleased(EquipSlot slot, MouseEvent e, ItemSlot dragSlot);
}