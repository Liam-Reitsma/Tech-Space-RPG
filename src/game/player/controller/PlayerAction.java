package game.player.controller;

import game.items.EquipSlot;
import game.player.model.SlotIdentity;

public interface PlayerAction
{
    void requestSlotReleased(SlotIdentity source, SlotIdentity destination);
}