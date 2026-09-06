package game.items;

public class Weapon extends EquippableItem<WeaponType> implements Usable
{
    WeaponType weaponType;

    public Weapon(String name, String desc, WeaponType type)
    {
        title = name;
        description = desc;
        weaponType = type;
    }

    public WeaponType getWeaponType() { return weaponType; }

    @Override
    public int use()
    {
        return weaponType.getBaseDamage();
    }

    @Override
    public void equip()
    {
        isEquipped = true;
    }
    @Override
    public void unequip()
    {
        isEquipped = false;
    }
}
