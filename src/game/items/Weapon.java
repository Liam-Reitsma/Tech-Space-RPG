package game.items;

public class Weapon extends Item
{
    WeaponType weaponType;

    public Weapon(String name, String desc, WeaponType type)
    {
        title = name;
        description = desc;
        weaponType = type;
    }

    public WeaponType getWeaponType() { return weaponType; }
}
