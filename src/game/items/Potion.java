package game.items;

public class Potion extends Item
{
    private final PotionType potionType;

    public Potion(String name, String desc, PotionType type)
    {
        title = name;
        description = desc;
        potionType = type;
    }

    public PotionType getPotionType() { return potionType; }
}
