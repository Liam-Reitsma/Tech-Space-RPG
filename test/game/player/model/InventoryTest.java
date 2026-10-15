package game.player.model;

import game.items.Potion;
import game.items.PotionType;
import game.items.armour.Armour;
import game.items.armour.ArmourMaterial;
import game.items.armour.ArmourSlot;
import game.items.weapon.Weapon;
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
    public void swapTwoOccupiedSlots()
    {
        ItemStack[] slots = inventory.getInventorySlots();
        
        // Set up two different items in slots 0 and 1.
        slots[0].setItem(new Potion(PotionType.SMALL_HEALTH));
        slots[1].setItem(new Weapon(WeaponType.ENERGY_SWORD, 2));
        
        inventory.swapItems(0, 1);
        
        ItemStack[] result = inventory.getInventorySlots();
        
        assertSame(slots[1], result[0]);
        assertSame(slots[0], result[1]);
    }
    
    @Test
    public void swapOccupiedWithEmptySlot()
    {
        ItemStack[] slots = inventory.getInventorySlots();
        
        slots[0].setItem(new Armour(ArmourMaterial.FABRIC, ArmourSlot.SHINS));
        
        inventory.swapItems(0, 1);
        
        ItemStack[] result = inventory.getInventorySlots();
        
        assertSame(slots[0], result[1]);
        assertSame(slots[1], result[0]);
        assertNull(result[0].getItem());
    }
    
    @Test
    public void swapSameSlot()
    {
        ItemStack original = inventory.getInventorySlots()[0];
        
        inventory.swapItems(0, 0);
        
        assertSame(original, inventory.getInventorySlots()[0]);
    }
    
    @Test
    public void swapInvalidIndices()
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
}