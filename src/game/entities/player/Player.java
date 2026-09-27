package game.entities.player;

import static game.entities.StatType.*;
import static game.entities.player.PlayerErrors.*;

import game.entities.*;
import game.items.*;
import game.items.armour.Armour;
import game.items.weapon.Weapon;

import java.util.*;

/** Player extends Entities and implements Persistable. It holds all the methods that apply to the {@link Player}*/
public class Player extends Entities implements Persistable
{
    @SaveExclude
    private static final int MAXIMUM_INVENTORY_SIZE = 20;
    @SaveExclude
    private static final int STARTING_LEVEL_XP = 15;
    @SaveExclude
    private static final float XP_GAIN_FACTOR = 1.25f;
    
    @SaveExclude
    private int xpToLevelUp;
    private int xp;
    private int gold;
    
    private Potion selectedPotion;
    
    private Map<StatType, Integer> statTypeMap;
    
    @SaveExclude
    private float maximumHealth;
    @SaveExclude
    private float armourPenetration;
    @SaveExclude
    private float damageMultiplier;
    @SaveExclude
    private float armourMultiplier;
    
    @SaveExclude
    private final Inventory inventory;

    /** Used when creating character at the start of a new game.*/
    public Player(String playerName, ClassType type)
    {
        level = 1;
        xp = 0;
        gold = 0;
        name = playerName;
        classType = type;
        point = new Point(0, 0);
        inventory = new Inventory();
        
        statTypeMap = new HashMap<>();
        statTypeMap.put(STR, type.getStatStrength());
        statTypeMap.put(DEX, type.getStatDexterity());
        statTypeMap.put(INT, type.getStatIntelligence());
        maximumHealth = 100 * (1 + statTypeMap.get(STR) * 0.05f);
        armourPenetration = statTypeMap.get(DEX) * 0.005f;
        damageMultiplier = 1 + statTypeMap.get(INT) * 0.006f;
        armourMultiplier = 1 + statTypeMap.get(INT) * 0.003f;
        
        health = maximumHealth;
        
        equippedWeapon = new Weapon(type.getStartingWeapon(), level);
        equippedWeapon.equip();
        
        xpToLevelUp = Math.round(STARTING_LEVEL_XP * (float)Math.pow(level, XP_GAIN_FACTOR));
    }

    /** Used when loading an existing character.*/
    public Player(String[] data)
    {
        loadSaveInfo(data);
        maximumHealth = 100 * (1 + statTypeMap.get(STR) * 0.05f);
        armourPenetration = statTypeMap.get(DEX) * 0.005f;
        damageMultiplier = 1 + statTypeMap.get(INT) * 0.006f;
        armourMultiplier = 1 + statTypeMap.get(INT) * 0.003f;
        xpToLevelUp = Math.round(STARTING_LEVEL_XP * (float)Math.pow(level, XP_GAIN_FACTOR));
    }
    
    public void setWeapon(Weapon weapon) { equippedWeapon = weapon; }
    
    public void setInventory(List<Item> newInventory)
    {
        if(newInventory == null) inventory = new ArrayList<>();
        else inventory = newInventory;
    }
    
    public void setEquippedArmourPieces(List<Armour> newEquipped)
    {
        if(newEquipped == null) equippedArmourPieces = new ArrayList<>();
        else equippedArmourPieces = newEquipped;
    }
    
    public void setSelectedPotion(Potion potion)
    {
        selectedPotion = potion;
    }

    /** First checks for equipped weapon and if there is no weapon equipped, it returns the strength value of {@link Player}. Otherwise, it gets the equipped weapons damage by using calculateDamage() and multiplying by the {@link Player} damage multiplier field. It is then rounded to 4 decimal places.*/
    @Override
    public float attack()
    {
        Weapon weapon = inventory.getEquippedWeapon();
        if(weapon != null) return Math.round((damageMultiplier * weapon.calculateDamage(statTypeMap.get(weapon.getScalingStat())) * 10000f) / 10000f);
        return statTypeMap.get(STR);
    }
    
    public void healing(int healAmount)
    {
        float healthGain = healAmount * statTypeMap.get(STR) * 0.01f;
        
        if(healthGain + health >= maximumHealth)
        {
            health = maximumHealth;
        }
        else
        {
            health += healthGain;
        }
    }

    /** Calculates the damage that is applied to the {@link Player} by first, going through the equipped armours and totalling the damage that the armour pieces blocks, second, subtracting the total protection value from 1, third, adding it to armour penetration value, finally, multiplying by the damage that is given to the {@link Player}.
     * If the health results in being less than or equal to 0, it sets to 0.*/
    @Override
    public void takeDamage(float damage, float armourPen)
    {
        float totalArmour = 0;
        for(Armour armour : inventory.getEquippedArmour())
        {
            totalArmour += (1 - totalArmour) * Math.min(armour.calculateProtection() * armourMultiplier, 1);
        }
        
        health -= Math.round(damage * Math.clamp((1 - totalArmour + armourPen), 0, 1));
        
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

    /** Used to recalculate the stat of the {@link Player} after increasing a stat when levelling up.
     * The ratio is used to get the ratio of the {@link Player} current health and current maximum health due to the maximum health increasing when the strength stat increases.
     * The ratio will allow the current health to increase with the same ratio as before.
     * Armour penetration gets the dexterity stat and multiplies it by a fixed multiplier.
     * Damage multiplier gets the intelligence stat, multiplies it by a fixed multiplier, and adding 1 to it so that the damage value increases when damage multiplier is applied.
     * Armour multiplier gets the intelligence stat, multiplies it by a fixed multiplier, and adding 1 to it so that the armour value increases when armour multiplier is applied.*/
    private void recalculateStats()
    {
        float ratio = health / maximumHealth;
        maximumHealth = 100 * (1 + statTypeMap.get(STR) * 0.05f);
        health = ratio * maximumHealth;
        armourPenetration = statTypeMap.get(DEX) * 0.005f;
        damageMultiplier = 1 + statTypeMap.get(INT) * 0.006f;
        armourMultiplier = 1 + statTypeMap.get(INT) * 0.003f;
    }
    
    public void applyGoldGain(int goldGain) { gold += goldGain; }
    
    public void applyStatGain(StatType stat)
    {
        statTypeMap.put(stat, statTypeMap.get(stat) + 1);
        recalculateStats();
    }
    
    public void equipItem(EquippableItem item)
    {
        inventory.equipItem(item);
    }
    
    public void unequipItem(EquippableItem item)
    {
        inventory.unequipItem(item);
    }
    
    public void addToInventory(Item item)
    {
        inventory.addToInventory(item);
    }
    
    public void removeFromInventory(Item item, boolean removeWholeStack)
    {
        inventory.removeFromInventory(item, removeWholeStack);
    }
    
    public Inventory getInventory()
    {
        return inventory;
    }
    
    public int getXP() { return xp; }
    
    public int getXpToLevelUp() { return xpToLevelUp; }
    
    public int getGold() { return gold; }
    
    public float getMaximumHealth() { return maximumHealth; }
    
    public float getArmourPenetration() { return armourPenetration; }
    
    public Point getPoint() { return point; }
    
    public ClassType getClassType() { return classType; }
}