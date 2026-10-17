package game.player.model;

import game.items.EquipSlot;

public record SlotIdentity(Integer inventorySlot, EquipSlot equipSlot)
{
    public SlotIdentity
    {
        if((inventorySlot == null) == (equipSlot == null))
        {
            inventorySlot = null;
            equipSlot = null;
        }
    }
    
    public boolean isValid() { return inventorySlot != null || equipSlot != null; }
}