package game.items;

import java.util.Random;

public class Potion extends Item
{
    private PotionType potionType;

    public Potion(PotionType type)
    {
        name = type.toString();
        potionType = type;
    }

    public Potion(String[] data)
    {
        loadSaveInfo(data);
    }

    public PotionType getPotionType() { return potionType; }

    public PotionType getRandomPotionType()
    {
        int random = new Random().nextInt(PotionType.values().length);
        return PotionType.values()[random];
    }

    public int use()
    {
        return potionType.getHealAmount();
    }

    @Override
    public String toString()
    {
        return potionType.toString();
    }
}