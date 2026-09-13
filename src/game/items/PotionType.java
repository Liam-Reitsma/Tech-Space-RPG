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
        String[] s = this.name().split("_");
        return s[0].charAt(0) + s[0].substring(1).toLowerCase() + " " + s[1].charAt(0) + s[1].substring(1).toLowerCase();
    }
}