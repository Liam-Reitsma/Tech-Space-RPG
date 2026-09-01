package game.entities;

public enum ClassType
{
    WARRIOR(5, 3, 3, "warrior"),
    ROGUE(3, 5, 3, "rogue"),
    MAGE(3, 3, 5, "mage");
    
    private final int statStrength;
    private final int statDexterity;
    private final int statIntelligence;
    private final String name;
    
    ClassType(int _str, int _dex, int _int, String _name)
    {
        statStrength = _str;
        statDexterity = _dex;
        statIntelligence = _int;
        name = _name;
    }
    
    public int getStatStrength()
    {
        return statStrength;
    }
    
    public int getStatDexterity()
    {
        return statDexterity;
    }
    
    public int getStatIntelligence()
    {
        return statIntelligence;
    }
    
    @Override
    public String toString()
    {
        return name;
    }
    
    public static ClassType parseType(String typeToCheck)
    {
        for(ClassType type : ClassType.values())
        {
            if(type.toString().equals(typeToCheck)) return type;
        }
        
        return null;
    }
}