package game.entities;

import game.items.*;

import java.util.*;

public class Player extends Entities implements Persistable
{
    @SaveExclude
    private static final int MAXIMUM_INVENTORY_SIZE = 10;
    @SaveExclude
    private int xpToLevelUp;
    private int xp;
    private int gold;
    private int statStr;
    private int statDex;
    private int statInt;
    @SaveExclude
    private List<Item> inventory;
    @SaveExclude
    private List<EquippableItem> equipped;

    private ClassType classType;

    public Player(String playerName, ClassType type)
    {
        health = 100;
        level = 1;
        xp = 0;
        gold = 0;
        name = playerName;
        classType = type;
        statStr = type.getStatStrength();
        statDex = type.getStatDexterity();
        statInt = type.getStatIntelligence();

        xpToLevelUp = 15 * level;
    }

    public Player(String[] data)
    {
        loadSaveInfo(data);
    }

    public void setInventory(List<Item> newInventory)
    {
        if(newInventory == null) inventory = new ArrayList<>();
        else inventory = newInventory;
    }

    public void setEquipped(List<EquippableItem> newEquipped)
    {
        if(newEquipped == null) equipped = new ArrayList<>();
        else equipped = newEquipped;
    }


    @Override
    public void attack()
    {

    }

    @Override
    public void useAbility()
    {

    }

    public void healing(int healAmount)
    {
        if (healAmount + health > 100) {
            health = 100;
        }
        else{
            health += healAmount;
        }
    }

    @Override
    public void takeDamage(int ouch)
    {
        double damageReceived = ouch;
        for (Item item : equipped)
        {
            if (item instanceof Armour)
            {
                damageReceived *= (1.0 - ((Armour) item).getArmourType().getProtectionValue());
            }
        }
        health -= Math.round(damageReceived);
    }

    public int getLevel() { return level; }

    public float getHealth() { return health; }

    public int getXP() { return xp; }

    public void applyXP(int xp)
    {
        this.xp += xp;
        while (this.xp >= xpToLevelUp)
        {
            level++;
            xp -= xpToLevelUp;
            xpToLevelUp *= level;
        }
    }


    public int getGold() { return gold; }

    public void addGold(int gold)
    {
        this.gold += gold;
    }

    public String getName()
    {
        return name;
    }

    public ClassType getClassType()
    {
        return classType;
    }

    public int[] getStats()
    {
        return new int[]{statStr, statDex, statInt};
    }

    public String getStatsToString()
    {
        return "STR: " + statStr + " | DEX: " + statDex + " | INT: " + statInt;
    }

    public void addStat(StatType stat)
    {
        switch(stat)
        {
            case STR -> statStr++;
            case DEX -> statDex++;
            case INT -> statInt++;
        }
    }

    public boolean equipWeapon(Weapon weapon)
    {
        if (!inventory.contains(weapon))
        {
            System.out.println("The weapon is not in inventory");
            return false;
        }
        else
        {
            for (int  i = 0; i < equipped.size(); i++)
            {
                if (equipped.get(i) instanceof Weapon)
                {
                    Weapon replacedWeapon = (Weapon) equipped.get(i);
                    replacedWeapon.unequip();
                    inventory.add(replacedWeapon);
                    equipped.set(i, weapon);
                    inventory.remove(weapon);
                    weapon.equip();
                    return true;
                }
            }
            equipped.add(weapon);
            weapon.equip();
            return true;
        }
    }

    public boolean equipArmour(Armour armour)
    {
        if (!inventory.contains(armour))
        {
            System.out.println("The armour is not in inventory");
            return false;
        }
        else
        {
            for (int  i = 0; i < equipped.size(); i++)
            {
                if (equipped.get(i) instanceof Armour && ((Armour) equipped.get(i)).getArmourType() == armour.getArmourType())
                {
                    Armour replacedArmour = (Armour) equipped.get(i);
                    replacedArmour.unequip();
                    inventory.add(replacedArmour);
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

    public boolean addToInventory(Item item)
    {
        if (item == null)
        {
            System.out.println("Add an Item");
            return false;
        }
        else if (inventory.size() >= MAXIMUM_INVENTORY_SIZE)
        {
            System.out.println("Maximum Inventory Size has been reached");
            return false;
        }
        inventory.add(item);
        return true;
    }

    public boolean unequipItem(EquippableItem item)
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
            System.out.println("Item is not in inventory");
        }
        inventory.remove(item);
    }

    public Optional<Weapon> getEquippedWeapon()
    {
        for (Item item : equipped) {
            if (item instanceof Weapon) {
                return Optional.of((Weapon) item);
            }
        }
        return Optional.empty();
    }

    public List<Item> getInventory()
    {
        return inventory;
    }

    public List<EquippableItem> getEquipped()
    {
        return equipped;
    }
}
