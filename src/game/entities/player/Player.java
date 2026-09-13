package game.entities.player;

import static game.entities.StatType.*;
import static game.entities.player.PlayerErrors.*;

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
    private static final float XP_GAIN_FACTOR = 1.25f;
    
    @SaveExclude
    private int xpToLevelUp;
    private int xp;
    private int gold;
    
    private PotionType selectedPotionType;
    
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
    private List<Item> inventory;
    @SaveExclude
    private List<Armour> equippedArmourPieces;
    @SaveExclude
    private Weapon equippedWeapon;
    
    public Player(String playerName, ClassType type)
    {
        level = 1;
        xp = 0;
        gold = 0;
        name = playerName;
        
        selectedPotionType = null;
        
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
    
    public void setSelectedPotion(PotionType type)
    {
        selectedPotionType = type;
    }
    
    @Override
    public float attack()
    {
        if(equippedWeapon == null) return statTypeMap.get(STR);
        return Math.round(damageMultiplier * equippedWeapon.calculateDamage(statTypeMap.get(equippedWeapon.getScalingStat())) * 10000f) / 10000f;
    }
    
    public void healing(int healAmount)
    {
        float healthGain = healAmount * statTypeMap.get(STR) * 0.01f;
        
        if (healthGain + health >= maximumHealth)
        {
            health = maximumHealth;
        }
        else
        {
            health += healthGain;
        }
    }
    
    @Override
    public void takeDamage(float damage, float armourPen)
    {
        float totalArmour = 0;
        for (Armour armour : equippedArmourPieces)
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
    
    public PlayerResult<Weapon> getWeaponsInInventory()
    {
        Weapon[] inventoryWeapons = new Weapon[10];
        Weapon[] weapons;
        int i = 0;
        for(Item item : getInventory())
        {
            if(item instanceof Weapon weapon)
            {
                inventoryWeapons[i++] = weapon;
            }
        }
        if(i == 0) return new PlayerResult<>(NO_WEAPON_AVAILABLE, new Weapon[0]);
        weapons = new Weapon[i];
        i = 0;
        
        for(Weapon weapon : inventoryWeapons)
        {
            if(weapon == null) continue;
            weapons[i++] = weapon;
        }
        
        return new PlayerResult<>(SUCCESS, weapons);
    }
    
    public PlayerErrors equipWeapon(Weapon weapon)
    {
        if(weapon == null) return NO_WEAPON_GIVEN;
        if(!inventory.contains(weapon)) return NO_WEAPON_AVAILABLE;
        else
        {
            if(equippedWeapon != null)
            {
                equippedWeapon.unequip();
                inventory.add(equippedWeapon);
            }
            
            equippedWeapon = weapon;
            inventory.remove(weapon);
            weapon.equip();
            return SUCCESS;
        }
    }
    
    public PlayerErrors unequipWeapon()
    {
        if(equippedWeapon == null) return NO_WEAPON_EQUIPPED;
        inventory.add(equippedWeapon);
        equippedWeapon.unequip();
        equippedWeapon = null;
        return SUCCESS;
    }
    
    public PlayerResult<Armour> getArmourInInventory()
    {
        Armour[] inventoryArmours = new Armour[10];
        Armour[] armours;
        int i = 0;
        for(Item item : getInventory())
        {
            if(item instanceof Armour armour)
            {
                inventoryArmours[i++] = armour;
            }
        }
        if(i == 0) return new PlayerResult<>(NO_ARMOUR_AVAILABLE, new Armour[0]);
        armours = new Armour[i];
        i = 0;
        
        for(Armour armour : inventoryArmours)
        {
            if(armour == null) continue;
            armours[i++] = armour;
        }
        
        return new PlayerResult<>(SUCCESS, armours);
    }
    
    public PlayerErrors equipArmour(Armour armour)
    {
        if(!inventory.contains(armour)) return NO_ARMOUR_AVAILABLE;
        else
        {
            for(Armour equippedArmour : equippedArmourPieces)
            {
                if(armour.getArmourSlot() == equippedArmour.getArmourSlot())
                {
                    equippedArmour.unequip();
                    inventory.add(equippedArmour);
                }
            }
            
            equippedArmourPieces.add(armour);
            inventory.remove(armour);
            armour.equip();
            return SUCCESS;
        }
    }
    
    public PlayerErrors unequipArmour(Armour armour)
    {
        if(equippedArmourPieces.isEmpty()) { return NO_ARMOUR_EQUIPPED; }
        if(!equippedArmourPieces.contains(armour)) { return NO_SLOT_ARMOUR_EQUIPPED; }
        equippedArmourPieces.remove(armour);
        armour.unequip();
        inventory.add(armour);
        return SUCCESS;
    }
    
    public PlayerResult<Potion> getPotionsInInventory()
    {
        Potion[] inventoryPotions = new Potion[10];
        Potion[] potions;
        int i = 0;
        for(Item item : getInventory())
        {
            if(item instanceof Potion potion)
            {
                inventoryPotions[i++] = potion;
            }
        }
        if(i == 0) return new PlayerResult<>(NO_POTION_AVAILABLE, new Potion[0]);
        potions = new Potion[i];
        i = 0;
        
        for(Potion potion : inventoryPotions)
        {
            if(potion == null) continue;
            potions[i++] = potion;
        }
        
        return new PlayerResult<>(SUCCESS, potions);
    }
    
    public PlayerErrors addToInventory(Item item)
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
    
    public PlayerErrors removeFromInventory(Item item)
    {
        if (!inventory.contains(item))
        {
            return ITEM_MISSING;
        }
        inventory.remove(item);
        return SUCCESS;
    }
    
    public int getXP() { return xp; }
    
    public int getXpToLevelUp() { return xpToLevelUp; }
    
    public int getGold() { return gold; }
    
    public float getMaximumHealth() { return maximumHealth; }
    
    public float getArmourPenetration() { return armourPenetration; }
    
    public Weapon getEquippedWeapon() { return equippedWeapon; }
    
    public List<Item> getInventory() { return inventory; }
    
    public List<Armour> getEquippedArmour() { return equippedArmourPieces; }
    
    public PlayerResult<Potion> getSelectedPotion()
    {
        if(selectedPotionType == null) return new PlayerResult<>(NO_POTION_SELECTED, new Potion[0]);
        return new PlayerResult<>(SUCCESS, new Potion[]{new Potion(selectedPotionType)});
    }
}