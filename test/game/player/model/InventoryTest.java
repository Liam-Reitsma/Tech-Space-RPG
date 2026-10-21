package game.player.model;

import static game.player.model.Inventory.INVENTORY_CAPACITY;
import static game.player.model.InventoryResult.*;
import static org.junit.Assert.*;

import game.items.EquippableItem;
import game.items.Potion;
import game.items.PotionType;
import game.items.weapon.Weapon;
import game.items.weapon.WeaponType;
import org.junit.Test;


public class InventoryTest
{
    private final Inventory inventory;
    
    public InventoryTest()
    {
        inventory = new Inventory();
    }
    
    @Test
    public void testEquipItemIndexOutOfBounds()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 1);
        
        assertEquals(INDEX_OUT_OF_BOUNDS, inventory.equipItem(-1, weapon.getEquipSlot()));
        assertEquals(INDEX_OUT_OF_BOUNDS, inventory.equipItem(INVENTORY_CAPACITY, weapon.getEquipSlot()));
    }
    
    @Test
    public void testEquipItemInvalidItem()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 1);
        Potion potion = new Potion(PotionType.SMALL_HEALTH);
        
        assertEquals(INVALID_ITEM, inventory.equipItem(null, weapon.getEquipSlot()));
        assertEquals(INVALID_ITEM, inventory.equipItem(potion, weapon.getEquipSlot()));
    }
    
    @Test
    public void testEquipItemInvalidSlot()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 1);
        
        assertTrue(inventory.addToInventory(weapon));
        
        assertEquals(INVALID_SLOT, inventory.equipItem(weapon, null));
        
        assertEquals(1, inventory.getItemCount(weapon));
        assertNull(inventory.getEquippedWeapon());
    }
    
    @Test
    public void testEquipItemMissingItem()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 1);
        
        assertEquals(MISSING_ITEM, inventory.equipItem(weapon, weapon.getEquipSlot()));
        
        assertNull(inventory.getEquippedWeapon());
        assertEquals(0, inventory.getItemCount(weapon));
    }
    
    @Test
    public void testEquipItemInventoryFull()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 1);
        
        assertTrue(inventory.addToInventory(weapon));
        assertEquals(SUCCESS, inventory.equipItem(weapon, weapon.getEquipSlot()));
        
        for(int level = 2; level <= INVENTORY_CAPACITY + 1; level++)
            assertTrue(inventory.addToInventory(new Weapon(WeaponType.ENERGY_SWORD, level)));
        
        Weapon replacement = new Weapon(WeaponType.ENERGY_SWORD, INVENTORY_CAPACITY + 1);
        assertTrue(inventory.addToInventory(replacement));
        
        ItemStack[] originalSlots = inventory.getInventorySlots();
        
        assertEquals(INVENTORY_FULL, inventory.equipItem(replacement, replacement.getEquipSlot()));
        
        assertArrayEquals(originalSlots, inventory.getInventorySlots());
        assertEquals(weapon, inventory.getEquippedWeapon());
        assertEquals(2, inventory.getItemCount(replacement));
    }
    
    @Test
    public void testEquipItemSuccessEmptySlot()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 1);
        
        assertTrue(inventory.addToInventory(weapon));
        
        assertEquals(SUCCESS, inventory.equipItem(weapon, weapon.getEquipSlot()));
        
        assertEquals(weapon, inventory.getEquippedWeapon());
        assertEquals(0, inventory.getItemCount(weapon));
    }
    
    @Test
    public void testEquipItemSuccessReplaceEquipped()
    {
        Weapon original = new Weapon(WeaponType.ENERGY_SWORD, 1);
        Weapon replacement = new Weapon(WeaponType.ENERGY_SWORD, 2);
        
        assertTrue(inventory.addToInventory(original));
        assertEquals(SUCCESS, inventory.equipItem(original, original.getEquipSlot()));
        
        assertTrue(inventory.addToInventory(replacement));
        
        assertEquals(SUCCESS, inventory.equipItem(replacement, replacement.getEquipSlot()));
        
        assertEquals(replacement, inventory.getEquippedWeapon());
        assertEquals(1, inventory.getItemCount(original));
        assertEquals(0, inventory.getItemCount(replacement));
    }
    
    @Test
    public void testUnequipItemInvalidItem()
    {
        assertEquals(INVALID_ITEM, inventory.unequipItem((EquippableItem)null, 0));
    }
    
    @Test
    public void testUnequipItemMissingItem()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 1);
        
        assertEquals(MISSING_ITEM, inventory.unequipItem(weapon, 0));
        
        assertNull(inventory.getEquippedWeapon());
    }
    
    @Test
    public void testUnequipItemDifferentEquippedItem()
    {
        Weapon original = new Weapon(WeaponType.ENERGY_SWORD, 1);
        Weapon different = new Weapon(WeaponType.ENERGY_SWORD, 2);
        
        assertTrue(inventory.addToInventory(original));
        assertEquals(SUCCESS, inventory.equipItem(original, original.getEquipSlot()));
        
        assertEquals(MISSING_ITEM, inventory.unequipItem(different, 0));
        
        assertEquals(original, inventory.getEquippedWeapon());
    }
    
    @Test
    public void testUnequipItemIndexOutOfBounds()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 1);
        
        assertTrue(inventory.addToInventory(weapon));
        assertEquals(SUCCESS, inventory.equipItem(weapon, weapon.getEquipSlot()));
        
        assertEquals(INDEX_OUT_OF_BOUNDS, inventory.unequipItem(weapon, -1));
        assertEquals(INDEX_OUT_OF_BOUNDS, inventory.unequipItem(weapon.getEquipSlot(), INVENTORY_CAPACITY));
        
        assertEquals(weapon, inventory.getEquippedWeapon());
    }
    
    @Test
    public void testUnequipItemSuccessSwap()
    {
        Weapon original = new Weapon(WeaponType.ENERGY_SWORD, 1);
        Weapon replacement = new Weapon(WeaponType.ENERGY_SWORD, 2);
        
        assertTrue(inventory.addToInventory(original));
        assertEquals(SUCCESS, inventory.equipItem(original, original.getEquipSlot()));
        
        assertTrue(inventory.addToInventory(replacement));
        
        assertEquals(SUCCESS, inventory.unequipItem(
                original,
                inventory.getItemIndex(replacement)));
        
        assertEquals(replacement, inventory.getEquippedWeapon());
        assertEquals(1, inventory.getItemCount(original));
        assertEquals(0, inventory.getItemCount(replacement));
    }
    
    @Test
    public void testUnequipItemSuccessEmptySlot()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 1);
        
        assertTrue(inventory.addToInventory(weapon));
        assertEquals(SUCCESS, inventory.equipItem(weapon, weapon.getEquipSlot()));
        
        assertEquals(SUCCESS, inventory.unequipItem(weapon, 0));
        
        assertNull(inventory.getEquippedWeapon());
        assertEquals(weapon, inventory.getItemStack(0).getItem());
        assertEquals(1, inventory.getItemCount(weapon));
    }
    
    @Test
    public void testUnequipItemInventoryFull()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 1);
        Potion potion = new Potion(PotionType.SMALL_HEALTH);
        
        assertTrue(inventory.addToInventory(weapon));
        assertEquals(SUCCESS, inventory.equipItem(weapon, weapon.getEquipSlot()));
        
        assertTrue(inventory.addToInventory(potion));
        
        for(int level = 2; level <= INVENTORY_CAPACITY; level++)
            assertTrue(inventory.addToInventory(new Weapon(WeaponType.ENERGY_SWORD, level)));
        
        ItemStack[] originalSlots = inventory.getInventorySlots();
        
        assertEquals(INVENTORY_FULL, inventory.unequipItem(weapon, 0));
        
        assertArrayEquals(originalSlots, inventory.getInventorySlots());
        assertEquals(weapon, inventory.getEquippedWeapon());
        assertEquals(0, inventory.getItemCount(weapon));
    }
}