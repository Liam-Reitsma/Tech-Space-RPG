package game.items;

public class Armour extends EquippableItem
{
    private ArmourType armourType;
    
    private int level;

    public Armour(ArmourType type, int playerLevel)
    {
        name = type.toString();
        armourType = type;
        isEquipped = false;
        level = playerLevel;
    }

    public Armour(String[] data)
    {
        loadSaveInfo(data);
    }

    public ArmourType getArmourType() { return armourType; }
}
