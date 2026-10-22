package game.player.model;

import game.items.Potion;
import game.items.PotionType;
import game.items.armour.Armour;
import game.items.armour.ArmourMaterial;
import game.items.armour.ArmourSlot;
import game.items.weapon.Weapon;
import game.items.weapon.WeaponSlot;
import game.items.weapon.WeaponType;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest
{
    private final Inventory inventory;
    
    public InventoryTest()
    {
        inventory = new Inventory();
    }
    
    @Test
    public void testSwapItemsTwoSlots()
    {
        ItemStack[] slots = inventory.getInventorySlots();
        
        slots[0].setItem(new Potion(PotionType.SMALL_HEALTH));
        slots[1].setItem(new Weapon(WeaponType.ENERGY_SWORD, 2));
        
        inventory.swapItems(0, 1);
        
        ItemStack[] result = inventory.getInventorySlots();
        
        assertSame(slots[1], result[0]);
        assertSame(slots[0], result[1]);
    }
    
    @Test
    public void testSwapItemsInvalidIndices()
    {
        ItemStack[] original = inventory.getInventorySlots();
        
        inventory.swapItems(-1, 0);
        inventory.swapItems(0, -1);
        inventory.swapItems(Inventory.INVENTORY_CAPACITY, 0);
        inventory.swapItems(0, Inventory.INVENTORY_CAPACITY);
        
        ItemStack[] result = inventory.getInventorySlots();
        
        for(int i = 0; i < original.length; i++)
            assertSame(original[i], result[i]);
    }
    
    @Test
    public void testEquipItemIntoEmptySlot()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 2);
        
        inventory.addToInventory(weapon);
        
        assertTrue(inventory.equipItem(weapon, WeaponSlot.MAIN_HAND));
        assertSame(weapon, inventory.getEquippedItem(WeaponSlot.MAIN_HAND));
        assertEquals(0, inventory.getItemCount(weapon));
    }
    
    @Test
    public void testEquipItemToReplaceEquippedItem()
    {
        Weapon original = new Weapon(WeaponType.ENERGY_SWORD, 2);
        Weapon replacement = new Weapon(WeaponType.SHOCK_HAMMER, 2);
        
        inventory.addToInventory(original);
        inventory.equipItem(original, WeaponSlot.MAIN_HAND);
        inventory.addToInventory(replacement);
        
        assertTrue(inventory.equipItem(replacement, WeaponSlot.MAIN_HAND));
        
        assertSame(replacement, inventory.getEquippedItem(WeaponSlot.MAIN_HAND));
        assertEquals(1, inventory.getItemCount(original));
        assertEquals(0, inventory.getItemCount(replacement));
        
        // The returned item occupies the original source slot.
        assertSame(original, inventory.getInventorySlots()[0].getItem());
    }
    
    @Test
    public void testEquipItemToReplaceEquippedItemWithEquippedItemStack()
    {
        Weapon original = new Weapon(WeaponType.ENERGY_SWORD, 2);
        Weapon replacement = new Weapon(WeaponType.SHOCK_HAMMER, 2);
        
        inventory.addToInventory(original);
        inventory.equipItem(original, WeaponSlot.MAIN_HAND);
        
        inventory.addToInventory(replacement);
        inventory.addToInventory(replacement);
        
        int sourceIndex = inventory.getItemIndex(replacement);
        
        assertTrue(inventory.equipItem(replacement, WeaponSlot.MAIN_HAND));
        
        assertSame(replacement, inventory.getEquippedItem(WeaponSlot.MAIN_HAND));
        assertEquals(1, inventory.getItemCount(replacement));
        assertEquals(1, inventory.getItemCount(original));
        assertSame(replacement, inventory.getInventorySlots()[sourceIndex].getItem());
    }
    
    @Test
    public void testEquipItemToReplaceEquippedItemWithOriginalItemStack()
    {
        Weapon original = new Weapon(WeaponType.ENERGY_SWORD, 2);
        Weapon replacement = new Weapon(WeaponType.SHOCK_HAMMER, 2);
        
        inventory.addToInventory(original);
        inventory.addToInventory(original);
        inventory.equipItem(original, WeaponSlot.MAIN_HAND);
        
        inventory.addToInventory(replacement);
        
        assertTrue(inventory.equipItem(replacement, WeaponSlot.MAIN_HAND));
        
        assertSame(replacement, inventory.getEquippedItem(WeaponSlot.MAIN_HAND));
        assertEquals(2, inventory.getItemCount(original));
        assertEquals(0, inventory.getItemCount(replacement));
    }
    
    @Test
    public void testEquipItemFromSameItemStack()
    {
        Armour armour = new Armour(ArmourMaterial.FABRIC, ArmourSlot.SHINS);
        
        inventory.addToInventory(armour);
        inventory.addToInventory(armour);
        
        assertTrue(inventory.equipItem(armour, ArmourSlot.SHINS));
        
        assertSame(armour, inventory.getEquippedItem(ArmourSlot.SHINS));
        assertEquals(1, inventory.getItemCount(armour));
    }
    
    @Test
    public void testEquipItemWithIncompatibleEquipmentSlot()
    {
        Armour armour = new Armour(ArmourMaterial.FABRIC, ArmourSlot.SHINS);
        
        inventory.addToInventory(armour);
        
        assertFalse(inventory.equipItem(armour, ArmourSlot.HEAD));
        assertEquals(1, inventory.getItemCount(armour));
        assertNull(inventory.getEquippedItem(ArmourSlot.HEAD));
    }
    
    @Test
    public void testEquipItemWithNonEquippableItem()
    {
        Potion potion = new Potion(PotionType.SMALL_HEALTH);
        
        inventory.addToInventory(potion);
        
        assertFalse(inventory.equipItem(potion, WeaponSlot.MAIN_HAND));
        assertEquals(1, inventory.getItemCount(potion));
        assertNull(inventory.getEquippedItem(WeaponSlot.MAIN_HAND));
    }
    
    @Test
    public void testEquipItemWithItemNotInInventory()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 2);
        
        assertFalse(inventory.equipItem(weapon, WeaponSlot.MAIN_HAND));
        assertNull(inventory.getEquippedItem(WeaponSlot.MAIN_HAND));
    }
    
    @Test
    public void testUnequipItemIntoEmptySlot()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 2);
        
        inventory.addToInventory(weapon);
        inventory.equipItem(weapon, WeaponSlot.MAIN_HAND);
        
        assertTrue(inventory.unequipItem(weapon, 3));
        assertNull(inventory.getEquippedItem(WeaponSlot.MAIN_HAND));
        assertSame(weapon, inventory.getInventorySlots()[3].getItem());
    }
    
    @Test
    public void testUnequipItemToReplaceEquippedItem()
    {
        Weapon equipped = new Weapon(WeaponType.ENERGY_SWORD, 2);
        Weapon replacement = new Weapon(WeaponType.SHOCK_HAMMER, 2);
        
        inventory.addToInventory(equipped);
        inventory.equipItem(equipped, WeaponSlot.MAIN_HAND);
        inventory.addToInventory(replacement);
        
        int targetIndex = inventory.getItemIndex(replacement);
        
        assertTrue(inventory.unequipItem(equipped, targetIndex));
        assertSame(replacement, inventory.getEquippedItem(WeaponSlot.MAIN_HAND));
        assertEquals(1, inventory.getItemCount(equipped));
        assertEquals(0, inventory.getItemCount(replacement));
    }
    
    @Test
    public void testUnequipItemWithIncompatibleTargetItem()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 2);
        Potion potion = new Potion(PotionType.SMALL_HEALTH);
        
        inventory.addToInventory(weapon);
        inventory.equipItem(weapon, WeaponSlot.MAIN_HAND);
        inventory.addToInventory(potion);
        
        int targetIndex = inventory.getItemIndex(potion);
        
        assertTrue(inventory.unequipItem(weapon, targetIndex));
        assertNull(inventory.getEquippedItem(WeaponSlot.MAIN_HAND));
        assertEquals(1, inventory.getItemCount(weapon));
        assertEquals(1, inventory.getItemCount(potion));
    }
    
    @Test
    public void testUnequipItemWithExistingItemStack()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 2);
        Potion potion = new Potion(PotionType.SMALL_HEALTH);
        
        inventory.addToInventory(weapon);
        inventory.addToInventory(weapon);
        inventory.equipItem(weapon, WeaponSlot.MAIN_HAND);
        inventory.addToInventory(potion);
        
        int originalIndex = inventory.getItemIndex(weapon);
        int targetIndex = inventory.getItemIndex(potion);
        
        assertTrue(inventory.unequipItem(weapon, targetIndex));
        assertNull(inventory.getEquippedItem(WeaponSlot.MAIN_HAND));
        assertEquals(2, inventory.getItemCount(weapon));
        assertSame(weapon, inventory.getInventorySlots()[originalIndex].getItem());
        assertSame(potion, inventory.getInventorySlots()[targetIndex].getItem());
    }
    
    @Test
    public void testUnequipItemWithNullItem()
    {
        assertFalse(inventory.unequipItem(null, 0));
    }
    
    @Test
    public void testUnequipItemWithItemNotEquipped()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 2);
        
        inventory.addToInventory(weapon);
        
        assertFalse(inventory.unequipItem(weapon, 0));
        assertEquals(1, inventory.getItemCount(weapon));
    }
    
    @Test
    public void testUnequipItemWithInvalidTargetIndex()
    {
        Weapon weapon = new Weapon(WeaponType.ENERGY_SWORD, 2);
        
        inventory.addToInventory(weapon);
        inventory.equipItem(weapon, WeaponSlot.MAIN_HAND);
        
        assertFalse(inventory.unequipItem(weapon, -1));
        assertFalse(inventory.unequipItem(weapon, Inventory.INVENTORY_CAPACITY));
        
        assertSame(weapon, inventory.getEquippedItem(WeaponSlot.MAIN_HAND));
        assertEquals(0, inventory.getItemCount(weapon));
    }
}