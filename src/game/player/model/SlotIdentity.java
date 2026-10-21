package game.player.model;

import static game.player.model.Inventory.INVENTORY_CAPACITY;
import game.items.EquipSlot;

public record SlotIdentity(Integer inventorySlot, EquipSlot equipSlot)
{
    public boolean isInvalid()
    {
        return !isValid();
    }
    
    public boolean isValid()
    {
        return ((inventorySlot == null) != (equipSlot == null)) &&
                (inventorySlot == null || (inventorySlot >= 0 && inventorySlot < INVENTORY_CAPACITY));
    }
}