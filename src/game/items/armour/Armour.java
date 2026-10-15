package game.items.armour;

import game.items.EquipSlot;
import game.items.EquippableItem;

import java.util.Objects;

public class Armour extends EquippableItem
{
    private final ArmourMaterial armourMaterial;
    private final ArmourSlot armourSlot;

    public Armour(ArmourMaterial material, ArmourSlot slot)
    {
        armourMaterial = material;
        armourSlot = slot;
        name = armourMaterial + " " + armourSlot;
    }

    public float calculateProtection() { return Math.round(armourSlot.getProtWeight() * armourMaterial.getProtValue() * 10000f) / 10000f; }
    
    public String getImageURL() { return "resources/images/items/" + armourMaterial.toString() + "_" + armourSlot.toString() + ".png"; }
    
    public ArmourMaterial getArmourMaterial() { return armourMaterial; }
    
    @Override
    public EquipSlot getEquipSlot() { return armourSlot; }
    
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