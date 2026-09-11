package game.items;

import static game.entities.StatType.*;

import game.entities.StatType;

public enum WeaponType
{
    ENERGY_SWORD         (10f, 0.90f, 03f, 0.10f, STR),
    SHOCK_HAMMER         (08f, 1.40f, 05f, 0.20f, STR),
    POWER_FIST           (16f, 1.00f, 08f, 0.25f, STR),
    PLASMA_CLEAVER       (10f, 1.65f, 12f, 0.40f, STR),
    SHOCK_DAGGER         (08f, 1.00f, 03f, 0.10f, DEX),
    PULSE_PISTOL         (16f, 0.95f, 05f, 0.15f, DEX),
    ARC_RIFLE            (20f, 0.90f, 08f, 0.25f, DEX),
    PLASMA_CARBINE       (18f, 1.15f, 10f, 0.30f, DEX),
    PSI_CATALYST         (14f, 0.85f, 03f, 0.25f, INT),
    GRAVITY_CONDUIT      (17f, 0.95f, 04f, 0.45f, INT),
    VOID_SCEPTRE         (20f, 1.05f, 04f, 0.55f, INT),
    SINGULARITY_PROJECTOR(24f, 1.10f, 06f, 0.60f, INT);
    
    private final float baseDamage;
    private final float scalingDamage;
    private final float baseStatReq;
    private final float scalingStatReq;
    private final StatType scalingStat;

    WeaponType(float baseD, float scalingD, float baseSR, float scalingSR, StatType statS)
    {
        baseDamage = baseD;
        scalingDamage = scalingD;
        baseStatReq = baseSR;
        scalingStatReq = scalingSR;
        scalingStat = statS;
    }
    
    public float calculateDamage(int statValue) { return baseDamage + (statValue * scalingDamage); }
    
    public int calculateStatReq(int level) { return (int)(baseStatReq + ((level - 1) * scalingStatReq)); }
    
    public boolean canUseWeapon(int statValue, int level) { return statValue >= calculateStatReq(level); }
    
    public StatType getScalingStat() { return scalingStat; }
}