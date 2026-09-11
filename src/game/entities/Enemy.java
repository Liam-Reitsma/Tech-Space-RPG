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
    public int attack()
    {
        return this.statStr;
    }

    @Override
    public void useAbility()
    {

    }

    @Override
    public void takeDamage(int ouch)
    {
        health -= ouch;
        if (health < 0)
        {
            health = 0;
        }
    }

    public float getHealth() { return health; }

    public EnemyType getEnemyType()
    {
        return enemyType;
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
