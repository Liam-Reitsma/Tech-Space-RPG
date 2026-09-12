package game.items;

import game.entities.ClassType;

public enum ArmourMaterial
{
    FABRIC   ( 1, 0.15f),
    POLYMER  ( 5, 0.30f),
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
    
    @Override
    public String toString() { return this.name().charAt(0) + this.name().substring(1).toLowerCase(); }
    
    public int getRequiredLevel() { return  requiredLevel; }
    
    public float getProtValue() { return protValue; }
    
    public static ArmourMaterial getRandomMaterial(int level)
    {
        int i = 0;
        for(ArmourMaterial mat : ArmourMaterial.values()) if(level >= mat.requiredLevel) i++;
        if(i == 0) return FABRIC;
        
        return switch((int)(Math.random() * i) + 1)
        {
            case 1 -> FABRIC;
            case 2 -> POLYMER;
            case 3 -> TITANIUM;
            case 4 -> PLASTEEL;
            case 5 -> NANOWEAVE;
            default -> null;
        };
    }
}