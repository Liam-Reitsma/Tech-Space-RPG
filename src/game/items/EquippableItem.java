package game.items;

public abstract class EquippableItem extends Item implements Equippable
{
    protected boolean isEquipped;
    
    @Override
    public void equip() { isEquipped = true; }
    
    @Override
    public void unequip() { isEquipped = false; }
}