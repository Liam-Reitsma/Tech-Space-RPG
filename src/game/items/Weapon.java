package game.items;

import game.entities.StatType;

public class Weapon extends EquippableItem
{
    private WeaponType weaponType;
    
    private int level;
    
    public Weapon(WeaponType type, int playerLevel)
    {
        name = type.toString();
        weaponType = type;
        level = playerLevel;
    }

    public Weapon(String[] data)
    {
        loadSaveInfo(data);
    }

    public float calculateDamage(int stat) { return weaponType.calculateDamage(stat); }
    
    public int calculateStatReq() { return weaponType.calculateStatReq(level); }
    
    public boolean canUseWeapon(int statValue) { return weaponType.canUseWeapon(statValue, level); }
    
    public StatType getScalingStat() { return weaponType.getScalingStat(); }
    
    public WeaponType getWeaponType() { return weaponType; }
}