package game.items.armour;

import game.items.EquipSlot;

public enum ArmourSlot implements EquipSlot
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
    
    public float getProtWeight() { return protWeight; }
    
    public static ArmourSlot getRandomSlot()
    {
        return switch((int)(Math.random() * 6) + 1)
        {
            case 1 -> HEAD;
            case 2 -> CHEST;
            case 3 -> ARMS;
            case 4 -> THIGHS;
            case 5 -> SHINS;
            case 6 -> BOOTS;
            default -> null;
        };
    }
}