package game.items;

public enum ArmourMaterial
{
    FABRIC   (01, 0.15f),
    POLYMER  (05, 0.30f),
    TITANIUM (10, 0.45f),
    PLASTEEL (20, 0.60f),
    NANOWEAVE(35, 0.75f);
    
    private final int requiredLevel;
    private final float protValue;
    
    ArmourMaterial(int reqLevel, float pValue)
    {
        requiredLevel = reqLevel;
        protValue = pValue;
    }
    
    public int getRequiredLevel() { return  requiredLevel; }
    
    public float getProtValue() { return protValue; }
}
