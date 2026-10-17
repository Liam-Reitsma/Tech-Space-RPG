package game.player.controller;

import game.items.EquipSlot;
import game.player.model.SlotIdentity;

public interface PlayerAction
{
    void requestEquippedItemChange(EquipSlot slot);
    void requestSlotPressed(SlotIdentity source);
    void requestSlotReleased(SlotIdentity source, SlotIdentity destination);
}