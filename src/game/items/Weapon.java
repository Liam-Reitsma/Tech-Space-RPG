package game.items;

public class Weapon extends EquippableItem
{
    WeaponType weaponType;
    

    public Weapon(WeaponType type, int level)
    {
        weaponType = type;
    }

    public Weapon(String[] data)
    {
        loadSaveInfo(data);
    }

    public WeaponType getWeaponType() { return weaponType; }

    public float getDamage(int stat) { return weaponType.calculateDamage(stat); }

    @Override
    public void equip() { isEquipped = true; }
    @Override
    public void unequip() { isEquipped = false; }
}
