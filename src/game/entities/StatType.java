package game.entities;

public enum StatType
{
    STR("Strength"),
    DEX("Dexterity"),
    INT("Intelligence");
    
    private final String message;
    
    StatType(String name)
    {
        message = name;
    }
    
    @Override
    public String toString() { return message; }
}
