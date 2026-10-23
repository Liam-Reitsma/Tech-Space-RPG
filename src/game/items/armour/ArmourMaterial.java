package game.items.armour;

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
    
    public int getRequiredLevel() { return  requiredLevel; }
    
    public float getProtValue() { return protValue; }

    /**
     * Randomly selects an armour material whose required level does not exceed the supplied level. Defaults to FABRIC if none qualify.
     *
     * @param level The level used to determine the eligible material.
     * @return The randomly selected armour material, or fabric if none qualify.
     */
    public static ArmourMaterial getRandomMaterial(int level)
    {
        int i = 0;
        for(ArmourMaterial mat : ArmourMaterial.values())
            if(level >= mat.requiredLevel)
                i++;
        
        if(i == 0)
            return FABRIC;
        
        return values()[(int)(Math.random() * i)];
    }
}