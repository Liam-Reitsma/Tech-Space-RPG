package game.items;

public enum WeaponType
{
    LIGHTSABER(10),
    DARKSABER(14),
    LASERGUN(8),
    FORCE(10);

    private final int baseDamage;

    WeaponType(int baseDamage)
    {
        this.baseDamage = baseDamage;
    }

    public int getBaseDamage() { return baseDamage; }
}