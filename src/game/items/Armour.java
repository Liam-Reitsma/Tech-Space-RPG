package game.items;

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
    
    public float calculateProtection() { return armourSlot.getProtWeight() * armourMaterial.getProtValue(); }
    
    public ArmourMaterial getArmourMaterial() { return armourMaterial; }
    
    public ArmourSlot getArmourSlot() { return armourSlot; }
}