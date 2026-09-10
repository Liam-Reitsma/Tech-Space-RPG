package game.entities;

public enum ClassType
{
    JUGGERNAUT(5, 3, 3, "Juggernaut"),
    MERCENARY(3, 5, 3, "Mercenary"),
    SAGE(3, 3, 5, "Sage"),
    UNSELECTED(0, 0 ,0, "Unselected");
    
    private final int statStrength;
    private final int statDexterity;
    private final int statIntelligence;
    private final String name;
    
    ClassType(int sStr, int sDex, int sInt, String n)
    {
        statStrength = sStr;
        statDexterity = sDex;
        statIntelligence = sInt;
        name = n;
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