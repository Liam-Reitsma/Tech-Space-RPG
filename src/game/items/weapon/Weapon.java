package game.items.weapon;

import game.entities.StatType;
import game.items.EquipSlot;
import game.items.EquippableItem;

import java.util.Objects;

public class Weapon extends EquippableItem
{
    private final WeaponType weaponType;
    private final WeaponSlot weaponSlot;
    
    private int level;
    
    public Weapon(WeaponType type, int playerLevel)
    {
        name = type.toString();
        weaponType = type;
        weaponSlot = type.getWeaponSlot();
        level = playerLevel;
    }

    public float calculateDamage(int stat) { return weaponType.calculateDamage(stat, level); }
    
    public int calculateStatReq() { return weaponType.calculateStatReq(level); }
    
    public boolean canUseWeapon(int statValue) { return weaponType.canUseWeapon(statValue, level); }
    
    public StatType getScalingStat() { return weaponType.getScalingStat(); }
    
    public WeaponType getWeaponType() { return weaponType; }
    
    @Override
    public EquipSlot getEquipSlot() { return weaponSlot; }
    
}