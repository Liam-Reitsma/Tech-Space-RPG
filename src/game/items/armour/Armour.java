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

    /**
     * Calculates the protection value by considering the slot's protection weight and material's protection value.
     *
     * @return The protection value as a decimal fraction, rounded to four decimal places.
     */
    public float calculateProtection()
    {
        return Math.round(armourSlot.getProtWeight() * armourMaterial.getProtValue() * 10000f) / 10000f;
    }
    
    public ArmourMaterial getArmourMaterial() { return armourMaterial; }
    
    @Override
    public EquipSlot getEquipSlot() { return armourSlot; }
    
    @Override
    public String getImageURL()
    {
        return "resources/images/items/" + armourMaterial.toFileName() + "_" + armourSlot.toFileName() + ".png";
    }
    
    @Override
    public Armour copy()
    {
        return new Armour(armourMaterial, armourSlot);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(armourMaterial, armourSlot);
    }
    
    @Override
    public boolean equals(Object object)
    {
        if(this == object)
            return true;
        if(!(object instanceof Armour armour))
            return false;
        
        return armourMaterial == armour.armourMaterial && armourSlot == armour.armourSlot;
    }
}