package game.entities;

public enum StatType
{
    STR("Strength"),
    DEX("Dexterity"),
    INT("Intelligence");
    
    private final String fullName;
    
    StatType(String name)
    {
        fullName = name;
    }
    
    public String getFullName() { return fullName; }
}