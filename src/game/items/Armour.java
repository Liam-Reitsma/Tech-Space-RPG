package game.items;

public class Armour extends EquippableItem
{
    private ArmourType armourType;

    public Armour(String name, String desc, ArmourType type)
    {
        title = name;
        description = desc;
        armourType = type;
        isEquipped = false;
    }

    public Armour(String[] data)
    {
        loadSaveInfo(data);
    }

    public ArmourType getArmourType()
    {
        return armourType;
    }

    @Override
    public void equip()
    {
        isEquipped = true;
    }

    @Override
    public void unequip()
    {
        isEquipped = false;
    }
}
