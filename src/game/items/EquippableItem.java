package game.items;

public abstract class EquippableItem extends Item implements Equippable
{
    public abstract EquipSlot getEquipSlot();
}