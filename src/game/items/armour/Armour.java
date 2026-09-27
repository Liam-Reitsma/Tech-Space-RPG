package game.items.armour;

import game.items.EquipSlot;
import game.items.EquippableItem;

public class Armour extends EquippableItem
{
    private ArmourMaterial armourMaterial;
    private ArmourSlot armourSlot;

    public Armour(ArmourMaterial material, ArmourSlot slot)
    {
        armourMaterial = material;
        armourSlot = slot;
        name = armourMaterial + " " + armourSlot;
    }
    
    public Armour(String armourString)
    {
        String armourMaterialString = armourString.split(" ")[0];
        String armourSlotString = armourString.split(" ")[1];
        
        armourMaterial = ArmourMaterial.parse(armourMaterialString);
        armourSlot = ArmourSlot.parse(armourSlotString);
        if(armourMaterial != null & armourSlot != null)
        {
            name = armourMaterial + " " + armourSlot;
        }
    }

    public float calculateProtection() { return Math.round(armourSlot.getProtWeight() * armourMaterial.getProtValue() * 10000f) / 10000f; }
    
    public ArmourMaterial getArmourMaterial() { return armourMaterial; }
    @Override
    public EquipSlot getEquipSlot() { return armourSlot; }
    
    
    public ArmourSlot getArmourSlot() { return armourSlot; }
}