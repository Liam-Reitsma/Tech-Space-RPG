package game.items;

public class Weapon extends Item implements Usable
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
}
