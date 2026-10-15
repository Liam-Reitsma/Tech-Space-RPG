package game.items.weapon;

import static game.entities.StatType.*;
import static game.items.weapon.WeaponSlot.MAIN_HAND;

import game.entities.StatType;

public enum WeaponType
{
    RUSTED_KNIFE         (05f, 0.70f, 00f, 0.00f, DEX, MAIN_HAND),
    
    ENERGY_SWORD         (10f, 0.90f, 03f, 0.10f, STR, MAIN_HAND),
    SHOCK_HAMMER         (08f, 1.40f, 05f, 0.20f, STR, MAIN_HAND),
    POWER_FIST           (16f, 1.00f, 08f, 0.25f, STR, MAIN_HAND),
    PLASMA_CLEAVER       (10f, 1.65f, 12f, 0.40f, STR, MAIN_HAND),
    
    SHOCK_DAGGER         (08f, 1.00f, 03f, 0.10f, DEX, MAIN_HAND),
    PULSE_PISTOL         (16f, 0.95f, 05f, 0.15f, DEX, MAIN_HAND),
    ARC_RIFLE            (20f, 0.90f, 08f, 0.25f, DEX, MAIN_HAND),
    PLASMA_CARBINE       (18f, 1.15f, 10f, 0.30f, DEX, MAIN_HAND),
    
    PSI_CATALYST         (14f, 0.85f, 03f, 0.25f, INT, MAIN_HAND),
    GRAVITY_CONDUIT      (17f, 0.95f, 04f, 0.45f, INT, MAIN_HAND),
    VOID_SCEPTRE         (20f, 1.05f, 04f, 0.55f, INT, MAIN_HAND),
    SINGULARITY_PROJECTOR(24f, 1.10f, 06f, 0.60f, INT, MAIN_HAND);
    
    private final float baseDamage;
    private final float scalingDamage;
    private final float baseStatReq;
    private final float scalingStatReq;
    private final StatType scalingStat;
    private final WeaponSlot weaponSlot;

    WeaponType(float baseD, float scalingD, float baseSR, float scalingSR, StatType statS, WeaponSlot ws)
    {
        baseDamage = baseD;
        scalingDamage = scalingD;
        baseStatReq = baseSR;
        scalingStatReq = scalingSR;
        scalingStat = statS;
        weaponSlot = ws;
    }
    
    @Override
    public String toString()
    {
        String[] s = this.name().split("_");
        return s[0].charAt(0) + s[0].substring(1).toLowerCase() + " " + s[1].charAt(0) + s[1].substring(1).toLowerCase();
    }
    
    public float calculateDamage(int statValue, int level) { return baseDamage + ((statValue + level) / 2f  * scalingDamage); }
    
    public int calculateStatReq(int level) { return (int)(baseStatReq + ((level - 1) * scalingStatReq)); }
    
    public boolean canUseWeapon(int statValue, int level) { return statValue >= calculateStatReq(level); }
    
    public StatType getScalingStat() { return scalingStat; }
    
    public WeaponSlot getWeaponSlot() { return weaponSlot; }
    
    public static WeaponType getRandomWeaponType()
    {
        return values()[Math.round((float)(Math.random() * values().length))];
    }
}