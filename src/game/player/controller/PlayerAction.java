package game.player.controller;

import game.items.EquipSlot;

public interface PlayerAction
{
    void requestEquippedItemChange(EquipSlot slot);
}