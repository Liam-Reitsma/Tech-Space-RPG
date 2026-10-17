package game.player.view;

import game.items.EquipSlot;
import game.items.EquippableItem;
import game.player.model.ItemStack;
import game.player.model.StatEntry;

import java.util.List;

public interface PlayerViewAction
{
    void updatePlayerStats(List<StatEntry> statEntries);
    void updateEquippedItem(EquipSlot slot, EquippableItem item);
    void updateInventory(ItemStack[] itemStacks);
    void flashEquipmentSlot(EquipSlot incorrectSlot, EquipSlot correctSlot);
}