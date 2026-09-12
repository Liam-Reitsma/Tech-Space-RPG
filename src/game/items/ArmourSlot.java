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
    
    @Override
    public String toString() { return this.name().charAt(0) + this.name().substring(1).toLowerCase(); }
    
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
    
    public static ArmourSlot parseType(String typeToCheck)
    {
        for(ArmourSlot slot : ArmourSlot.values())
        {
            if(slot.toString().equalsIgnoreCase(typeToCheck)) return slot;
        }
        
        return null;
    }
}