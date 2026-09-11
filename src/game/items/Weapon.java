package game.items;

import game.entities.StatType;

public class Weapon extends EquippableItem
{
    private int level;
    
    private WeaponType weaponType;

    public Weapon(WeaponType type, int playerLevel)
    {
        level = playerLevel;
        weaponType = type;
    }

    public Weapon(String[] data)
    {
        loadSaveInfo(data);
    }

    public WeaponType getWeaponType() { return weaponType; }

    public float calculateDamage(int stat) { return weaponType.calculateDamage(stat); }
    
    public int calculateStatReq() { return weaponType.calculateStatReq(level); }
    
    public boolean canUseWeapon(int statValue) { return weaponType.canUseWeapon(statValue, level); }
    
    public StatType getScalingStat() { return weaponType.getScalingStat(); }
}