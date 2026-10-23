package game.items;

public abstract class EquippableItem extends Item implements Equippable
{
    public abstract EquipSlot getEquipSlot();
    @Override
    public abstract EquippableItem copy();
}