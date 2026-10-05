package game.items.armour;

import game.items.EquipSlot;
import game.items.EquippableItem;

import java.util.Objects;

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
    
    @Override
    public EquipSlot getEquipSlot() { return armourSlot; }
    
    public ArmourMaterial getArmourMaterial() { return armourMaterial; }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(armourMaterial, armourSlot);
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(object == null) return false;
        if(!(object instanceof Armour armour)) return false;
        
        return armourMaterial == armour.armourMaterial && armourSlot == armour.armourSlot;
    }
    
}