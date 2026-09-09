package game.entities;

import jdk.jshell.spi.ExecutionEnv;

public class Enemy extends Entities implements Persistable
{
    private int statStr;
    private int statDex;
    private int statInt;

    private EnemyType enemyType;

    public Enemy(String n, EnemyType type)
    {
        name = n;
        enemyType = type;
        statStr = type.getStatStrength();
        statDex = type.getStatDexterity();
        statInt = type.getStatIntelligence();
        health = type.getHealth();
    }

    public Enemy(String[] data) { loadSaveInfo(data); }

    @Override
    public void attack()
    {

    }

    @Override
    public void useAbility()
    {

    }

    @Override
    public void takeDamage(int ouch)
    {
        health -= ouch;
    }

    public float getHealth() { return health; }

    public String getEnemyType()
    {
        return enemyType.toString();
    }

    public int[] getStats()
    {
        return new int[]{statStr, statDex, statInt};
    }

    public String getStatsToString()
    {
        return "STR: " + statStr + " | DEX: " + statDex + " | INT: " + statInt;
    }
}
