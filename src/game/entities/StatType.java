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
        int i = 1;
        for(StatType stat : StatType.values())
        {
            result.append(i++).append(". ").append(stat.toString()).append("\n");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }
}