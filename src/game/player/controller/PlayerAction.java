package game.player.controller;

import game.items.EquipSlot;
import game.player.model.SlotIdentity;

public interface PlayerAction
{
    void requestEquippedItemChange(EquipSlot slot);
    void requestSlotReleased(SlotIdentity source, SlotIdentity destination);
}