package game.entities.player;

import static game.entities.StatType.*;
import static game.entities.player.PlayerError.*;

import game.entities.*;
import game.items.*;
import game.items.armour.Armour;
import game.items.weapon.Weapon;

import java.util.*;

public class Player extends Entities implements Persistable
{
    @SaveExclude
    private static final int MAXIMUM_INVENTORY_SIZE = 10;
    @SaveExclude
    private static final int STARTING_LEVEL_XP = 15;
    @SaveExclude
    private static final float XP_GAIN_FACTOR = 1.5f;
    
    @SaveExclude
    private int xpToLevelUp;
    private int xp;
    private int gold;
    
    private Map<StatType, Integer> statTypeMap;
    
    @SaveExclude
    private List<Item> inventory;
    @SaveExclude
    private List<Armour> equipped;
    @SaveExclude
    private Weapon equippedWeapon;
    
    public Player(String playerName, ClassType type)
    {
        health = 100;
        level = 1;
        xp = 0;
        gold = 0;
        name = playerName;
        
        statTypeMap = new HashMap<>();
        statTypeMap.put(STR, type.getStatStrength());
        statTypeMap.put(DEX, type.getStatDexterity());
        statTypeMap.put(INT, type.getStatIntelligence());
        
        equippedWeapon = new Weapon(type.getWeapon(), level);
        
        xpToLevelUp = Math.round(STARTING_LEVEL_XP * (float)Math.pow(level, XP_GAIN_FACTOR));
    }
    
    public Player(String[] data)
    {
        loadSaveInfo(data);
        
        xpToLevelUp = Math.round(STARTING_LEVEL_XP * (float)Math.pow(level, XP_GAIN_FACTOR));
    }
    
    public void setWeapon(Weapon weapon)
    {
        equippedWeapon = weapon;
    }
    
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
    
    
    @Override
    public float attack()
    {
        return equippedWeapon.calculateDamage(statTypeMap.get(equippedWeapon.getScalingStat()));
    }
    
    public void healing(int healAmount)
    {
        if (healAmount + health > 100)
        {
            health = 100;
        }
        else
        {
            health += healAmount;
        }
    }
    
    @Override
    public void takeDamage(float damage)
    {
        float totalArmour = 0;
        for (Armour armour : equipped)
        {
            totalArmour += (1 - totalArmour) * armour.calculateProtection();
        }
        
        health -= Math.round(damage * (1 - totalArmour));
        
        if(health <= 0) health = 0;
    }
    
    public int applyXPGain(int xpGain)
    {
        int currentLevel = level;
        xp += xpGain;
        while(xp >= xpToLevelUp)
        {
            level++;
            xp -= xpToLevelUp;
            xpToLevelUp = Math.round(STARTING_LEVEL_XP * (float)Math.pow(level, XP_GAIN_FACTOR));
        }
        
        return level - currentLevel;
    }
    
    public void applyGoldGain(int goldGain) { gold += goldGain; }
    
    public void applyStatGain(StatType stat) { statTypeMap.put(stat, statTypeMap.get(stat) + 1); }
    
    public PlayerError equipWeapon(Weapon weapon)
    {
        if(!inventory.contains(weapon)) return NO_WEAPON_EQUIPPED;
        else
        {
            if(equippedWeapon != null)
            {
                equippedWeapon.unequip();
                inventory.add(equippedWeapon);
                equippedWeapon = weapon;
                inventory.remove(weapon);
                weapon.equip();
                return SUCCESS;
            }
            
            equippedWeapon = weapon;
            weapon.equip();
            return SUCCESS;
        }
    }
    
    public PlayerError unequipWeapon()
    {
        equippedWeapon.unequip();
        equippedWeapon = null;
        return SUCCESS;
    }
    
    public PlayerError equipArmour(Armour armour)
    {
        if(!inventory.contains(armour)) return NO_ARMOR_AVAILABLE;
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
                    return SUCCESS;
                }
            }
            
            equipped.add(armour);
            armour.equip();
            return SUCCESS;
        }
    }
    
    public PlayerError unequipArmour(Armour armour)
    {
        if (!equipped.contains(armour))
        {
            System.out.println("Item is not equipped");
            return NO_ARMOUR_EQUIPPED;
        }
        equipped.remove(armour);
        armour.unequip();
        inventory.add(armour);
        return SUCCESS;
    }
    
    public PlayerError addToInventory(Item item)
    {
        if(item == null)
        {
            return NO_ITEM_GIVEN;
        }
        else if (inventory.size() >= MAXIMUM_INVENTORY_SIZE)
        {
            return MAX_INVENTORY_REACHED;
        }
        inventory.add(item);
        return SUCCESS;
    }
    
    public PlayerError removeFromInventory(Item item)
    {
        if (!inventory.contains(item))
        {
            return ITEM_MISSING;
        }
        inventory.remove(item);
        return SUCCESS;
    }
    
    public String getName() { return name; }
    
    public int getLevel() { return level; }
    
    public float getHealth() { return health; }
    
    public int getXP() { return xp; }
    
    public int getGold() { return gold; }
    
    public Weapon getEquippedWeapon() { return equippedWeapon; }
    
    public List<Item> getInventory() { return inventory; }
    
    public List<Armour> getEquipped() { return equipped; }
}