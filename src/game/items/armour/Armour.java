package game.items.armour;

import game.items.EquippableItem;

public class Armour extends EquippableItem
{
    private ArmourMaterial armourMaterial;
    private ArmourSlot armourSlot;

    public Armour(ArmourMaterial material, ArmourSlot slot)
    {
        armourMaterial = material;
        armourSlot = slot;
        name = material.toString() + " " + slot.toString();
        isEquipped = false;
    }

    public Armour(String[] data)
    {
        loadSaveInfo(data);
    }
    
    public float calculateProtection() { return Math.round(armourSlot.getProtWeight() * armourMaterial.getProtValue() * 10000f) / 10000f; }
    
    public ArmourMaterial getArmourMaterial() { return armourMaterial; }
    
    public ArmourSlot getArmourSlot() { return armourSlot; }
}