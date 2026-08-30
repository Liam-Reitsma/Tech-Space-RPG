package game.entities;

import game.SaveExclude;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Player extends Entities implements Saveable
{
    private int xp;
    private int gold;
    private int statStr;
    private int statDex;
    private int statInt;
    
    private final ClassType classType;
    
    public Player(String playerName, ClassType type)
    {
        xp = 0;
        gold = 0;
        name = playerName;
        classType = type;
        statStr = type.getStatStrength();
        statDex = type.getStatDexterity();
        statInt = type.getStatIntelligence();
    }
    
    @Override
    public void attack()
    {
    
    }
    
    @Override
    public void useAbility()
    {
    
    }
    
    @Override
    public void dealDamage()
    {
    
    }
    @Override
    public void takeDamage()
    {
    
    }
    
    public int getLevel() { return level; }
    
    public int getXP() { return xp; }
    
    public int getGold() { return gold; }
    
    public String getName()
    {
        return name;
    }
    
    public String getClassType()
    {
        return classType.toString();
    }
    
    public int[] getStats()
    {
        return new int[]{statStr, statDex, statInt};
    }
    
    public String getStatsToString()
    {
        return "STR: " + statStr + " | DEX: " + statDex + " | INT: " + statInt;
    }
    
    public void addStat(StatChange[] stats)
    {
        for(StatChange stat : stats)
        {
            switch(stat.statType)
            {
                case STR:
                    statStr += stat.change;
                    break;
                case DEX:
                    statDex += stat.change;
                    break;
                case INT:
                    statInt += stat.change;
                    break;
                default:
                    break;
            }
        }
    }
}
