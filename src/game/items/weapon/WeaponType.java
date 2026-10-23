package game.items.weapon;

import static game.entities.StatType.*;
import static game.items.weapon.WeaponSlot.MAIN_HAND;

import game.entities.StatType;

public enum WeaponType
{
    RUSTED_KNIFE         (05f, 0.70f,  0, 0.00f, DEX, MAIN_HAND),
    
    ENERGY_SWORD         (10f, 0.90f,  3, 0.10f, STR, MAIN_HAND),
    SHOCK_HAMMER         (08f, 1.40f,  5, 0.20f, STR, MAIN_HAND),
    POWER_FIST           (16f, 1.00f,  8, 0.25f, STR, MAIN_HAND),
    PLASMA_CLEAVER       (10f, 1.65f, 12, 0.40f, STR, MAIN_HAND),
    
    SHOCK_DAGGER         (08f, 1.00f,  3, 0.10f, DEX, MAIN_HAND),
    PULSE_PISTOL         (16f, 0.95f,  5, 0.15f, DEX, MAIN_HAND),
    ARC_RIFLE            (20f, 0.90f,  8, 0.25f, DEX, MAIN_HAND),
    PLASMA_CARBINE       (18f, 1.15f, 10, 0.30f, DEX, MAIN_HAND),
    
    PSI_CATALYST         (14f, 0.85f,  3, 0.25f, INT, MAIN_HAND),
    GRAVITY_CONDUIT      (17f, 0.95f,  4, 0.45f, INT, MAIN_HAND),
    VOID_SCEPTRE         (20f, 1.05f,  4, 0.55f, INT, MAIN_HAND),
    SINGULARITY_PROJECTOR(24f, 1.10f,  6, 0.60f, INT, MAIN_HAND);
    
    private final float baseDamage;
    private final float scalingDamage;
    private final int baseStatReq;
    private final float scalingStatReq;
    private final StatType scalingStat;
    private final WeaponSlot weaponSlot;

    WeaponType(float baseD, float scalingD, int baseSR, float scalingSR, StatType statS, WeaponSlot ws)
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
        String[] words = name().split("_");
        
        for(int i = 0; i < words.length; i++)
            words[i] = words[i].charAt(0) + words[i].substring(1).toLowerCase();
        
        return String.join(" ", words);
    }
    
    public String toFileName()
    {
        return toString().replace(" ", "_");
    }
    
    public float calculateDamage(int statValue, int level) { return baseDamage + ((statValue + level) / 2f  * scalingDamage); }
    
    public int calculateStatReq(int level) { return baseStatReq + (int)((level - 1) * scalingStatReq); }
    
    public boolean canUseWeapon(int statValue, int level) { return statValue >= calculateStatReq(level); }
    
    public StatType getScalingStat() { return scalingStat; }
    
    public WeaponSlot getWeaponSlot() { return weaponSlot; }
    
    public static WeaponType getRandomWeaponType()
    {
        return values()[(int)(Math.random() * values().length)];
    }
}