/*
This was an attempt in restructuring the Player class but got lost in the sauce,
so I tried making the PlayerController related stuff.

package game.entities.player;

import game.entities.Persistable;
import game.entities.SaveExclude;
import game.entities.StatType;
import game.items.Item;
import game.items.armour.Armour;
import game.items.weapon.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerEquipment implements Persistable
{
    //need to move inventory, equipped, and equippedWeapon here
    //need logic like attack and takeDamage here
    @SaveExclude
    private static final int MAXIMUM_INVENTORY_SIZE = 10;

    private List<Item> inventory;
    private List<Armour> equipped;
    private Weapon equippedWeapon;

    public PlayerEquipment(ClassType playerType,  int level)
    {
        equippedWeapon = new Weapon(playerType.getWeapon(), level);
        setEquipped(null);
        setInventory(null);
    }

    public PlayerEquipment(String[] data)
    {
        loadSaveInfo(data);
    }

    public void setWeapon(Weapon weapon) { equippedWeapon = weapon; }

    public void setInventory(List<Item> newInventory)
    {
        if(newInventory == null) inventory = new ArrayList<>();
        else inventory = newInventory;
    }

    public void setEquipped(List<Armour> newEquipped)
    {
        if(newEquipped == null) equipped = new ArrayList<>();
        else equipped = newEquipped;
    }

    public List<Item> getInventory() { return inventory; }

    public List<Armour> getEquipped() { return equipped; }

    public Weapon getEquippedWeapon()
    {
        return equippedWeapon;
    }

    public Float calculatePlayerDamage(Map<StatType, Integer> statTypeMap)
    {
        return equippedWeapon.calculateDamage(statTypeMap.get(equippedWeapon.getScalingStat()));
    }

    public float getTotalArmour()
    {
        float totalArmour = 0;
        for(Armour armour : equipped)
        {
            totalArmour += (1 - totalArmour) * armour.calculateProtection();
        }
        return totalArmour;
    }

    public boolean equipWeapon(Weapon weapon)
    {
        if(!inventory.contains(weapon)) return false;
        else
        {
            if(equippedWeapon != null)
            {
                equippedWeapon.unequip();
                inventory.add(equippedWeapon);
                equippedWeapon = weapon;
                inventory.remove(weapon);
                weapon.equip();
                return true;
            }

            equippedWeapon = weapon;
            weapon.equip();
            return true;
        }
    }

    public boolean equipArmour(Armour armour)
    {
        if(!inventory.contains(armour)) return false;
        else
        {
            for(int  i = 0; i < equipped.size(); i++)
            {
                if(equipped.get(i).getArmourSlot() == armour.getArmourSlot())
                {
                    Armour currentArmour = equipped.get(i);
                    currentArmour.unequip();
                    inventory.add(currentArmour);
                    equipped.set(i, armour);
                    inventory.remove(armour);
                    armour.equip();
                    return true;
                }
            }

            equipped.add(armour);
            armour.equip();
            return true;
        }
    }

    public boolean unequipItem(Armour item)
    {
        if (!equipped.contains(item))
        {
            System.out.println("Item is not equipped");
            return false;
        }
        equipped.remove(item);
        item.unequip();
        inventory.add(item);
        return true;
    }

    public void removeFromInventory(Item item)
    {
        if (!inventory.contains(item))
        {
            // System.out.println("Item is not in inventory"); will need to use the consoleH like other class
        }
        inventory.remove(item);
    }

    public boolean addToInventory(Item item)
    {
        if(item == null)
        {
            //System.out.println("Add an Item"); will need to use the consoleH like other class
            return false;
        }
        else if (inventory.size() >= MAXIMUM_INVENTORY_SIZE)
        {
            //System.out.println("Maximum Inventory Size has been reached"); will need to use the consoleH like other class
            return false;
        }
        inventory.add(item);
        return true;
    }
}*/
