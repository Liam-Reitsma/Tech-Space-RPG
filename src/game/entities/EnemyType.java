package game.entities;

public enum EnemyType
{
    KRORK(5, 3, 3, 150, "krork"),
    CHITIN(3, 5, 3, 110, "chitin"),
    CHRONOMANCER(3, 3, 5, 100, "chronomancer"),
    ATOM(0, 0 ,0, 0, "atom");

    private final int statStrength;
    private final int statDexterity;
    private final int statIntelligence;
    private final int statHealth;
    private final String name;


    EnemyType(int statStr, int statDex, int statIntel, int hp, String n) {
        statStrength = statStr;
        statDexterity = statDex;
        statIntelligence = statIntel;
        statHealth = hp;
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

    public int getHealth() { return statHealth; }

    @Override
    public String toString()
    {
        return name;
    }

    public static EnemyType parseType(String typeToCheck)
    {
        for(EnemyType type : EnemyType.values())
        {
            if(type.toString().equals(typeToCheck)) return type;
        }

        return null;
    }
}
