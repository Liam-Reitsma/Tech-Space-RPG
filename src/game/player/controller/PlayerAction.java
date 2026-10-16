package game.player.controller;

import game.items.EquipSlot;

public interface PlayerAction
{
    void requestEquippedItemChange(EquipSlot slot);
    void requestSlotDragged(int index);
    void requestSlotDragged(EquipSlot slot);
    void requestSlotPressed(int index);
    void requestSlotReleased(int index);
    void requestSlotReleased(EquipSlot slot);
}