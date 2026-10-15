package game.items;

import game.entities.EnemyType;

import java.util.Random;

public enum PotionType
{
    SMALL_HEALTH(20),
    MEDIUM_HEALTH(40),
    LARGE_HEALTH(60);

    private final int healGain;

    PotionType(int value)
    {
        healGain = value;
    }

    public int getHealAmount() { return healGain; }

    public static PotionType getRandomPotionType()
    {
        return PotionType.values()[((int)(Math.random() * PotionType.values().length))];
    }
    
    @Override
    public String toString()
    {
        String[] words = name().split("_");
        
        for(int i = 0; i < words.length; i++)
            words[i] = words[i].charAt(0) + words[i].substring(1).toLowerCase();
        
        return String.join(" ", words);
    }
    
    public String toFileName()
    {
        return toString().replace(" ", "_");
    }
}