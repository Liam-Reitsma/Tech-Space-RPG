package game.items;

public abstract class EquippableItem<T> extends Item implements  Equippable<T>
{
    protected boolean isEquipped;
}
