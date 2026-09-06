package game.entities;

import game.items.Armour;
import game.items.ArmourType;
import game.items.Item;
import game.items.Weapon;

import java.util.*;

public class Player extends Entities implements Persistable
{
    private int xp;
    private int gold;
    private int statStr;
    private int statDex;
    private int statInt;
    @SaveExclude
    private List<Item> inventory;
    @SaveExclude
    private List<Item> equipped;
    
    private ClassType classType;
    
    public Player(String playerName, ClassType type)
    {
        xp = 0;
        gold = 0;
        name = playerName;
        classType = type;
        statStr = type.getStatStrength();
        statDex = type.getStatDexterity();
        statInt = type.getStatIntelligence();
        inventory = new ArrayList<>();
        equipped = new ArrayList<>();
    }
    
    public Player(String[] data)
    {
        loadSaveInfo(data);
    }
    
    @Override
    public void attack()
    {
    
    }
    
    @Override
    public void useAbility()
    {
    
    }
    
    @Override
    public void dealDamage()
    {
    
    }
    @Override
    public void takeDamage()
    {
    
    }
    
    public int getLevel() { return level; }
    
    public int getXP() { return xp; }
    
    public int getGold() { return gold; }
    
    public String getName()
    {
        return name;
    }
    
    public String getClassType()
    {
        return classType.toString();
    }
    
    public int[] getStats()
    {
        return new int[]{statStr, statDex, statInt};
    }
    
    public String getStatsToString()
    {
        return "STR: " + statStr + " | DEX: " + statDex + " | INT: " + statInt;
    }
    
    public void addStat(StatChange[] stats)
    {
        for(StatChange stat : stats)
        {
            switch(stat.statType)
            {
                case STR:
                    statStr += stat.change;
                    break;
                case DEX:
                    statDex += stat.change;
                    break;
                case INT:
                    statInt += stat.change;
                    break;
                default:
                    break;
            }
        }
    }

    public boolean equipWeapon(Weapon weapon)
    {
        if (!inventory.contains(weapon))
        {
            System.out.println("Weapon is not in inventory");
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
            System.out.println("Armour is not in inventory");
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
}
