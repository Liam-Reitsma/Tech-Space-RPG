package game.items;

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

    public int use()
    {
        return potionType.getHealAmount();
    }
}