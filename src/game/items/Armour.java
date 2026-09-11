package game.items;

public class Armour extends EquippableItem
{
    private ArmourType armourType;

    public Armour(String armour, ArmourType type)
    {
        this.name = armour;
        armourType = type;
        isEquipped = false;
    }

    public Armour(String[] data)
    {
        loadSaveInfo(data);
    }

    public ArmourType getArmourType() { return armourType; }
}
