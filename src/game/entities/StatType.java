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
    
    public String getMessage() { return message; }
    
    @Override
    public String toString() { return name().charAt(0) + name().substring(1).toLowerCase(); }
    
    public static String optionsToString()
    {
        StringBuilder result = new StringBuilder();
        for(StatType stat : StatType.values())
        {
            result.append(stat.message);
        }
        
        return result.toString();
    }
}