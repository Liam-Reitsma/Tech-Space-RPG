package game.player.model;

import static game.entities.StatType.*;

import game.entities.*;
import game.items.*;
import game.items.armour.Armour;
import game.items.weapon.Weapon;
import game.world.model.Direction;
import game.world.model.Point;

import java.util.*;

/** Player extends Entities and implements Persistable. It holds all the methods that apply to the {@link Player}*/
public class Player extends Entities
{
    private static final int STARTING_LEVEL_XP = 15;
    private static final float XP_GAIN_FACTOR = 1.25f;
    
    private int gold;
    private int xp;
    private int xpToLevelUp;
    
    private float armourMultiplier;
    private float armourPenetration;
    private float damageMultiplier;
    private float maximumHealth;
    
    private Point point;
    
    private final ClassType classType;
    private final Map<StatType, Integer> statTypeMap;
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
        Weapon startingWeapon = new Weapon(classType.getStartingWeapon(), level);
        inventory.addToInventory(startingWeapon);
        inventory.equipItem(startingWeapon, startingWeapon.getEquipSlot());
        
        statTypeMap = new HashMap<>();
        statTypeMap.put(STR, type.getStatStrength());
        statTypeMap.put(DEX, type.getStatDexterity());
        statTypeMap.put(INT, type.getStatIntelligence());
        maximumHealth = 100 * (1 + statTypeMap.get(STR) * 0.05f);
        armourPenetration = statTypeMap.get(DEX) * 0.005f;
        damageMultiplier = 1 + statTypeMap.get(INT) * 0.006f;
        armourMultiplier = 1 + statTypeMap.get(INT) * 0.003f;
        
        health = maximumHealth;
        
        xpToLevelUp = Math.round(STARTING_LEVEL_XP * (float)Math.pow(level, XP_GAIN_FACTOR));
    }

    /** Used when loading an existing character.*/
    public Player(String loadedName, int loadedLevel, float loadedHealth, int loadedXP, int loadedGold, ClassType loadedClassType, Point loadedPoint, int loadedSTR, int loadedDEX, int loadedINT)
    {
        name = loadedName;
        level = loadedLevel;
        health = loadedHealth;
        xp = loadedXP;
        gold = loadedGold;
        classType = loadedClassType;
        point = loadedPoint;

        inventory = new Inventory();
        statTypeMap = new HashMap<>();
        statTypeMap.put(STR, loadedSTR);
        statTypeMap.put(DEX, loadedDEX);
        statTypeMap.put(INT, loadedINT);
        
        maximumHealth = 100 * (1 + statTypeMap.get(STR) * 0.05f);
        armourPenetration = statTypeMap.get(DEX) * 0.005f;
        damageMultiplier = 1 + statTypeMap.get(INT) * 0.006f;
        armourMultiplier = 1 + statTypeMap.get(INT) * 0.003f;
        
        xpToLevelUp = Math.round(STARTING_LEVEL_XP * (float)Math.pow(level, XP_GAIN_FACTOR));
    }
    
    /** First checks for equipped weapon and if there is no weapon equipped, it returns the strength value of {@link Player}. Otherwise, it gets the equipped weapons damage by using calculateDamage() and multiplying by the {@link Player} damage multiplier field. It is then rounded to 4 decimal places.*/
    @Override
    public float calculateAttackDamage()
    {
        Weapon weapon = inventory.getEquippedWeapon();
        return weapon == null ? statTypeMap.get(STR) : damageMultiplier * weapon.calculateDamage(statTypeMap.get(weapon.getScalingStat()));
    }
    
    /** Calculates the damage that is applied to the {@link Player} by first, going through the equipped armours and totalling the damage that the armour pieces blocks, second, subtracting the total protection value from 1, third, adding it to armour penetration value, finally, multiplying by the damage that is given to the {@link Player}.
     * If the health results in being less than or equal to 0, it sets to 0.*/
    @Override
    public void applyDamage(float damage, float armourPen)
    {
        if(damage <= 0)
            return;
        
        health -= damage * Math.clamp((1 - getProtectionValue() + armourPen), 0f, 1f);
        
        health = Math.max(health, 0);
    }
    
    public float getProtectionValue()
    {
        float totalArmour = 0;
        for(Armour armour : inventory.getEquippedArmour())
            totalArmour += (1 - totalArmour) * Math.min(armour.calculateProtection() * armourMultiplier, 1);
        
        return totalArmour;
    }
    
    public void applyHealing(int healAmount)
    {
        if(healAmount <= 0)
            return;
        
        float healthGain = healAmount * (1 + statTypeMap.get(STR) * 0.05f);
        
        if(healthGain + health >= maximumHealth)
            health = maximumHealth;
        else
            health += healthGain;
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
    
    public void applyStatGain(StatType stat)
    {
        statTypeMap.put(stat, statTypeMap.get(stat) + 1);
        recalculateStats();
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
    
    public void applyGoldGain(int goldGain)
    {
        gold += goldGain;
    }
    
    public boolean addToInventory(Item item)
    {
        return inventory.addToInventory(item);
    }
    
    public boolean removeFromInventory(Item item, boolean removeStack)
    {
        return inventory.removeFromInventory(item, removeStack);
    }
    
    public boolean equipItem(int itemIndex, EquipSlot targetEquipSlot)
    {
        return inventory.equipItem(itemIndex, targetEquipSlot);
    }
    
    public boolean equipItem(EquippableItem item, EquipSlot slot)
    {
        return inventory.equipItem(item, slot);
    }
    
    public boolean unequipItem(EquipSlot equipSlot, int itemIndex)
    {
        return inventory.unequipItem(equipSlot, itemIndex);
    }
    
    public boolean unequipItem(EquippableItem item, int targetIndex)
    {
        return inventory.unequipItem(item, targetIndex);
    }
    
    public void removeFromEquipped(EquippableItem equippedItem)
    {
        inventory.removeFromEquipped(equippedItem);
    }
    
    public void swapItems(int originalIndex, int targetIndex)
    {
        inventory.swapItems(originalIndex, targetIndex);
    }
    
    public void movePoint(Direction direction)
    {
        point = point.move(direction);
    }
    
    public int getGold() { return gold; }
    
    public int getXP() { return xp; }
    
    public int getXpToLevelUp() { return xpToLevelUp; }
    
    public float getMaximumHealth() { return maximumHealth; }
    
    public float getArmourPenetration() { return armourPenetration; }
    
    public float getDamageAmplification() { return damageMultiplier; }
    
    public float getDamageReduction() { return armourMultiplier; }
    
    public Point getPoint() { return point; }
    
    public ClassType getClassType() { return classType; }
    
    public int getStatValue(StatType statType) { return statTypeMap.get(statType); }
    
    public int getSTR() { return statTypeMap.get(STR); }
    
    public int getDEX() { return statTypeMap.get(DEX); }
    
    public int getINT() { return statTypeMap.get(INT); }
    
    public ReadInventory getInventory() { return inventory; }
    
    public void setInventory(ItemStack[] loadedItemStacks) { inventory.setInventory(loadedItemStacks); }
    
    public ItemStack[] getInventorySlots() { return inventory.getInventorySlots(); }
    
    public void setEquipped(EquippableItem[] loadedEquippedArray) { inventory.setEquipped(loadedEquippedArray); }
}