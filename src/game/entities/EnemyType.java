package game.entities;

public enum EnemyType
{
    KRORK(5, 3, 3, 150, 100, 20, "Krork"),
    CHITIN(3, 5, 3, 110, 70, 15, "Chitin"),
    CHRONOMANCER(3, 3, 5, 100, 60, 10, "Chronomancer"),
    ATOM(0, 0 ,0, 0, 0, 0, "Atom");

    private final int statStrength;
    private final int statDexterity;
    private final int statIntelligence;
    private final int statHealth;
    private final int goldDrop;
    private final int xpDrop;
    private final String name;


    EnemyType(int statStr, int statDex, int statIntel, int hp, int gd, int xp, String n) {
        statStrength = statStr;
        statDexterity = statDex;
        statIntelligence = statIntel;
        statHealth = hp;
        goldDrop = gd;
        xpDrop = xp;
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

    public int getGoldDrop() { return goldDrop; }

    public int getXpDrop() { return xpDrop; }

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
