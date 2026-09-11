package game.items;

public enum ArmourSlot
{
    HEAD  (0.20f),
    CHEST (0.40f),
    ARMS  (0.10f),
    THIGHS(0.15f),
    SHINS (0.10f),
    BOOTS (0.05f);
    
    private final float protWeight;
    
    ArmourSlot(float pWeight)
    {
        protWeight = pWeight;
    }
    
    public float getProtWeight() { return protWeight; }
}
