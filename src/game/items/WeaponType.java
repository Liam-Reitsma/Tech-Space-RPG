package game.items;

public enum WeaponType
{
    SWORD(10),
    AXE(14),
    BOW(8),
    STAFF(10);

    private final int baseDamage;

    WeaponType(int baseDamage)
    {
        this.baseDamage = baseDamage;
    }

    public int getBaseDamage() { return baseDamage; }
}