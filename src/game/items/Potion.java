package game.items;

public class Potion extends Item
{
    private PotionType potionType;

    public Potion(String potion, PotionType type)
    {
        name = potion;
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
