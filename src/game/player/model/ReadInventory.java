package game.player.model;

import game.items.EquipSlot;
import game.items.EquippableItem;
import game.items.Item;
import game.items.armour.Armour;
import game.items.weapon.Weapon;

public interface ReadInventory
{
    int getItemCount(Item item);
    int getItemIndex(Item item);
    ItemStack[] getInventorySlots();
    ItemStack getItemStack(int index);
    <I extends Item> I[] getAllItemTypeInInventory(Class<I> itemClass);
    
    EquipSlot[] getAllEquippedSlots();
    EquippableItem getEquippedItem(EquipSlot slot);
    Armour[] getEquippedArmour();
    Weapon getEquippedWeapon();
}