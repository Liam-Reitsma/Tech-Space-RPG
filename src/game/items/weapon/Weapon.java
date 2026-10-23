package game.items.weapon;

import game.entities.StatType;
import game.items.EquipSlot;
import game.items.EquippableItem;

import java.util.Objects;

public class Weapon extends EquippableItem
{
    private final WeaponType weaponType;
    private final WeaponSlot weaponSlot;
    
    private final int level;
    
    public Weapon(WeaponType type, int playerLevel)
    {
        name = type.toString();
        weaponType = type;
        weaponSlot = type.getWeaponSlot();
        level = playerLevel;
    }

    public float calculateDamage(int stat) { return weaponType.calculateDamage(stat, level); }
    
    public boolean canUseWeapon(int statValue) { return weaponType.canUseWeapon(statValue, level); }
    
    public int getLevel() { return level; }
    
    public StatType getScalingStat() { return weaponType.getScalingStat(); }
    
    public WeaponType getWeaponType() { return weaponType; }
    
    @Override
    public String getImageURL()
    {
        return "resources/images/items/" + weaponType.toFileName() + ".png";
    }
    
    @Override
    public EquipSlot getEquipSlot() { return weaponSlot; }
    
    @Override
    public Weapon copy()
    {
        return new Weapon(weaponType, level);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(weaponType, weaponSlot, level);
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(this == object)
            return true;
        if(!(object instanceof Weapon weapon))
            return false;
        
        return weaponType == weapon.weaponType && weaponSlot == weapon.weaponSlot && level == weapon.level;
    }
}